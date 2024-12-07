package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.Arrays;

public class StringCell extends AbstractCell {

    public StringCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    public int getDataTypeCode() {
        return 12 + this.column.getSize();
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.write(Arrays.copyOf(value.getBytes(), column.getSize()));
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = raf.readString(column.getSize());
    }

    @Override
    public void validate() {
        if (this.value.length() > this.column.getSize()) {
            this.throwInvalidValueException("Value must not exceed length of " + this.column.getSize());
        }
    }

    @Override
    public int compareTo(ICell o) {
        StringCell cell = (StringCell) o.getDataSource();
        return this.value.compareTo(cell.getValue());
    }
}
