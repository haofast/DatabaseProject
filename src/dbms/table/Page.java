package dbms.table;

import dbms.builders.RecordBuilder;
import dbms.exceptions.InvalidValueException;
import dbms.utilities.ExtendedRaf;
import interfaces.IBundleable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Page implements IBundleable<Page> {

    protected static final int PAGE_SIZE = 4096;

    private final Table table;
    private final Header header;
    private List<Record> records;

    public Page(Table table, Header header) {
        this.table = table;
        this.header = header;
        this.records = new ArrayList<>();
    }

    @Override
    public Page getObjectCopy() {
        Page page = new Page(this.table, this.header);
        page.records = new ArrayList<>(this.records.stream().map(Record::getObjectCopy).toList());
        return page;
    }

    @Override
    public void setObjectState(Page object) {
        this.records = object.getRecords();
    }

    protected int getAbsoluteOffset() {
        return this.table.getPages().indexOf(this) * PAGE_SIZE;
    }

    protected List<Record> getRecords() {
        return new ArrayList<>(this.records);
    }

    protected void addRecord(RecordBuilder recordBuilder) {
        this.mutateInBundle(
            bundledPage -> {
                Record record = recordBuilder.build(this);
                bundledPage.records.add(record);
                record.validate();
            },
            exception -> {
                if (exception instanceof InvalidValueException) {
                    System.out.println(exception.getMessage());
                } else {
                    throw new RuntimeException(exception);
                }
            }
        );
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
            keyValueToRelativeOffsetMap.put(record.getPrimaryKeyCell().getValue(), record.getPageRelativeOffset());
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
            this.records.add(new RecordBuilder(header).build(this));
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
