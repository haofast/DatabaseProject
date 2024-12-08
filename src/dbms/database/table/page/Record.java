package dbms.database.table.page;

import dbms.database.cell.AbstractCell;
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

    private final Page page;
    private final Header header;
    private List<ICell> cells;

    public Record(Page page, RecordBuilder builder) {
        this.page = page;
        this.header = builder.getHeader();
        this.cells = initializeCells(builder.getValues());
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

    private int getIndexInPage() {
        return this.page.getRecords().indexOf(this);
    }

    public int getAbsoluteOffset() {
        return this.page.getAbsoluteOffset() + this.getPageRelativeOffset();
    }

    protected int getPageRelativeOffset() {
        return Page.PAGE_SIZE - ((this.getIndexInPage() + 1) * header.getRecordSize());
    }

    public ICell getPrimaryKeyCell() {
        return this.cells.get(this.header.getPrimaryKeyColumn().getIndex());
    }

    public List<ICell> getCells() {
        return new ArrayList<>(this.cells);
    }

    protected ICell getCellWithName(String name) {
        return this.cells.stream().filter(c -> c.getColumn().getName().equals(name)).findFirst().orElse(null);
    }

    public ICell getCellByIndex(int index) {
        return this.cells.get(index);
    }

    protected void validate() throws InvalidValueException {
        for (ICell cell : this.cells) { cell.validate(); }
    }

    public boolean meetsCriteria(Criteria criteria) throws InvalidValueException {
        for (ICell cell : this.cells) {
            if(!criteria.validateCell(cell)) return false;
        }

        return true;
    }

    protected void write(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        for (ICell cell: this.cells) { cell.write(raf); }
    }

    protected void read(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        for (ICell cell: this.cells) { cell.read(raf); }
    }

    public String toString() {
        return new ArrayList<>(this.cells.stream().map(ICell::getValue).toList()).toString();
    }
}
