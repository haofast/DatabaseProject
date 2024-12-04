package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class TimeType extends AbstractDataType {
    public TimeType() {
        super(DataTypeCode.TIME, 4);
    }
}
