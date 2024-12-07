package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class IntegerCell extends AbstractCell {

    public IntegerCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    public int getIntegerValue() {
        return Integer.parseInt(this.value);
    }

    @Override
    public int getDataTypeCode() {
        return 3;
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeInt(this.getIntegerValue());
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readInt());
    }

    @Override
    public void validate() {
        try {
            Integer.parseInt(this.value);
        } catch (NumberFormatException e) {
            this.throwInvalidValueException("Value is not an integer");
        }
    }

    @Override
    public int compareTo(ICell o) {
        IntegerCell cell = (IntegerCell) o.getDataSource();
        return Integer.compare(this.getIntegerValue(), cell.getIntegerValue());
    }
}
