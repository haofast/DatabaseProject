package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class YearType extends AbstractDataType {
    public YearType() {
        super(DataTypeCode.YEAR, 1);
    }
}
