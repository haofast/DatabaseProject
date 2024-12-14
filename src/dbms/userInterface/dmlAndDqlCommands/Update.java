package dbms.userInterface.dmlAndDqlCommands;

import dbms.database.internalSchema.InternalSchema;
import dbms.database.table.page.Record;
import dbms.database.table.Table;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update {
    // Class variables
    private String query;
    private String tableName;
    private String columnName;
    private String value;
    private String condition;

    // Constructor only takes in query
    // Will add more methods and code to handle commands and backend
    public Update(String query) {
        // Initiate
        this.query = query.trim();
        this.tableName = "";
        this.columnName = "";
        this.value = "";
        this.condition = "";

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
    private void handleQuery() {
        // Split query and get first character for action
        String[] querySplit = this.query.trim().split("\\s+");

        // Execute update query if query is of valid length
        if (querySplit.length >= 6) {
            this.tableName = querySplit[1].trim();
            this.condition = querySplit[5].trim();

            System.out.println("\nUpdate from Table: " + this.tableName);
            System.out.println("\nUpdate on Condition: " + this.condition);

            // Split on = sign to get column name and value
            String[] columnNameValue = querySplit[3].split("=");
            this.columnName = columnNameValue[0].trim();
            this.value = columnNameValue[1].trim();

            System.out.println("\nUpdate Column: " + this.columnName);
            System.out.println("\nUpdated Value " + this.value);


            try {
                // Get the table from internal schema
                Table table = InternalSchema.globalInstance.getTable(this.tableName + ".tbl");
                List<Record> records = table.searchRecordsByValue(this.columnName, this.value);

                // create column_name -> value map
                Map<String, String> valuePair = new HashMap<>();
                valuePair.put(this.columnName, this.value);

                // update all records matching where condition
                for(Record r : records) { r.setValues(new HashMap<>(valuePair)); }
                InternalSchema.globalInstance.saveTable(table);

            } catch (IOException e) {
                System.out.println("ERROR: unable to update record");
                System.out.println(e);
            }
        }
        else
            System.out.println("\nQuery is invalid!");
    }
}
