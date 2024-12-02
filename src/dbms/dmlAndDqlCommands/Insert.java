package dbms.dmlAndDqlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class Insert {
    // Class variables
    private String query;
    private String tableName;
    private List<String> valueList;

    // Constructor only takes in query
    // Will add more methods and code to handle commands and backend
    public Insert(String query) {
        // Initiate
        this.query = query.trim();
        this.tableName = "";
        this.valueList = new ArrayList<String>();

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

        // Execute insert query if query is of valid length
        if (querySplit.length >= 6) {
            this.tableName = querySplit[3].trim();
            System.out.println("\nInserting into Table: " + this.tableName);

            // Split on "VALUES", remove first and last indices of the string of values
            String[] queryOnValues = this.query.trim().split("(?i)VALUES");
            String valuesToInsertStr = queryOnValues[1].trim();

            // Remove open parentheses if exists
            if (valuesToInsertStr.startsWith("("))
                valuesToInsertStr = valuesToInsertStr.substring(1);

            // Remove closed parentheses if exists
            if (valuesToInsertStr.endsWith(")"))
                valuesToInsertStr = valuesToInsertStr.substring(0, valuesToInsertStr.length() - 1);

            // Get list of values to be inserted
            String[] valuesToInsert = valuesToInsertStr.trim().split(",");
            System.out.println("\nValues Inserted Into Table: ");
            for (String val: valuesToInsert) {
                val = val.trim();
                this.valueList.add(val);
                System.out.println(val);
            }
        }
        else
            System.out.println("\nQuery is invalid!");
    }
}
