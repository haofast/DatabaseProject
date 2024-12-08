package dbms.userInterface.dmlAndDqlCommands;

import java.util.*;

public class Select {
    // Class variables
    private String query;
    private List<String> columnList;
    private String tableName;
    private String condition;
    private boolean returnAll;

    // Constructor only takes in query
    // Will add more methods and code to handle commands and backend
    public Select(String query) {
        // Initiate
        this.query = query.trim();
        this.columnList = new ArrayList<String>();
        this.tableName = "";
        this.condition = "";
        this.returnAll = false;

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

        // Execute select query if query is of valid length
        if (querySplit.length >= 6) {
            String[] twoParts = this.query.trim().split("(?i)FROM");

            // Split on "FROM" to isolate list of columns
            String firstPart = twoParts[0];
            String secondPart = twoParts[1];

            // From first part, get list of columns
            String[] firstPartSplit = firstPart.trim().split("(?i)SELECT");
            String listOfColumnsStr = firstPartSplit[1].trim();

            // From list of columns, populate our list of columns
            String[] listOfColumns = listOfColumnsStr.trim().split(",");
            System.out.println("\nColumns Selected: ");
            for (String col: listOfColumns) {
                col = col.trim();
                if (col.equals("*")) {
                    this.returnAll = true;
                    break;
                }
                this.columnList.add(col);
                System.out.println(col);
            }
            this.checkReturnAll();
            System.out.println("\nReturn All? " + this.returnAll);

            // From second part, get table name and condition
            String[] secondPartSplit = secondPart.trim().split("(?i)WHERE");
            this.tableName = secondPartSplit[0].trim();
            this.condition = secondPartSplit[1].trim();

            System.out.println("\nSelect from Table: " + this.tableName);
            System.out.println("\nSelect on Condition: " + this.condition);
        }
        else
            System.out.println("\nQuery is invalid!");
    }

    private void checkReturnAll() {
        if (this.returnAll)
            this.columnList = new ArrayList<String>();
    }
}
