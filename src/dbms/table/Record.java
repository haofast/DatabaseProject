package dbms.table;

import dbms.exceptions.InvalidValueException;
import dbms.table.cell.CellFactory;
import dbms.table.cell.ICell;
import dbms.builders.RecordBuilder;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Record {

    private final Page page;
    private final Header header;
    private final List<ICell> cells;
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

    private int getIndexInPage() {
        return this.page.getRecords().indexOf(this);
    }

    public int getAbsoluteOffset() {
        return this.page.getAbsoluteOffset() + this.getPageRelativeOffset();
    }

    protected int getPageRelativeOffset() {
        return Page.PAGE_SIZE - ((this.getIndexInPage() + 1) * header.getRecordSize());
    }

    protected ICell getPrimaryKeyCell() {
        return this.cells.get(this.header.getPrimaryKeyColumn().getIndex());
    }

    protected List<ICell> getCells() {
        return new ArrayList<>(this.cells);
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
