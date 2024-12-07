package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class DoubleCell extends AbstractCell {

    public DoubleCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public double getDoubleValue() {
        return Double.parseDouble(this.value);
    }

    @Override
    public int getDataTypeCode() {
        return 6;
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeDouble(this.getDoubleValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readDouble());
    }

    @Override
    public void validate() {
        try {
            Double.parseDouble(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not a double");
        }
    }

    @Override
    public int compareTo(ICell o) {
        DoubleCell cell = (DoubleCell) o.getDataSource();
        return Double.compare(this.getDoubleValue(), cell.getDoubleValue());
    }
}
