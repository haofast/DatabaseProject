package dbms.database.cell.subclasses;

import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.cell.AbstractCell;
import dbms.database.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class BooleanCell extends AbstractCell {

    private final String TRUE = "true";
    private final String FALSE = "false";

    public BooleanCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public boolean getBooleanValue() {
        return this.value.equals(TRUE);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeByte(getBooleanValue() ? 1 : 0);
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

    @Override
    public int compareTo(ICell o) {
        BooleanCell cell = (BooleanCell) o.getDataSource();
        return Boolean.compare(this.getBooleanValue(), cell.getBooleanValue());
    }
}
