package dbms.table.cell;

import dbms.exceptions.InvalidValueException;
import dbms.table.Column;
import dbms.table.Record;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public abstract class AbstractCell implements ICell {

    protected final Record record;
    protected final Column column;
    protected String value;

    public AbstractCell(Record record, Column column, String value) {
        this.record = record;
        this.column = column;
        this.value = value;
    }

    public ICell getDataSource() {
        return this;
    }

    private int getAbsoluteOffset() {
        return this.record.getAbsoluteOffset() + this.column.getRelativeRecordOffset();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Column getColumn() {
        return this.column;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void validate() {
        /* to be implemented in decorators */
    }

    @Override
    public void write(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        this.performWrite(raf);
    }

    @Override
    public void read(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        this.performRead(raf);
    }

    @Override
    public void throwInvalidValueException(String message) {
        throw new InvalidValueException(message, this.value, this.column);
    }

    protected abstract void performWrite(ExtendedRaf raf) throws IOException;

    protected abstract void performRead(ExtendedRaf raf) throws IOException;
}
