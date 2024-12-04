package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class DoubleCell extends AbstractCell {

    public DoubleCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeDouble(Double.parseDouble(this.value));
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
}
