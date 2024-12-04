package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class DateType extends AbstractDataType {
    public DateType() {
        super(DataTypeCode.DATE, 8);
    }
}
