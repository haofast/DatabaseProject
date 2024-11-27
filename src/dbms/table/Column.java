package dbms.table;

import dbms.constants.ColumnFlag;
import dbms.constants.DataType;
import dbms.table.cell.ICell;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Column {

    private final Table           table;
    private final Header          header;
    private final String          name;
    private final int             size;
    private final DataType        type;
    private final Set<ColumnFlag> flags;

    public Column(Table table, Header header, Builder builder) {
        this.table = table;
        this.header = header;
        this.name = builder.name;
        this.size = builder.size;
        this.type = builder.type;
        this.flags = builder.flags;
    }

    public int getRelativeRecordOffset() {
        return IntStream.range(0, this.getIndex()).map(i -> this.header.getColumns().get(i).getSize()).sum();
    }

    public List<ICell> getCells() {
        int columnIndex = this.getIndex();
        return this.table.getRecords().stream().map(r -> r.getCells().get(columnIndex)).toList();
    }

    public int getLargestValue() {
        return this.getCells().stream().mapToInt(c -> Integer.parseInt(c.getValue())).max().orElse(0);
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

        public Column build(Table table, Header header) {
            return new Column(table, header, this);
        }
    }
}
