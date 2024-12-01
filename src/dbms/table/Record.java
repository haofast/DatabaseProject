package dbms.table;

import dbms.exceptions.InvalidValueException;
import dbms.table.cell.CellFactory;
import dbms.table.cell.ICell;
import dbms.builders.RecordBuilder;
import dbms.utilities.ExtendedRaf;
import interfaces.IBundleable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Record implements IBundleable<Record> {

    private Page page;
    private Header header;
    private List<ICell> cells;
    private boolean markedForDeletion;

    public Record(Page page, RecordBuilder builder) {
        this.page = page;
        this.header = builder.getHeader();
        this.cells = initializeCells(builder.getValues());
    }

    private List<ICell> initializeCells(List<String> values) {
        List<Column> columns = this.header.getColumns();
        return IntStream.range(0, columns.size()).mapToObj(i -> CellFactory.build(this, columns.get(i), values.get(i))).toList();
    }

    public Record getObjectCopy() {
        List<String> cellValues = this.cells.stream().map(ICell::getValue).toList();
        return new Record(this.page, new RecordBuilder(this.header, cellValues));
    }

    public void setObjectState(Record object) {
        this.cells = object.getCells();
    }

    public void setValues(Map<String, String> values) {
       this.mutateInBundle(
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

    protected List<ICell> getCells() {
        return new ArrayList<>(this.cells);
    }

    protected ICell getCellWithName(String name) {
        return this.cells.stream().filter(c -> c.getColumn().getName().equals(name)).findFirst().orElse(null);
    }

    protected void validate() throws InvalidValueException {
        for (ICell cell : this.cells) { cell.validate(); }
    }

    protected void write(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        for (ICell cell: this.cells) { cell.write(raf); }
        raf.writeByte(this.markedForDeletion ? 1 : 0);
    }

    protected void read(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        for (ICell cell: this.cells) { cell.read(raf); }
        this.markedForDeletion = raf.readByte() != 0;
    }

    public String toString() {
        List<String> values = new ArrayList<>(this.cells.stream().map(ICell::getValue).toList());
        values.add(this.markedForDeletion ? "deleted" : "not deleted");
        return values.toString();
    }
}
