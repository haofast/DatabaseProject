package dbms.table;

import dbms.constants.ColumnFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Header {

    private final List<Column> columns;

    protected Header(Column.Builder[] columnBuilders) {
        this.columns = Arrays.stream(columnBuilders).map(cb -> cb.build(this)).toList();
    }

    protected List<Column> getColumns() {
        return new ArrayList<>(this.columns);
    }

    protected Column getPrimaryKeyColumn() {
        return this.columns.stream().filter(c -> c.hasFlag(ColumnFlag.PRIMARY_KEY)).findFirst().orElse(null);
    }

    protected int getRecordSize() {
        return this.columns.stream().mapToInt(Column::getSize).sum() + 1;
    }

    public String toString() {
        List<String> columnNames = new ArrayList<>(this.columns.stream().map(Column::getName).toList());
        columnNames.add("Deletion Marker");
        return columnNames.toString();
    }
}
