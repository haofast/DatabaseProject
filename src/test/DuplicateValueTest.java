package test;

import dbms.database.constants.ColumnFlag;
import dbms.database.datatypes.StringType;
import dbms.database.table.Column;
import dbms.database.table.Table;
import dbms.database.table.page.Record;

import java.util.List;
import java.util.Map;

public class DuplicateValueTest {
    /*
     * Description: should not commit changes to rows that would introduce a duplicate key value
     * Expected behavior: the row {"987654321", "Brian", "D", "Yip"} should not be updated and errors should be caught
     */
    public static void main(String[] args) {
        Column.Builder[] columns = {
                new Column.Builder("SSN", new StringType(9)).addExtension(ColumnFlag.PRIMARY_KEY),
                new Column.Builder("First Name", new StringType(20)),
                new Column.Builder("Middle Initial", new StringType(1)),
                new Column.Builder("Last Name", new StringType(20)),
        };

        Table table = new Table(columns);

        table.addRecord(List.of(new String[]{"123456789", "Brandon", "Y", "Ho"}));
        table.addRecord(List.of(new String[]{"987654321", "Brian", "D", "Yip"}));

        System.out.println("Table records before attempted change:");
        System.out.println(table);

        Record record = table.getRecords().stream().filter(r -> r.getPrimaryKeyCell().getValue().equals("987654321")).findFirst().orElse(null);
        record.setValues(Map.ofEntries(Map.entry("SSN", "123456789")));

        System.out.println("Table records after attempted change:");
        System.out.println(table);
    }
}
