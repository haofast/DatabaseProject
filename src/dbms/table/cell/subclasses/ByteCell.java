package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class ByteCell extends AbstractCell {

    public ByteCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public byte getByteValue() {
        return Byte.parseByte(this.value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeByte(this.getByteValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readByte());
    }

    @Override
    public void validate() {
        try {
            Byte.parseByte(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not a byte");
        }
    }

    @Override
    public int compareTo(ICell o) {
        ByteCell cell = (ByteCell) o.getDataSource();
        return Byte.compare(this.getByteValue(), cell.getByteValue());
    }
}
