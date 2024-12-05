package dbms.table;

import dbms.builders.RecordBuilder;
import dbms.exceptions.InvalidValueException;
import dbms.utilities.ExtendedRaf;
import dbms.interfaces.IBundleable;

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

    protected boolean addRecord(RecordBuilder recordBuilder) {
        return this.mutateInBundle(
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
        int absoluteOffset = this.getAbsoluteOffset();

        // clear entire page on disk
        this.zeroOutEntirePage(raf);

        // move to start of page on disk
        raf.seek(absoluteOffset);

        // write number of records into header
        raf.seek(absoluteOffset + 2);
        raf.writeShort(records.size());

        // @todo write offset of the start of cell content area
        raf.seek(absoluteOffset + 4);

        // @todo write pointer that points to the root page of file
        raf.seek(absoluteOffset + 10);

        // @todo write pointer that points to parent page of this page
        raf.seek(absoluteOffset + 12);

        // @todo write pointer that for:
        //   INTERIOR PAGE           - the page number of the rightmost child
        //   NON-RIGHTMOST LEAF PAGE - the page number of the sibling to the right
        //   RIGHTMOST LEAF PAGE     - the special value of 0xFFFF
        raf.seek(absoluteOffset + 14);

        // write relative offsets of each record (sorted by primary key) into header
        raf.seek(absoluteOffset + 16);
        for (int relativeOffset : this.getRecordOffsetsSortedByKeyValue().values()) {
            raf.writeShort(relativeOffset);
        }

        // write records onto disk
        for (Record record : records) {
            record.write(raf);
        }
    }

    protected void read(ExtendedRaf raf) throws IOException {
        int absoluteOffset = this.getAbsoluteOffset();

        // clear all records
        this.records.clear();

        // move to start of page on disk
        raf.seek(absoluteOffset);

        // read number of records from header
        raf.seek(absoluteOffset + 2);
        int numRecords = raf.readShort();

        // @todo read offset of the start of cell content area
        raf.seek(absoluteOffset + 4);

        // @todo read pointer that points to the root page of file
        raf.seek(absoluteOffset + 10);

        // @todo read pointer that points to parent page of this page
        raf.seek(absoluteOffset + 12);

        // @todo read pointer that for:
        //   INTERIOR PAGE           - the page number of the rightmost child
        //   NON-RIGHTMOST LEAF PAGE - the page number of the sibling to the right
        //   RIGHTMOST LEAF PAGE     - the special value of 0xFFFF
        raf.seek(absoluteOffset + 14);

        // initialize records objects
        for (int i = 0; i < numRecords; i++) {
            this.records.add(new RecordBuilder(header).build(this));
        }

        // read data for records from disk
        for (Record record : this.records) {
            record.read(raf);
        }
    }

    private SortedMap<String, Integer> getRecordOffsetsSortedByKeyValue() {
        // store primary key value to relative offset mapping
        SortedMap<String, Integer> keyValueToRelativeOffsetMap = new TreeMap<>();

        // sort relative offset of each record by primary key value
        for (Record record : this.records) {
            keyValueToRelativeOffsetMap.put(record.getPrimaryKeyCell().getValue(), record.getPageRelativeOffset());
        }

        // return map of <key_value, relative_offset>
        return keyValueToRelativeOffsetMap;
    }

    private void zeroOutEntirePage(ExtendedRaf raf) throws IOException {
        raf.seek(this.getAbsoluteOffset());
        raf.write(new byte[PAGE_SIZE]);
    }
}
