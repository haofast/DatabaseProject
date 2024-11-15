package dbms.table;

import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Page {

    protected static final int PAGE_SIZE = 4096;

    private final Table table;
    private final Header header;
    private final List<Record> records;

    public Page(Table table, Header header) {
        this.table = table;
        this.header = header;
        this.records = new ArrayList<>();
    }

    protected int getAbsoluteOffset() {
        return this.table.getPages().indexOf(this) * PAGE_SIZE;
    }

    protected List<Record> getRecords() {
        return new ArrayList<>(this.records);
    }

    protected void addRecord(List<String> values) {
        this.records.add(new Record(this, this.header, values));
    }

    protected void write(ExtendedRaf raf) throws IOException {
        // clear entire page on disk
        this.zeroOutEntirePage(raf);

        // move to start of page on disk
        raf.seek(this.getAbsoluteOffset());

        // write number of records into header
        raf.writeShort(records.size());

        // store primary key value to relative offset mapping
        SortedMap<String, Integer> keyValueToRelativeOffsetMap = new TreeMap<>();

        // sort relative offset of each record by primary key value
        for (Record record : this.records) {
            keyValueToRelativeOffsetMap.put(record.getPrimaryKeyValue(), record.getPageRelativeOffset());
        }

        // write relative offsets of each record (sorted by primary key) into header
        for (int relativeOffset : keyValueToRelativeOffsetMap.values()) {
            raf.writeShort(relativeOffset);
        }

        // write records onto disk
        for (Record record : records) {
            record.write(raf);
        }
    }

    protected void read(ExtendedRaf raf) throws IOException {
        // clear all records
        this.records.clear();

        // move to start of page on disk
        raf.seek(this.getAbsoluteOffset());

        // read number of records from header
        int numRecords = raf.readShort();

        // initialize records objects
        for (int i = 0; i < numRecords; i++) {
            this.records.add(new Record(this, header));
        }

        // read data for records from disk
        for (Record record : this.records) {
            record.read(raf);
        }
    }

    private void zeroOutEntirePage(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        raf.write(new byte[PAGE_SIZE]);
    }
}
