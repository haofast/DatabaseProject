package dbms.table.cell;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.Column;
import dbms.table.Record;
import dbms.table.cell.decorators.NotNullCellDecorator;
import dbms.table.cell.decorators.PrimaryKeyCellDecorator;
import dbms.table.cell.decorators.UniqueCellDecorator;
import dbms.table.cell.subclasses.IntegerCell;
import dbms.table.cell.subclasses.ShortCell;
import dbms.table.cell.subclasses.StringCell;

public class CellFactory {
    public static ICell build(Record record, Column column, String value) {
        ICell cell = switch(column.getType()) {
            case DataType.INTEGER -> new IntegerCell(record, column, value);
            case DataType.SHORT -> new ShortCell(record, column, value);
            case DataType.STRING ->  new StringCell(record, column, value);
        };

        if (column.hasFlag(ColumnFlag.PRIMARY_KEY)) {
            cell = new PrimaryKeyCellDecorator(cell);
        }
        if (column.hasFlag(ColumnFlag.UNIQUE)) {
            cell = new UniqueCellDecorator(cell);
        }
        if (column.hasFlag(ColumnFlag.NOT_NULL)) {
            cell = new NotNullCellDecorator(cell);
        }

        return cell;
    }
}
