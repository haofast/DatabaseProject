package dbms.datatypes;

import dbms.constants.DataTypeCode;

public abstract class AbstractDataType {

    protected final DataTypeCode dataType;
    protected final int size;

    protected AbstractDataType(DataTypeCode dataType, int size) {
        this.dataType = dataType;
        this.size = size;
    }

    public DataTypeCode getCode() {
        return this.dataType;
    }

    public int getSize() {
        return this.size;
    }
}
