package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class BooleanType extends AbstractDataType {
    public BooleanType() {
        super(DataTypeCode.BOOLEAN, 1);
    }
}
