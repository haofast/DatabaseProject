package dbms.database.cell.subclasses;

import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.cell.AbstractCell;
import dbms.database.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class ShortCell extends AbstractCell {

    public ShortCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public short getShortValue() {
        return Short.parseShort(this.value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeShort(this.getShortValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readShort());
    }

    @Override
    public void validate() {
        try {
            Short.parseShort(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not a short");
        }
    }

    @Override
    public int compareTo(ICell o) {
        ShortCell cell = (ShortCell) o.getDataSource();
        return Short.compare(this.getShortValue(), cell.getShortValue());
    }
}
