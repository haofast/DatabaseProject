package dbms.internalSchema;

import java.util.Scanner;

public class KyptonColumns {

    public KyptonColumns(){
        initializeKyptonColumns();
    }

    public void initializeKyptonColumns() {
        if(!isInternalKyptonColumnsInitiaized()){
            createKyptonColumns();
        }
    }

    public boolean isInternalKyptonColumnsInitiaized() {
        //if the internal schema files have been found return true
        return false;
    }

    public void createKyptonColumns() {

    }
}
