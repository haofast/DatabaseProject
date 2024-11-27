import dbms.userInterface.CLI;
import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.Column;
import dbms.table.Table;
import dbms.utilities.CsvRaf;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        Column.Builder[] columns = {
            new Column.Builder("Row ID", 4, DataType.INTEGER).addExtension(ColumnFlag.AUTO_INCREMENT),
            new Column.Builder("SSN", 9, DataType.STRING).addExtension(ColumnFlag.PRIMARY_KEY),
            new Column.Builder("First Name", 20, DataType.STRING),
            new Column.Builder("Middle Initial", 1, DataType.STRING),
            new Column.Builder("Last Name", 20, DataType.STRING),
            new Column.Builder("Birth Date", 10, DataType.STRING),
            new Column.Builder("Address", 40, DataType.STRING),
            new Column.Builder("Sex", 1, DataType.STRING),
            new Column.Builder("Salary", 4, DataType.INTEGER),
            new Column.Builder("Department Number", 2, DataType.SHORT),
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

        CLI userInterface = new CLI();
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

            try {
                table.addRecord(lineData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // close csv file
        csvFile.close();
    }
}