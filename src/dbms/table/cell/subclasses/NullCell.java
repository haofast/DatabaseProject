package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class NullCell extends AbstractCell {
    public NullCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        /* do nothing */
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        /* do nothing */
    }

    @Override
    public void validate() {
        if (this.value != null && !this.value.isEmpty()) {
            this.throwInvalidValueException("Null type cannot have a non-empty value");
        }
    }
}
