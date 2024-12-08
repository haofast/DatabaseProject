package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class DateTimeType extends AbstractDataType {
    public DateTimeType() {
        super(DataTypeCode.DATETIME, 8);
    }
}
