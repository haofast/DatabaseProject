package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class DateTimeType extends AbstractDataType {
    public DateTimeType() {
        super(DataTypeCode.DATETIME, 8);
    }
}
