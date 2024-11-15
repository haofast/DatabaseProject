package dbms.table;

import dbms.constants.DataType;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Record {

    private final Page page;
    private final Header header;
    private final List<String> values;
    private boolean markedForDeletion;

    protected Record(Page page, Header header, List<String> values) {
        this.page = page;
        this.header = header;
        this.values = values;
    }

    protected Record(Page page, Header header) {
        this(page, header, new ArrayList<>(header.getColumns().size()));
    }

    private int getIndexInPage() {
        return this.page.getRecords().indexOf(this);
    }

    private int getAbsoluteOffset() {
        return this.page.getAbsoluteOffset() + this.getPageRelativeOffset();
    }

    protected int getPageRelativeOffset() {
        return Page.PAGE_SIZE - ((this.getIndexInPage() + 1) * header.getRecordSize());
    }

    protected String getPrimaryKeyValue() {
        return this.values.get(this.header.getPrimaryKeyColumn().getIndex());
    }

    protected List<String> getValues() {
        return new ArrayList<>(this.values);
    }

    protected void write(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());

        // write record values to disk
        for (int i = 0; i < this.header.getColumns().size(); i++) {
            Column column = this.header.getColumns().get(i);
            String value = this.values.get(i);

            switch (column.getType()) {
                case DataType.INTEGER -> raf.writeInt(Integer.parseInt(value));
                case DataType.SHORT -> raf.writeShort(Short.parseShort(value));
                case DataType.STRING -> raf.write(Arrays.copyOf(value.getBytes(), column.getSize()));
            }
        }

        // write deletion marker to disk
        raf.writeByte(this.markedForDeletion ? 1 : 0);
    }

    protected void read(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());

        // read record values from disk
        for (int i = 0; i < this.header.getColumns().size(); i++) {
            Column column = this.header.getColumns().get(i);

            switch (column.getType()) {
                case DataType.INTEGER -> values.add(i, String.valueOf(raf.readInt()));
                case DataType.SHORT -> values.add(i, String.valueOf(raf.readShort()));
                case DataType.STRING -> values.add(i, raf.readString(column.getSize()));
            }
        }

        // read deletion marker from disk
        this.markedForDeletion = raf.readByte() != 0;
    }

    public String toString() {
        List<String> values = this.getValues();
        values.add(this.markedForDeletion ? "deleted" : "not deleted");
        return values.toString();
    }
}
