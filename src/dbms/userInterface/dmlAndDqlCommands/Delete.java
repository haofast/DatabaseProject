package dbms.userInterface.dmlAndDqlCommands;

public class Delete {
    // Class variables
    private String query;
    private String tableName;
    private String condition;

    // Constructor only takes in query
    // Will add more methods and code to handle commands and backend
    public Delete(String query) {
        // Initiate
        this.query = query.trim();
        this.tableName = "";
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

        // Execute delete query if query is of valid length
        if (querySplit.length >= 5) {
            // Get table name
            this.tableName = querySplit[2].trim();
            System.out.println("\nDeleting from Table: " + this.tableName);

            // Split on "WHERE" to get condition
            String[] splitOnWhere = this.query.trim().split("(?i)WHERE");
            this.condition = splitOnWhere[1].trim();
            System.out.println("\nDelete Condition: " + this.condition);



        }
        else
            System.out.println("\nQuery is invalid!");
    }
}
