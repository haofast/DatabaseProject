package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;

public class TimeCell extends IntegerCell {

    private static int MIN_VALUE = 0;
    private static int MAX_VALUE = 86400000;

    public TimeCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    public void validate() {
        super.validate();
        int integerValue = Integer.parseInt(this.value);
        if (integerValue < MIN_VALUE || integerValue > MAX_VALUE) {
            this.throwInvalidValueException("Value must be between " + MIN_VALUE + "-" + MAX_VALUE);
        }
    }
}
