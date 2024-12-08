package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class BooleanType extends AbstractDataType {
    public BooleanType() {
        super(DataTypeCode.BOOLEAN, 1);
    }
}
