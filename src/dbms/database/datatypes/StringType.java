package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class StringType extends AbstractDataType {
    public StringType(int size) {
        super(DataTypeCode.STRING, size);
    }
}
