package dbms.database.cell.subclasses;

import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.cell.AbstractCell;
import dbms.database.cell.ICell;
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

    public Date getDateValue() {
        try {
            return this.getDateFormat().parse(this.value);
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getDateFormatString() {
        return "yyyy-MM-dd_HH:mm:ss";
    }

    protected DateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.getDateFormatString());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }

    @Override
    public int getDataTypeCode() {
        return 10;
    }

    @Override
    protected void performWrite(ExtendedRaf raf) throws IOException {
        Date date = this.getDateValue();
        long epochTimeInSeconds = date.getTime() / 1000;
        raf.writeLong(epochTimeInSeconds);
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

    @Override
    public int compareTo(ICell o) {
        DateTimeCell cell = (DateTimeCell) o.getDataSource();
        return Long.compare(this.getDateValue().getTime(), cell.getDateValue().getTime());
    }
}
