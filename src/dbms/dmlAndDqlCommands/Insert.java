package dbms.dmlAndDqlCommands;

import dbms.userInterface.*;
import dbms.constants.*;
import dbms.table.*;
import dbms.utilities.*;
import java.util.*;

public class Insert {
    private final String tableName;
    private final String valueList;

    // Constructor only takes in names of variables in query
    // Will add more methods and code to handle commands and backend
    public Insert(String tableName, String valueList) {
        this.tableName = tableName;
        this.valueList = valueList;
    }
}
