package dbms.table.cell.subclasses;

import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.AbstractCell;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeCell extends AbstractCell {

    public DateTimeCell(Record record, Column column, String value) {
        super(record, column, value);
    }

    protected DateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        try {
            Date date = this.getDateFormat().parse(this.value);
            long epochTimeInSeconds = date.getTime() / 1000;
            raf.writeLong(epochTimeInSeconds);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void performRead(ExtendedRaf raf) throws IOException {
        long epochTimeInSeconds = raf.readLong();
        Date date = new Date(epochTimeInSeconds * 1000);
        this.value = getDateFormat().format(date);
    }

    @Override
    public void validate() {
        try {
            this.getDateFormat().parse(this.value);
        } catch (ParseException e) {
            this.throwInvalidValueException("Value must be in the format yyyy-MM-dd_HH:mm:ss");
        }
    }
}
