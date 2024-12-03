package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class ShortType extends AbstractDataType {
    public ShortType() {
        super(DataTypeCode.SHORT, 2);
    }
}
