package dbms.ddlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class IndexCommands {
    // Class variables
    private String query;
    private String indexName;
    private String tableName;

    // Constructor
    public IndexCommands(String query) {
        // Initiate
        this.query = query.trim();
        this.indexName = "";
        this.tableName = "";

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
        String action = querySplit[0];

        // Create an index
        if (action.equalsIgnoreCase("CREATE")) {
            if (querySplit.length == 6) {
                this.indexName = querySplit[2];
                this.tableName = querySplit[4];

                System.out.println("\nIndex Created: " + this.indexName);
                System.out.println("\nCreated in Table: " + this.tableName);

                int columnNameLastIdx = querySplit[5].length() - 1;
                String columnName = querySplit[5].substring(1, columnNameLastIdx);
                this.createIndex(this.indexName, this.tableName, columnName);
            }
            else
                System.out.println("\nQuery is invalid!");
        }

        // Drop an index
        else if (action.equalsIgnoreCase("DROP")) {
            if (querySplit.length == 3) {
                this.indexName = querySplit[2];

                System.out.println("\nIndex dropped: " + this.indexName);
                this.dropIndex(this.indexName);
            }
            else
                System.out.println("\nQuery is invalid!");
        }

        // Invalid
        else
            System.out.println("\nQuery is invalid!");
    }

    // Will add code to create index
    private void createIndex(String indexName, String tableName, String columnName) {

    }

    // Will add code to drop index
    public void dropIndex(String indexName) {

    }
}
