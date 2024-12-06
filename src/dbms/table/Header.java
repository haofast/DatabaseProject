package dbms.table;

import dbms.constants.ColumnFlag;
import dbms.datatypes.BooleanType;
import dbms.datatypes.IntegerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Header {

    public static final String ROW_ID_COLUMN_NAME = "rowid";
    public static final String DELETED_COLUMN_NAME = "deleted";
    private final List<Column> columns;

    protected Header(Table table, Column.Builder[] columnBuilders) {
        // automatically build reserved row ID column
        Column.Builder rowIDColumnBuilder = new Column.Builder(ROW_ID_COLUMN_NAME, new IntegerType())
                .addExtension(ColumnFlag.SYSTEM_MANAGED);

        // automatically build reserved deletion marker column
        Column.Builder deletedColumnBuilder = new Column.Builder(DELETED_COLUMN_NAME, new BooleanType())
                .addExtension(ColumnFlag.SYSTEM_MANAGED);

        // build user-defined columns and filter out columns with reserved names
        this.columns = new ArrayList<>(
            Arrays.stream(columnBuilders).map(cb -> cb.build(table, this))
                .filter(c -> !c.getName().equals(ROW_ID_COLUMN_NAME))
                .filter(c -> !c.getName().equals(DELETED_COLUMN_NAME))
                .toList()
        );

        // assign row ID as primary key if user did not define one
        boolean primaryKeySpecified = columns.stream().anyMatch(c -> c.hasFlag(ColumnFlag.PRIMARY_KEY));
        if (!primaryKeySpecified) rowIDColumnBuilder.addExtension(ColumnFlag.PRIMARY_KEY);

        // add reserved columns
        this.columns.addFirst(rowIDColumnBuilder.build(table, this));
        this.columns.addLast(deletedColumnBuilder.build(table, this));
    }

    public List<Column> getColumns() {
        return new ArrayList<>(this.columns);
    }

    public Column getColumnByName(String columnName) {
        return this.columns.stream().filter(c -> c.getName().equals(columnName)).findFirst().orElse(null);
    }

    public Column getPrimaryKeyColumn() {
        return this.columns.stream().filter(c -> c.hasFlag(ColumnFlag.PRIMARY_KEY)).findFirst().orElse(null);
    }

    protected int getRecordSize() {
        return this.columns.stream().mapToInt(Column::getSize).sum();
    }

    public String toString() {
        return this.columns.toString();
    }
}
