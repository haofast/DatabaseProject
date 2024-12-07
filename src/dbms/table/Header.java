package dbms.table;

import dbms.constants.ColumnFlag;
import dbms.datatypes.IntegerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Header {

    public static final String ROW_ID_COLUMN_NAME = "rowid";
    private final List<Column> columns;

    protected Header(Table table, Column.Builder[] columnBuilders) {
        // automatically build reserved row ID column
        Column.Builder rowIDColumnBuilder = new Column.Builder(ROW_ID_COLUMN_NAME, new IntegerType())
                .addExtension(ColumnFlag.SYSTEM_MANAGED);


        // build user-defined columns and filter out columns with reserved names
        this.columns = new ArrayList<>(
            Arrays.stream(columnBuilders).map(cb -> cb.build(table, this))
                .filter(c -> !c.getName().equals(ROW_ID_COLUMN_NAME))
                .toList()
        );

        // ensure that there is no more than 1 user-defined primary key column
        List<Column> primaryKeyColumns = columns.stream().filter(c -> c.hasFlag(ColumnFlag.PRIMARY_KEY)).toList();
        if (primaryKeyColumns.size() > 1) throw new RuntimeException("there can only be one primary key column");

        // assign row ID as primary key if user did not define one
        boolean primaryKeySpecified = primaryKeyColumns.size() == 1;
        if (!primaryKeySpecified) rowIDColumnBuilder.addExtension(ColumnFlag.PRIMARY_KEY);

        // add reserved columns
        this.columns.addFirst(rowIDColumnBuilder.build(table, this));
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

    public int getRecordSize() {
        // record header size is 9, each column type takes 2 bytes
        return 9 + (this.columns.size() * 2)+ this.columns.stream().mapToInt(Column::getSize).sum();
    }

    public String toString() {
        return this.columns.toString();
    }
}
