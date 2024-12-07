package dbms.internalSchema;

import dbms.constants.ColumnFlag;
import dbms.datatypes.IntegerType;
import dbms.datatypes.ShortType;
import dbms.datatypes.StringType;
import dbms.table.Column;
import dbms.table.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KyptonTables {
    static Table kyptonTablesTable;
    static List kyptonTableData = List.of(new String[]{"kypton_tables", "1", "32", "1"});
    static List kyptonColumnData = List.of(new String[]{"kypton_columns", "2", "41", "1"});

    public void KyptonTable() throws IOException {
        initializeKyptonTables();
    }

    public static void initializeKyptonTables() throws IOException {
        if (!isInternalKyptonTablesInitiaized()) {
            createKyptonTables();
        } else{
            kyptonTablesTable.read("kypton_tables.tbl");
        }
    }

    public static boolean isInternalKyptonTablesInitiaized() {
        //if the internal schema files have been found return true
        return false;
    }

    public static void createKyptonTables() throws IOException {
        Column.Builder[] columns = {
                new Column.Builder("rowid", new IntegerType()).addExtension(ColumnFlag.PRIMARY_KEY),
                new Column.Builder("table_name", new StringType(20)),
                new Column.Builder("record_count", new IntegerType()),
                new Column.Builder("avg_length", new ShortType()),
                new Column.Builder("root_page", new ShortType()),
        };

        kyptonTablesTable = new Table(columns);
        kyptonTablesTable.write("kypton_tables.tbl");

        try {
            kyptonTablesTable.addRecord(kyptonTableData);
            kyptonTablesTable.addRecord(kyptonColumnData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesTableExist(String tableName) {
        return true;
    }

    public Table getKyptonTable() {
        return kyptonTablesTable;
    }
}
