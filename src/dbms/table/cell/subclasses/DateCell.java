package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;

import java.text.ParseException;

public class DateCell extends DateTimeCell {

    public DateCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    public int getDataTypeCode() {
        return 11;
    }

    @Override
    protected String getDateFormatString() {
        return "yyyy-MM-dd";
    }

    @Override
    public void validate() {
        try {
            this.getDateFormat().parse(this.value);
        } catch (ParseException e) {
            this.throwInvalidValueException("Value must be in the format yyyy-MM-dd");
        }
    }
}
