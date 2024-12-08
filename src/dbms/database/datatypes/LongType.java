package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class LongType extends AbstractDataType {
    public LongType() {
        super(DataTypeCode.LONG, 8);
    }
}
