package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class NullType extends AbstractDataType {
    public NullType() {
        super(DataTypeCode.NULL, 0);
    }
}
