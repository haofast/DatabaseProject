package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class YearType extends AbstractDataType {
    public YearType() {
        super(DataTypeCode.YEAR, 1);
    }
}
