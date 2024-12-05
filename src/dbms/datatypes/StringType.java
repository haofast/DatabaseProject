package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class StringType extends AbstractDataType {
    public StringType(int size) {
        super(DataTypeCode.STRING, size);
    }
}
