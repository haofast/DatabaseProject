package dbms.internalSchema;

import dbms.constants.ColumnFlag;
import dbms.datatypes.IntegerType;
import dbms.datatypes.ShortType;
import dbms.datatypes.StringType;
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
                new Column.Builder("rowid", new IntegerType()).addExtension(ColumnFlag.PRIMARY_KEY),
                new Column.Builder("column_name", new StringType(20)),
                new Column.Builder("table_rowid", new IntegerType()),
                new Column.Builder("data_type", new StringType(9)),
                new Column.Builder("ordinal_position", new ShortType()),
                new Column.Builder("is_nullable", new ShortType()),
        };

        kyptonColumnTable = new Table(columns);
        populateKyptonColumns(kyptonColumnTable);
        kyptonColumnTable.write("kypton_columns.tbl");
    }

    public void populateKyptonColumns(Table kyptonColumns) {
        try {
            kyptonColumns.addRecord(List.of(new String[]{"rowid",              "1", "INTEGER" , "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"table_name",         "1", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"record_count",       "1", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"avg_length",         "1", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"root_page",          "1", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"rowid",              "2", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"column_name",        "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"table_rowid",        "2", "INTEGER", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"table_name",         "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"data_type",         "2", "STRING", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"ordinal_position",  "2", "SHORT", "0", "0"}));
            kyptonColumns.addRecord(List.of(new String[]{"is_nullable",       "2", "SHORT", "0", "0"}));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Table getKyptonColumnTable() {
        return kyptonColumnTable;
    }
}
