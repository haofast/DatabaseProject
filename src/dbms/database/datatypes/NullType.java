package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class NullType extends AbstractDataType {
    public NullType() {
        super(DataTypeCode.NULL, 0);
    }
}
