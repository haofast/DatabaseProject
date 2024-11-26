package dbms.table.cell.decorators;

import dbms.exceptions.InvalidValueException;
import dbms.table.cell.ICell;

import java.util.List;

public class UniqueCellDecorator extends AbstractCellDecorator {

    public UniqueCellDecorator(ICell dataSource) {
        super(dataSource);
    }

    protected String getDuplicateKeyOnSetMessage() {
        return "Unable to set value. Value already exists in column.";
    }

    protected String getDuplicateKeyOnVerifyMessage() {
        return "Duplicate value found in column.";
    }

    @Override
    public void setValue(String newValue) throws InvalidValueException {
        validateDuplicateValue(newValue);
        getDataSource().setValue(newValue);
    }

    @Override
    public void validate() throws InvalidValueException {
        validateDuplicateValue(this.getValue());
        getDataSource().validate();
    }

    private void validateDuplicateValue(String value) throws InvalidValueException {
        if (cellExistsWithSameValue(value)) {
            throw new InvalidValueException(this.getDuplicateKeyOnVerifyMessage(), this.getValue(), this.getColumn());
        }
    }

    private boolean cellExistsWithSameValue(String value) {
        return this.getColumn().getCells().stream().anyMatch(c -> (
            c.getDataSource() != this.getDataSource()
            && c.getValue().equals(value)
        ));
    }
}
