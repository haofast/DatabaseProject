package dbms.dmlAndDqlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class Update {
    private final String tableName;
    private final String columnName;
    private final String value;
    private final String condition;

    // Constructor only takes in names of variables in query
    // Will add more methods and code to handle commands and backend
    public Update(String tableName, String columnName, String value, String condition) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.value = value;
        this.condition = condition;
    }
}
