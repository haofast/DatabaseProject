package dbms.table.cell.decorators;

import dbms.exceptions.InvalidValueException;
import dbms.table.cell.ICell;

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
            throw new InvalidValueException("Value cannot be null", this.getValue(), this.getColumn());
        }
        getDataSource().validate();
    }
}
