package dbms.internalSchema;

public class InternalSchema {
    KyptonTables kyptonTables = new KyptonTables();
    KyptonColumns kyptonColumns = new KyptonColumns();

    public void InternalSchema(){
        kyptonTables.initializeKyptonTables();
        kyptonColumns.initializeKyptonColumns();
    }
}
