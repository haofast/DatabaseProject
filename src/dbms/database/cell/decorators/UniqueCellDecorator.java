package dbms.database.cell.decorators;

import dbms.exceptions.InvalidValueException;
import dbms.database.cell.ICell;

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
        if (cellExistsWithSameValue(this.getValue())) {
            this.throwInvalidValueException(this.getDuplicateKeyOnSetMessage());
        }
        getDataSource().setValue(newValue);
    }

    @Override
    public void validate() throws InvalidValueException {
        if (cellExistsWithSameValue(this.getValue())) {
            this.throwInvalidValueException(this.getDuplicateKeyOnVerifyMessage());
        }
        getDataSource().validate();
    }

    private boolean cellExistsWithSameValue(String value) {
        return this.getColumn().getCells().stream().anyMatch(c -> (
            c.getDataSource() != this.getDataSource()
            && c.getValue().equals(value)
        ));
    }
}
