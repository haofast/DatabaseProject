package dbms.userInterface.ddlCommands;

import dbms.database.datatypes.*;
import dbms.database.internalSchema.InternalSchema;
import dbms.database.internalSchema.KryptonTables;
import dbms.database.constants.ColumnFlag;
import dbms.database.table.Column;
import dbms.database.table.Table;

import java.io.IOException;
import java.util.*;

import static dbms.database.internalSchema.InternalSchema.globalInstance;

public class TableCommands {
    // Class variables
    private String query;
    private String tableName;
    private List<String> columnInfoList;
    private List<Table> tables;

    // Constructor
    public TableCommands(String query) throws Exception {
        // Initiate
        this.query = query.trim();
        this.tableName = "";
        this.columnInfoList = new ArrayList<String>();
        this.tables = new ArrayList<Table>();

        // Perform functions
        this.trimSemicolon();
        this.handleQuery();
    }

    // Trim semicolon
    private void trimSemicolon() {
        System.out.println("\nQuery: " + this.query);
        if (!this.query.isEmpty() && this.query.endsWith(";"))
            // Use substring to get all characters except the last one
            this.query = this.query.substring(0, this.query.length() - 1);
    }

    // Handle the query entered by user
    private void handleQuery() throws Exception {
        // Split query and get first character for action
        String[] querySplit = this.query.trim().split("\\s+");
        String action = querySplit[0];

        // Show tables
        if (action.equalsIgnoreCase("SHOW")) {
            if (querySplit.length == 2)
                this.showTables();
            else
                System.out.println("\nQuery is invalid!");
        }

        // Create a table
        else if (action.equalsIgnoreCase("CREATE")) {
            if (querySplit.length == 3) {
                this.tableName = querySplit[2];
                this.createTable(tableName + ".tbl");
            } else if (querySplit.length > 3) {
                this.tableName = querySplit[2];

                // Get list of columns (and info) to put in table (split on open parentheses)
                String[] columnsInfoInQuery = this.query.trim().split("\\(");
                String columnsInfoStr = columnsInfoInQuery[1];

                // Remove closed parentheses if exists
                if (columnsInfoStr.endsWith(")"))
                    columnsInfoStr = columnsInfoStr.substring(0, columnsInfoStr.length() - 1);

                // Get list of values (and info) to be inserted
                String[] columnsInfo = columnsInfoStr.split(",");
                for (String c : columnsInfo) {
                    c = c.trim();
                    this.columnInfoList.add(c);
                }

                if (columnInfoList.isEmpty()) {
                    this.createTable(tableName + ".tbl");
                }else {
                    this.createTable(tableName + ".tbl", columnInfoList);
                }

            } else {
                System.out.println("\nQuery is invalid!");
            }
        }

        // Drop a table
        else if (action.equalsIgnoreCase("DROP")) {
            if (querySplit.length == 3) {
                this.tableName = querySplit[2];
                this.dropTable(tableName);
                System.out.println("\nDropped table: " + this.tableName);
            }
            else
                System.out.println("\nQuery is invalid!");
        }

        // Invalid
        else {
            System.out.println("\nQuery is invalid!");
        }
    }

    // Will add code to show all tables (returned blank Table list as placeholder)
    private void showTables() throws IOException {
        Map<String, Table> tables = InternalSchema.globalInstance.getTables();
        tables.values().forEach(value -> System.out.println(value));
    }


    // Will add code to create a table (returned blank Table as placeholder)
    private Table createTable(String tableName) throws Exception {
        Column.Builder[] columnBuilders = {};
        Table newTable = globalInstance.createTable(tableName, columnBuilders);
        return newTable;
    }
    // Will add code to create a table (returned blank Table as placeholder)
    private Table createTable(String tableName, List<String> columnInfo) throws Exception {
        List<Column.Builder> builders = new ArrayList<>();

        for(String column : columnInfo){
            String[] columnSplit = column.split("\\s+");
            String columnName = columnSplit[0];

            String dataTypeString = columnSplit[1];
            AbstractDataType dataType = switch (dataTypeString.toUpperCase()) {
                case "NULL" -> new NullType();
                case "BYTE" -> new ByteType();
                case "SHORT" -> new ShortType();
                case "INTEGER" -> new IntegerType();
                case "LONG" -> new LongType();
                case "FLOAT" -> new FloatType();
                case "DOUBLE" -> new DoubleType();
                case "YEAR" -> new YearType();
                case "TIME" -> new TimeType();
                case "DATETIME" -> new DateTimeType();
                case "DATE" -> new DateType();
                case "STRING" -> new StringType(50);
                case "BOOLEAN" -> new BooleanType();
                default -> throw new IllegalStateException("Unexpected value: " + dataTypeString);
            };

            Column.Builder builder = new Column.Builder(columnName, dataType);
            if (columnInfo.contains("NOT NULL")) {
                builder.addExtension(ColumnFlag.NOT_NULL);
            }

            if (columnInfo.contains("UNIQUE")) {
                builder.addExtension(ColumnFlag.UNIQUE);
            }

            if (columnInfo.contains("PRIMARY KEY")) {
                builder.addExtension(ColumnFlag.PRIMARY_KEY);
            }

            builders.add(builder);
        }

        Column.Builder[] columnBuilders = builders.toArray(new Column.Builder[0]);


        Table newTable = globalInstance.createTable(tableName, columnBuilders);
        return newTable;
    }

    // Will add code to drop a table (returned blank Table as a placeholder)
    private void dropTable(String tableName) {
        try {
            globalInstance.dropTable(tableName + ".tbl");
        } catch (Exception e) {
            System.out.println("ERROR: unable to drop table");
            System.out.println(e);
        }
    }
}
