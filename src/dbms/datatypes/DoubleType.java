package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class DoubleType extends AbstractDataType {
    public DoubleType() {
        super(DataTypeCode.DOUBLE, 8);
    }
}
