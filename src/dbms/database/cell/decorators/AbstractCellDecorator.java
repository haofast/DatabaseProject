package dbms.database.cell.decorators;

import dbms.database.cell.ICell;

public abstract class AbstractCellDecorator implements ICell {

    protected final ICell dataSource;

    public AbstractCellDecorator(ICell dataSource) {
        this.dataSource = dataSource;
    }

    public ICell getDataSource() {
        return this.dataSource;
    }
}
