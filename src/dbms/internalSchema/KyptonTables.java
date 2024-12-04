package dbms.internalSchema;

public class KyptonTables {

    public void KyptonTable(){
        initializeKyptonTables();
    }

    public void initializeKyptonTables() {
        if(!isInternalKyptonTablesInitiaized()){
            createKyptonTables();
        }
    }

    public boolean isInternalKyptonTablesInitiaized() {
        //if the internal schema files have been found return true
        return false;
    }

    public void createKyptonTables() {

    }
}
