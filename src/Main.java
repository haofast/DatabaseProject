import dbms.database.datatypes.IntegerType;
import dbms.database.datatypes.ShortType;
import dbms.database.datatypes.StringType;
import dbms.database.internalSchema.InternalSchema;
import dbms.database.constants.ColumnFlag;
import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.table.Table;
import dbms.userInterface.CLI;
import dbms.utilities.CsvRaf;
import dbms.userInterface.ddlCommands.*;
import dbms.userInterface.dmlAndDqlCommands.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        //uncomment employeeCSVDemo for milestone 1 example
        //employeeCSVDemo();

        //internalSchemaAndReadTableDemo();
        InternalSchema internalSchema = new InternalSchema();
        //uncomment printInternalSchema for viewing internal schema at start
        //internalSchema.printInternalSchema();

        CLI userInterface = new CLI();
    }

    public static void internalSchemaAndReadTableDemo() throws IOException {
        InternalSchema internalSchema = new InternalSchema();
        System.out.println(internalSchema);
        System.out.println(internalSchema.getTable("employee.tbl"));
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

        Table table = new Table("employee.tbl", columns);
        populateTable(table);

        System.out.println(">> Input >>");
        System.out.println(table);
        System.out.println();

        System.out.println("Writing table...");
        table.write();
        System.out.println();

        System.out.println("Reading table...");
        table.read();
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


  public static void queryTest() throws Exception {
        TableCommands tc = new TableCommands("DROP TABLE table_name;");
        IndexCommands ic = new IndexCommands("DROP INDEX index_name;");
        Delete d = new Delete("DELETE FROM table_name WHERE val=69;");
        Insert i = new Insert("INSERT INTO TABLE table_name VALUES (value1, value2, value3);");
        Select s = new Select("SELECT * FROM table_name WHERE column_name1>value1 AND column_name2>=value2;");
        Update u = new Update("UPDATE table_name SET column_name=value WHERE condition;");
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