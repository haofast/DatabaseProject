package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class DateType extends AbstractDataType {
    public DateType() {
        super(DataTypeCode.DATE, 8);
    }
}
