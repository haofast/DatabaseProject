package dbms.userInterface.CommandParser;

import dbms.database.table.page.Criteria;

import java.util.List;

public class WhereClause {



    public static void processWhere(List<Record> records, String whereConditions) {

        // used to store search criteria
        Criteria criteria = new Criteria();

        // add criteria based on conditions
        for (String condition : whereConditions.trim().split("(?i)AND")) {
            String[] columnNameValue = condition.split("=");
            criteria.add(columnNameValue[0].trim(), columnNameValue[1].trim());
        }





    }

}
