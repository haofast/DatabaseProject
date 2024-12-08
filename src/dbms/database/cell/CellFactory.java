package dbms.database.cell;

import dbms.database.constants.ColumnFlag;
import dbms.database.constants.DataTypeCode;
import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.cell.decorators.NotNullCellDecorator;
import dbms.database.cell.decorators.PrimaryKeyCellDecorator;
import dbms.database.cell.decorators.UniqueCellDecorator;
import dbms.database.cell.subclasses.*;

public class CellFactory {

    public static ICell build(Record record, Column column, String value) {
        ICell cell = switch(column.getType()) {
            case DataTypeCode.NULL     -> new NullCell(record, column, value);
            case DataTypeCode.BYTE     -> new ByteCell(record, column, value);
            case DataTypeCode.SHORT    -> new ShortCell(record, column, value);
            case DataTypeCode.INTEGER  -> new IntegerCell(record, column, value);
            case DataTypeCode.LONG     -> new LongCell(record, column, value);
            case DataTypeCode.FLOAT    -> new FloatCell(record, column, value);
            case DataTypeCode.DOUBLE   -> new DoubleCell(record, column, value);
            case DataTypeCode.YEAR     -> new YearCell(record, column, value);
            case DataTypeCode.TIME     -> new TimeCell(record, column, value);
            case DataTypeCode.DATETIME -> new DateTimeCell(record, column, value);
            case DataTypeCode.DATE     -> new DateCell(record, column, value);
            case DataTypeCode.STRING   -> new StringCell(record, column, value);
            case DataTypeCode.BOOLEAN  -> new BooleanCell(record, column, value);
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
