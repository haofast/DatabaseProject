package dbms.userInterface.CommandParser;

import dbms.database.table.Column;
import dbms.database.table.Table;
import dbms.database.table.page.Criteria;
import dbms.database.table.page.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhereClause {



    public static List<Record> processWhere(Table table, String whereConditions) {

        // used to store search criteria
        Criteria criteria = new Criteria();

        List<String> whereClauseSplit = Arrays.asList(whereConditions.trim().split("\\s+"));
        List<Column> columns = table.getColumns();

        String columnName = whereClauseSplit.get(0).toUpperCase().trim();
        String operator = whereClauseSplit.get(1).trim();
        String value = whereClauseSplit.get(2).trim();

        criteria.add(columnName, value);

        // add criteria based on conditions
//        for (String condition : whereConditions.trim().split("(?i)AND")) {
//            String[] columnNameValue = condition.split("=");
//            criteria.add(columnNameValue[0].trim(), columnNameValue[1].trim());
//        }

        return table.searchRecordsByCriteria(criteria);
    }

}
