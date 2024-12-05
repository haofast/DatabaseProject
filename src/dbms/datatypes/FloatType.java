package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class FloatType extends AbstractDataType {
    public FloatType() {
        super(DataTypeCode.FLOAT, 4);
    }
}
