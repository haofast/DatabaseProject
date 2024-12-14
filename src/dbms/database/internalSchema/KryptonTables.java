package dbms.database.internalSchema;

import dbms.database.datatypes.*;
import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.table.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KryptonTables {

    private static final String TABLE_NAME   = "table_name";
    private static final String RECORD_COUNT = "record_count";

    private static String TABLES_TABLE_NAME = "krypton_tables.tbl";
    private static Table kryptonTablesTable;

    public static void initKryptonTablesTable() throws IOException {
        kryptonTablesTable = initializeTableInMemory();
        if (tableFileExistsOnDisk()) {
            kryptonTablesTable.read();
        } else {
            kryptonTablesTable.write();
        };
    }

    protected static Table getKryptonTablesTable() {
        return kryptonTablesTable;
    }

    protected static void write() throws IOException {
        kryptonTablesTable.write();
    }

    protected static Map<String, Table> getTables() throws IOException {
        Map<String, Table> tableMap = new TreeMap<>();

        for (Record tableRecord : kryptonTablesTable.getRecords()) {
            int tableRowID = tableRecord.getRowIDValue();
            String tableName = tableRecord.getValueWithColumnName(TABLE_NAME);
            int lastUsedRowID = Integer.parseInt(tableRecord.getValueWithColumnName(RECORD_COUNT));
            Table table = new Table(tableName, KryptonColumns.getColumnBuildersForTable(tableRowID), lastUsedRowID);
            tableMap.put(tableName, table);
            table.read();
        }

        return tableMap;
    }

    protected static Table getTable(String tableName) throws IOException {
        Record tableRecord = kryptonTablesTable.searchRecordsByValueUndeleted(TABLE_NAME, tableName).getFirst();
        int tableRowID = tableRecord.getRowIDValue();
        int lastUsedRowID = Integer.parseInt(tableRecord.getValueWithColumnName(RECORD_COUNT));
        Table table = new Table(tableName, KryptonColumns.getColumnBuildersForTable(tableRowID), lastUsedRowID);
        table.read();
        return table;
    }

    protected static void addTable(Table table) throws Exception {
        // can't create a table whose name is already taken
        if (!kryptonTablesTable.searchRecordsByValueUndeleted(TABLE_NAME, table.getName()).isEmpty()) {
            throw new Exception("ERROR: Unable to create table with name " + table.getName() + " because name is already taken. Operation aborted.");
        }

        String rowCountString = String.valueOf(table.getLastUsedRowID());
        kryptonTablesTable.addRecord(new ArrayList<>(List.of(new String[]{table.getName(), rowCountString})));
        int tableRowID = kryptonTablesTable.searchRecordsByValueUndeleted(TABLE_NAME, table.getName()).getFirst().getRowIDValue();
        KryptonColumns.addColumns(tableRowID, table);
    }

    protected static void dropTable(String tableName) throws Exception {
        Record tableRecord = kryptonTablesTable.searchRecordsByValueUndeleted(TABLE_NAME, tableName).getFirst();
        tableRecord.setDeleted();
    }

    protected static void updateTableRecordCount(Table table) {
        Record tableRecord = kryptonTablesTable.searchRecordsByValueUndeleted(TABLE_NAME, table.getName()).getFirst();
        tableRecord.getCellWithName(RECORD_COUNT).setValue(String.valueOf(table.getLastUsedRowID()));
    }

    private static Table initializeTableInMemory() {
        return new Table(TABLES_TABLE_NAME, new Column.Builder[]{
            new Column.Builder(TABLE_NAME,   new StringType(20)), //doc requirements plus .tbl extension
            new Column.Builder(RECORD_COUNT, new IntegerType())
        });
    }

    private static boolean tableFileExistsOnDisk() {
        return new File(TABLES_TABLE_NAME).exists();
    }
}
