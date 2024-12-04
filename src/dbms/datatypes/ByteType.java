package dbms.datatypes;

import dbms.constants.DataTypeCode;

public class ByteType extends AbstractDataType {
    public ByteType() {
        super(DataTypeCode.BYTE, 1);
    }
}
