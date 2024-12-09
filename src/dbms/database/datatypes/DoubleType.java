package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class DoubleType extends AbstractDataType {
    public DoubleType() {
        super(DataTypeCode.DOUBLE, 8);
    }
}
