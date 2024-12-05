package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateCell extends DateTimeCell {

    public DateCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    @Override
    protected DateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
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
