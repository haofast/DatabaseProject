package dbms.database.cell;

import dbms.exceptions.InvalidValueException;
import dbms.database.table.Column;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public interface ICell extends Comparable<ICell> {

    public ICell getDataSource();

    public default int getDataTypeCode() {
        return this.getDataSource().getDataTypeCode();
    }

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

    public default void throwInvalidValueException(String message) {
        this.getDataSource().throwInvalidValueException(message);
    }

    public default void write(ExtendedRaf raf) throws IOException {
        this.getDataSource().write(raf);
    }

    public default void read(ExtendedRaf raf) throws IOException {
        this.getDataSource().read(raf);
    }

    @Override
    public default int compareTo(ICell o) {
        return this.getDataSource().compareTo(o);
    }
}
