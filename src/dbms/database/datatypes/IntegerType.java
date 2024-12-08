package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class IntegerType extends AbstractDataType {
    public IntegerType() {
        super(DataTypeCode.INTEGER, 4);
    }
}
