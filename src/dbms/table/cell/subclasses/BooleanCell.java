package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class BooleanCell extends AbstractCell {
    private final String TRUE = "true";
    private final String FALSE = "false";

    public BooleanCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeByte(this.value.equals(TRUE) ? 1 : 0);
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = (raf.readByte() == 0 ? FALSE : TRUE);
    }

    @Override
    public void validate() {
        if (!this.value.equals(TRUE) && !this.value.equals(FALSE)) {
            throwInvalidValueException("Value must either be \"true\" or \"false\"");
        }
    }
}
