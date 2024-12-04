package dbms.internalSchema;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.Column;
import dbms.table.Table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KyptonTables {
    Table kyptonTablesTable;
    List kyptonTableData = List.of(new String[]{"1", "kypton_tables", "1", "32", "1"});
    List kyptonColumnData = List.of(new String[]{"1", "kypton_columns", "2", "41", "1"});

    public void KyptonTable() throws IOException {
        initializeKyptonTables();
    }

    public void initializeKyptonTables() throws IOException {
        if (!isInternalKyptonTablesInitiaized()) {
            createKyptonTables();
        }
    }

    public boolean isInternalKyptonTablesInitiaized() {
        //if the internal schema files have been found return true
        return false;
    }

    public void createKyptonTables() throws IOException {
        Column.Builder[] columns = {
                new Column.Builder("rowid", 4, DataType.INTEGER).addExtension(ColumnFlag.PRIMARY_KEY),
                new Column.Builder("table_name", 20, DataType.STRING),
                new Column.Builder("record_count", 2, DataType.INTEGER),
                new Column.Builder("avg_length", 2, DataType.SHORT),
                new Column.Builder("root_page", 2, DataType.SHORT),
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

    public Table getKyptonTable() {
        return kyptonTablesTable;
    }
}
