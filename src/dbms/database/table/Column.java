package dbms.database.table;

import dbms.database.constants.ColumnFlag;
import dbms.database.constants.DataTypeCode;
import dbms.database.datatypes.AbstractDataType;
import dbms.database.cell.ICell;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Column {

    private final Table table;
    private final Header header;
    private final String          name;
    private final int             size;
    private final DataTypeCode    type;
    private final Set<ColumnFlag> flags;

    public Column(Table table, Header header, Builder builder) {
        this.table  = table;
        this.header = header;
        this.name   = builder.name;
        this.size   = builder.size;
        this.type   = builder.type;
        this.flags  = builder.flags;
    }

    public int getRelativeRecordOffset() {
        return IntStream.range(0, this.getIndex()).map(i -> this.header.getColumns().get(i).getSize()).sum();
    }

    public List<ICell> getCells() {
        int columnIndex = this.getIndex();
        return this.table.getRecords().stream().map(r -> r.getCells().get(columnIndex)).toList();
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

    public DataTypeCode getType() {
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
        private final DataTypeCode    type;
        private final Set<ColumnFlag> flags = new HashSet<>();

        public Builder(String name, AbstractDataType dataType) {
            this.name = name;
            this.size = dataType.getSize();
            this.type = dataType.getCode();
        }

        public Builder addExtension(ColumnFlag extension) {
            this.flags.add(extension);
            return this;
        }

        public Column build(Table table, Header header) {
            return new Column(table, header, this);
        }
    }
}
