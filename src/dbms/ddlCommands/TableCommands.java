package dbms.ddlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class TableCommands {
    private final List<Table> tables;

    public TableCommands() {
        this.tables = new ArrayList<Table>();
    }

    // Will add code to show all tables (returned blank Table list as placeholder)
    public List<Table> showTables() {
        return new ArrayList<Table>();
    }

    // Will add code to create a table (returned blank Table as placeholder)
    public Table createTable(String columns) {
        Column.Builder[] columnBuilders = {};
        return new Table(columnBuilders);
    }

    // Will add code to drop a table (returned blank Table as a placeholder)
    public Table dropTable(String tableName) {
        Column.Builder[] columnBuilders = {};
        return new Table(columnBuilders);
    }
}
