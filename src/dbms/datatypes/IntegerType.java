package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class IntegerType extends AbstractDataType {
    public IntegerType() {
        super(DataTypeCode.INTEGER, 4);
    }
}
