package dbms.table.cell;

import dbms.exceptions.InvalidValueException;
import dbms.table.Column;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public interface ICell {

    public ICell getDataSource();

    public default Column getColumn() {
        return this.getDataSource().getColumn();
    }

    public default String getValue() {
        return this.getDataSource().getValue();
    }

    public default void setValue(String newValue) throws InvalidValueException {
        this.getDataSource().setValue(newValue);
    }

    public default void validate() throws InvalidValueException {
        this.getDataSource().validate();
    }

    public default void write(ExtendedRaf raf) throws IOException {
        this.getDataSource().write(raf);
    }

    public default void read(ExtendedRaf raf) throws IOException {
        this.getDataSource().read(raf);
    }
}
