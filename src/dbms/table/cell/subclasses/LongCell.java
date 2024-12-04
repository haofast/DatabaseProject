package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class LongCell extends AbstractCell {

    public LongCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeLong(Long.parseLong(this.value));
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
}
