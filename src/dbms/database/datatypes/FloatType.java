package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class FloatType extends AbstractDataType {
    public FloatType() {
        super(DataTypeCode.FLOAT, 4);
    }
}
