package dbms.database.table.page;

import dbms.database.table.Column;
import dbms.database.table.Header;
import dbms.exceptions.InvalidValueException;
import dbms.database.cell.CellFactory;
import dbms.database.cell.ICell;
import dbms.database.builders.RecordBuilder;
import dbms.utilities.ExtendedRaf;
import dbms.interfaces.IBundleable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Record implements IBundleable<Record> {

    private final LeafPage page;
    private final Header header;
    private List<ICell> cells;
    private boolean deleted;

    public Record(LeafPage page, RecordBuilder builder) {
        this.page = page;
        this.header = builder.getHeader();
        this.cells = initializeCells(builder.getValues());
        this.deleted = false;
    }

    private List<ICell> initializeCells(List<String> values) {
        List<Column> columns = this.header.getColumns();
        return IntStream.range(0, columns.size()).mapToObj(i -> CellFactory.build(this, columns.get(i), values.get(i))).toList();
    }

    @Override
    public Record getObjectCopy() {
        List<String> cellValues = this.cells.stream().map(ICell::getValue).toList();
        return new Record(this.page, new RecordBuilder(this.header, cellValues));
    }

    @Override
    public void setObjectState(Record object) {
        this.cells = object.getCells();
    }

    public boolean setValues(Map<String, String> values) {
       return this.mutateInBundle(
           bundledRecord -> {
               for (String key : values.keySet()) {
                   ICell cell = bundledRecord.getCellWithName(key);
                   if (cell != null) cell.setValue(values.get(key));
               }
           },
           exception -> {
               if (exception instanceof InvalidValueException) {
                   System.out.println(exception.getMessage());
               } else {
                   throw new RuntimeException(exception);
               }
           }
       );
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    private int getIndexInPage() {
        return this.page.getRecords().indexOf(this);
    }

    public int getAbsoluteOffset() {
        return this.page.getAbsoluteOffset() + this.getPageRelativeOffset();
    }

    public int getPageRelativeOffset() {
        return LeafPage.PAGE_SIZE - ((this.getIndexInPage() + 1) * header.getRecordSize());
    }

    public int getRowIDValue() {
        return Integer.parseInt(this.getCellWithName(Header.ROW_ID_COLUMN_NAME).getValue());
    }

    public ICell getPrimaryKeyCell() {
        return this.cells.get(this.header.getPrimaryKeyColumn().getIndex());
    }

    public List<ICell> getCells() {
        return new ArrayList<>(this.cells);
    }

    public ICell getCellWithName(String name) {
        return this.cells.stream().filter(c -> c.getColumn().getName().equals(name)).findFirst().orElse(null);
    }

    public List<String> getValues() {
        return new ArrayList<>(this.cells.stream().map(ICell::getValue).toList()).subList(0, this.cells.size());
    }

    public String getValueWithColumnName(String name) {
        return this.getCellWithName(name).getValue();
    }

    public List<String> getValuesWithColumnNames(List<String> names) {
        return new ArrayList<>(names.stream().map(this::getValueWithColumnName).toList());
    }

    public ICell getCellByIndex(int index) {
        return this.cells.get(index);
    }

    public void validate() throws InvalidValueException {
        for (ICell cell : this.cells) { cell.validate(); }
    }

    public boolean meetsCriteria(Criteria criteria) throws InvalidValueException {
        for (ICell cell : this.cells) {
            if(!criteria.validateCell(cell)) return false;
        }

        return true;
    }

    public void write(ExtendedRaf raf) throws IOException {
        // write payload size
        raf.seek(this.getAbsoluteOffset());
        raf.writeShort(this.header.getRecordSize());

        // write row id
        raf.seek(this.getAbsoluteOffset() + 2);
        raf.writeInt(this.getRowIDValue());

        // write deletion byte
        raf.seek(this.getAbsoluteOffset() + 6);
        raf.writeByte(this.deleted ? 1 : 0);

        // write number of columns
        raf.seek(this.getAbsoluteOffset() + 7);
        raf.writeShort(this.header.getColumns().size());

        // write column data types
        raf.seek(this.getAbsoluteOffset() + 9);
        for (ICell c : this.cells) { raf.writeShort(c.getDataTypeCode()); }

        // write cell data
        raf.seek(this.getAbsoluteOffset() + 9 + (this.cells.size() * 2L));
        for (ICell cell: this.cells) { cell.write(raf); }
    }

    public void read(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());

        // read deletion byte
        raf.seek(this.getAbsoluteOffset() + 6);
        this.deleted = raf.readByte() == 1;

        // read cell data
        raf.seek(this.getAbsoluteOffset() + 9 + (this.cells.size() * 2L));
        for (ICell cell: this.cells) { cell.read(raf); }
    }

    public String toString() {
        return new ArrayList<>(this.cells.stream().map(ICell::getValue).toList()).toString();
    }
}
