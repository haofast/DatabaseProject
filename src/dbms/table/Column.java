package dbms.table;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;

import java.util.HashSet;
import java.util.Set;

public class Column {

    private final Header          header;
    private final String          name;
    private final int             size;
    private final DataType        type;
    private final Set<ColumnFlag> flags;

    public Column(Header header, Builder builder) {
        this.header = header;
        this.name = builder.name;
        this.size = builder.size;
        this.type = builder.type;
        this.flags = builder.flags;
    }

    public int getIndex() {
        return this.header.getColumns().indexOf(this);
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public DataType getType() {
        return this.type;
    }

    public boolean hasFlag(ColumnFlag flag) {
        return this.flags.contains(flag);
    }

    public String toString() {
        return this.name;
    }

    public static class Builder {
        private final String          name;
        private final int             size;
        private final DataType        type;
        private final Set<ColumnFlag> flags = new HashSet<>();

        public Builder(String name, int size, DataType type) {
            this.name = name;
            this.size = size;
            this.type = type;
        }

        public Builder addExtension(ColumnFlag extension) {
            this.flags.add(extension);
            return this;
        }

        public Column build(Header header) {
            return new Column(header, this);
        }
    }
}
