package dbms.database.cell.subclasses;

import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.cell.AbstractCell;
import dbms.database.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class LongCell extends AbstractCell {

    public LongCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public long getLongValue() {
        return Long.parseLong(this.value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeLong(this.getLongValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readLong());
    }

    @Override
    public void validate() {
        try {
            Long.parseLong(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not a long");
        }
    }

    @Override
    public int compareTo(ICell o) {
        LongCell cell = (LongCell) o.getDataSource();
        return Long.compare(this.getLongValue(), cell.getLongValue());
    }
}
