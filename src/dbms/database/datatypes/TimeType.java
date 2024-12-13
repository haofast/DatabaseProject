package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class TimeType extends AbstractDataType {
    public TimeType() {
        super(DataTypeCode.TIME, 4);
    }
}
