package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class ShortType extends AbstractDataType {
    public ShortType() {
        super(DataTypeCode.SHORT, 2);
    }
}
