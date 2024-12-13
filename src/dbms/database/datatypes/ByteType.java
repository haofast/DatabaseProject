package dbms.database.datatypes;

import dbms.database.constants.DataTypeCode;

public class ByteType extends AbstractDataType {
    public ByteType() {
        super(DataTypeCode.BYTE, 1);
    }
}
