package dbms.database.internalSchema;

import dbms.database.table.Column;
import dbms.database.table.Table;

import java.io.IOException;
import java.util.Map;

public class InternalSchema {

    public static final InternalSchema globalInstance = new InternalSchema();

    public InternalSchema() {
        try {
            KryptonTables.initKryptonTablesTable();
            KryptonColumns.initKryptonColumnsTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* procedure for creating a table and recording changes to disk */
    public Table createTable(String tableName, Column.Builder[] columnBuilders) throws Exception {
        // create table in memory
        Table table = new Table(tableName, columnBuilders);

        // add table to meta-data table and write to disk
        KryptonTables.addTable(table);
        KryptonColumns.write();
        KryptonTables.write();

        // write the table data to disk
        table.write();

        return table;
    }

    public void dropTable(String tableName) throws Exception {
        KryptonTables.dropTable(tableName);
        KryptonTables.write();
    }

    /* procedure for saving a table to disk */
    public void saveTable(Table table) throws IOException {
        // update and write meta-data for table to disk
        KryptonTables.updateTableRecordCount(table);
        KryptonTables.write();

        // write table data to disk
        table.write();
    }

    /* returns all table instances */
    public Map<String, Table> getTables() throws IOException {
        return KryptonTables.getTables();
    }

    /* returns the table matching the table name */
    public Table getTable(String tableName) throws IOException {
        return KryptonTables.getTable(tableName);
    }

    public String toString() {
        return new StringBuilder()
            .append(KryptonTables.getKryptonTablesTable().toString()).append("\n")
            .append(KryptonColumns.getKryptonColumnsTable().toString()).append("\n")
            .toString();
    }
}
