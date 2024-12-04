package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class LongType extends AbstractDataType {
    public LongType() {
        super(DataTypeCode.LONG, 8);
    }
}
