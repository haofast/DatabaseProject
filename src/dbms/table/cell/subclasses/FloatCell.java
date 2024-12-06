package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class FloatCell extends AbstractCell {

    public FloatCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public float getFloatValue() {
        return Float.parseFloat(this.value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeFloat(this.getFloatValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readFloat());
    }

    @Override
    public void validate() {
        try {
            Float.parseFloat(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not a float");
        }
    }

    @Override
    public int compareTo(ICell o) {
        FloatCell cell = (FloatCell) o.getDataSource();
        return Float.compare(this.getFloatValue(), cell.getFloatValue());
    }
}
