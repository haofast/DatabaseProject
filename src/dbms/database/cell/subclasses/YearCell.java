package dbms.database.cell.subclasses;

import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class YearCell extends IntegerCell  {

    private static final int EPOCH_YEAR = 2000;
    private static final int MAX_YEAR = EPOCH_YEAR + 255;
    private static final int OFFSET = EPOCH_YEAR + 128;

    public YearCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        raf.writeByte(Byte.parseByte(this.value) - OFFSET);
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        this.value = String.valueOf(raf.readByte() + OFFSET);
    }

    @Override
    public void validate() {
        super.validate();
        int integerValue = this.getIntegerValue();
        if (integerValue < EPOCH_YEAR || integerValue > (MAX_YEAR)) {
            this.throwInvalidValueException("Value must be between " + EPOCH_YEAR + "-" + MAX_YEAR);
        }
    }
}
