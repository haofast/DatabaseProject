package dbms.table.cell.decorators;

import dbms.table.cell.ICell;

public class PrimaryKeyCellDecorator extends UniqueCellDecorator {

    public PrimaryKeyCellDecorator(ICell dataSource) {
        super(dataSource);
    }

    @Override
    protected String getDuplicateKeyOnSetMessage() {
        return "Unable to set key value. Key value already exists in column.";
    }

    @Override
    protected String getDuplicateKeyOnVerifyMessage() {
        return "Duplicate key value found in column.";
    }
}
