package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.lang.String;

public class IntegerCell extends AbstractCell {

    public IntegerCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeInt(Integer.parseInt(this.getValue()));
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readInt());
    }
}
