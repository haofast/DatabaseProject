package dbms.dmlAndDqlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class Select {
    private final String columnList;
    private final String tableName;
    private final String condition;

    // Constructor only takes in names of variables in query
    // Will add more methods and code to handle commands and backend
    public Select(String columnList, String tableName, String condition) {
        this.columnList = columnList;
        this.tableName = tableName;
        this.condition = condition;
    }
}
