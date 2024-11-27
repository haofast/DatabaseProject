package dbms.dmlAndDqlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class Delete {
    private final String tableName;
    private final String condition;

    // Constructor only takes in names of variables in query
    // Will add more methods and code to handle commands and backend
    public Delete(String tableName, String condition) {
        this.tableName = tableName;
        this.condition = condition;
    }
}
