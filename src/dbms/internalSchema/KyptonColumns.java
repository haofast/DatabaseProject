package dbms.internalSchema;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.Column;
import dbms.table.Table;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class KyptonColumns {
    Table kyptonColumnTable;

    public KyptonColumns() {
        initializeKyptonColumns();
    }

    public void initializeKyptonColumns() {
        if (!isInternalKyptonColumnsInitiaized()) {
            try {
                createKyptonColumns();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isInternalKyptonColumnsInitiaized() {
        //if the internal schema files have been found return true
        return false;
    }

    public void createKyptonColumns() throws IOException {
        Column.Builder[] columns = {
                new Column.Builder("rowid", 4, DataType.INTEGER).addExtension(ColumnFlag.PRIMARY_KEY),
                new Column.Builder("column_name", 20, DataType.STRING),
                new Column.Builder("table_rowid", 4, DataType.INTEGER),
                new Column.Builder("data_type", 9, DataType.STRING),
                new Column.Builder("ordinal_position", 2, DataType.SHORT),
                new Column.Builder("is_nullable", 2, DataType.SHORT),
        };

        kyptonColumnTable = new Table(columns);
        populateKyptonColumns(kyptonColumnTable);
        kyptonColumnTable.write("kypton_columns.tbl");
    }

    public void populateKyptonColumns(Table kyptonColumns) {
        try {
            kyptonColumns.addRecord(List.of(new String[]{"1", "rowid",              "1", "INTEGER" , "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"2", "table_name",         "1", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"3", "record_count",       "1", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"4", "avg_length",         "1", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"5", "root_page",          "1", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"6", "rowid",              "2", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"7", "column_name",        "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"8", "table_rowid",        "2", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"9", "table_name",         "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"10", "data_type",         "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"11", "ordinal_position",  "2", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"12", "is_nullable",       "2", "SHORT", "0", "0"}));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Table getKyptonColumnTable() {
        return kyptonColumnTable;
    }
}
