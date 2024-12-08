package dbms.database.internalSchema;

import java.io.IOException;

public class InternalSchema {
    KyptonTables kyptonTables = new KyptonTables();
    KyptonColumns kyptonColumns = new KyptonColumns();

    public InternalSchema() throws IOException {
        kyptonTables.initializeKyptonTables();
        kyptonColumns.initializeKyptonColumns();
    }

    public void printInternalSchema(){
        System.out.println(">> Internal Schema Initialized >>");
        System.out.println();

        System.out.println(">> kypton_tables Output >>");
        System.out.println(kyptonTables.getKyptonTable());
        System.out.println();

        System.out.println(">> kypton_columns Output >>");
        System.out.println(kyptonColumns.getKyptonColumnTable());
        System.out.println();
    }

    public boolean doesTableHaveColumn(int tableRowId, String columnName){
        return true;
    }

    public boolean isValidTable(String tableName){
        return kyptonTables.doesTableExist(tableName);
    }

    public boolean isValidColumn(String columnName){
        return kyptonColumns.doesColumnExist(columnName);
    }

//    public void getTableColumnBuilder(){
//        Column.Builder[] tableColumns = new Column.Builder[];
//
//        new tableColumns;
//    }
}
