import dbms.datatypes.IntegerType;
import dbms.datatypes.ShortType;
import dbms.datatypes.StringType;
import dbms.internalSchema.InternalSchema;
import dbms.userInterface.CLI;
import dbms.constants.ColumnFlag;
import dbms.table.Column;
import dbms.table.Record;
import dbms.table.Table;
import dbms.utilities.CsvRaf;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        InternalSchema internalSchema = new InternalSchema();
        employeeCSVDemo();
        CLI userInterface = new CLI();
    }

    public static void employeeCSVDemo() throws Exception {
        Column.Builder[] columns = {
            new Column.Builder("SSN", new StringType(9)).addExtension(ColumnFlag.PRIMARY_KEY),
            new Column.Builder("First Name", new StringType(20)),
            new Column.Builder("Middle Initial", new StringType(1)),
            new Column.Builder("Last Name", new StringType(20)),
            new Column.Builder("Birth Date", new StringType(10)),
            new Column.Builder("Address", new StringType(40)),
            new Column.Builder("Sex", new StringType(1)),
            new Column.Builder("Salary", new IntegerType()),
            new Column.Builder("Department Number", new ShortType()),
        };

        Table table = new Table(columns);
        populateTable(table);

        System.out.println(">> Input >>");
        System.out.println(table);
        System.out.println();

        System.out.println("Writing table...");
        table.write("employee.tbl");
        System.out.println();

        System.out.println("Reading table...");
        table.read("employee.tbl");
        System.out.println();

        System.out.println(">> Output >>");
        System.out.println(table);
        System.out.println();
    }

    public static void testIndexRetrieval(Table table) {
        System.out.println(">> Records with Salary == 35000 >>");
        table.createIndex("test_index", "Salary");
        List<Record> searchResults = table.searchRecordsByValue("Salary", "35000");
        searchResults.forEach(System.out::println);
    }


    public static void populateTable(Table table) throws Exception {
        // open csv file
        CsvRaf csvFile = new CsvRaf("employee.csv", "r");

        // read header and find index of ssn
        List<String> header = csvFile.readLineData().stream().map(String::toLowerCase).toList();
        int indexOfSsn = header.indexOf("ssn");

        // mutate data into the correct format for the table
        csvFile.forEachLineData((lineData, lineIndex) -> {
            String ssnValue = lineData.remove(indexOfSsn).replaceAll("[^\\d.]", "");
            lineData.addFirst(ssnValue);
            table.addRecord(lineData);
        });

        // close csv file
        csvFile.close();
    }
}