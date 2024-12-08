package dbms.database.cell.decorators;

import dbms.exceptions.InvalidValueException;
import dbms.database.cell.ICell;

public class NotNullCellDecorator extends AbstractCellDecorator {

    public NotNullCellDecorator(ICell dataSource) {
        super(dataSource);
    }

    @Override
    public void setValue(String newValue) throws InvalidValueException {
        if (newValue == null) {
            throw new InvalidValueException("Unable to set value, Value cannot be null.", null, this.getColumn());
        }
        getDataSource().setValue(newValue);
    }

    @Override
    public void validate() throws InvalidValueException {
        if (this.getValue() == null) {
            throwInvalidValueException("Value cannot be null");
        }
        getDataSource().validate();
    }
}
