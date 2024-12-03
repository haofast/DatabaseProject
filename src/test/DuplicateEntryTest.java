package test;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.Column;
import dbms.table.Table;

import java.util.List;

public class DuplicateEntryTest {
    /*
     * Description: should not insert records that have a duplicate primary key value
     * Expected behavior: only the row {"123458769", "Brandon", "Y", "Ho"} should be added and errors should be caught
     */
    public static void main(String[] args) {
        Column.Builder[] columns = {
            new Column.Builder("SSN", 9, DataType.STRING).addExtension(ColumnFlag.PRIMARY_KEY),
            new Column.Builder("First Name", 20, DataType.STRING),
            new Column.Builder("Middle Initial", 1, DataType.STRING),
            new Column.Builder("Last Name", 20, DataType.STRING),
        };

        Table table = new Table(columns);

        table.addRecord(List.of(new String[]{"123456789", "Brandon", "Y", "Ho"}));

        System.out.println("Table records after first attempted insertion:");
        System.out.println(table);

        table.addRecord(List.of(new String[]{"123456789", "Brian", "D", "Yip"}));

        System.out.println("Table records after second attempted insertion:");
        System.out.println(table);
    }
}
