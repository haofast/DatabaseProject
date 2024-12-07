package dbms.table.page;

import dbms.builders.RecordBuilder;
import dbms.exceptions.InvalidValueException;
import dbms.table.Header;
import dbms.table.Record;
import dbms.table.Table;
import dbms.table.cell.ICell;
import dbms.utilities.ExtendedRaf;
import dbms.interfaces.IBundleable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class LeafPage extends AbstractPage implements IBundleable<LeafPage> {

    public static final int PAGE_HEADER_SIZE = 16;
    public static final int RECORD_OFFSET_SIZE = 2;

    private final Table table;
    private final Header header;
    private List<Record> records;

    public LeafPage(Table table, Header header, int fileOffset) {
        super(fileOffset);
        this.table = table;
        this.header = header;
        this.records = new ArrayList<>();
    }

    @Override
    public LeafPage getObjectCopy() {
        LeafPage copyOfSelf = new LeafPage(this.table, this.header, this.fileOffset);
        copyOfSelf.records = new ArrayList<>(this.records.stream().map(dbms.table.Record::getObjectCopy).toList());
        return copyOfSelf;
    }

    @Override
    public void setObjectState(LeafPage object) {
        this.records = object.getRecords();
    }

    public List<Record> getRecords() {
        return new ArrayList<>(this.records);
    }

    public Record getRecordByRowID(int rowID) {
        return this.records.stream().filter(r -> r.getRowIDValue() == rowID).findFirst().orElse(null);
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

    public void write(ExtendedRaf raf, PageContainer pageContainer) throws IOException {
        // clear entire page on disk
        this.zeroOutEntirePage(raf);

        // write the type of b-tree page (0x0d for leaf page)
        raf.seek(this.fileOffset);
        raf.writeByte(13);

        // write the number of records in the page
        raf.seek(this.fileOffset + 2);
        raf.writeShort(this.records.size());

        // write the page offset of the start of the cell content area
        raf.seek(this.fileOffset + 4);
        raf.writeShort(PAGE_SIZE);

        // write the page number of the root page of the file (same for all pages)
        raf.seek(this.fileOffset + 10);
        raf.writeShort(pageContainer.getRootPageNumber());

        // write the page number of the parent page
        raf.seek(this.fileOffset + 12);
        raf.writeShort(pageContainer.getParentPageNumber(this));

        // write the page number of right neighbor
        raf.seek(this.fileOffset + 14);
        raf.writeShort(this.rightNeighborPageNumber);

        // write relative offsets of each record (sorted by primary key) into header
        raf.seek(this.fileOffset + 16);
        for (int relativeOffset : this.getRecordOffsetsSortedByKeyValue().values()) {
            raf.writeShort(relativeOffset);
        }

        // write records onto disk
        for (Record record : records) {
            record.write(raf);
        }
    }

    public void read(ExtendedRaf raf) throws IOException {
        // clear all records
        this.records.clear();

        // move to start of page on disk
        raf.seek(this.fileOffset);

        // read number of records from header
        raf.seek(this.fileOffset + 2);
        int numRecords = raf.readShort();

        // read the page offset of the parent page (it is the right neighbor for interior page)
        raf.seek(this.fileOffset + 14);
        this.rightNeighborPageNumber = (int) raf.readShort();

        // initialize records objects
        for (int i = 0; i < numRecords; i++) {
            this.records.add(new RecordBuilder(header).build(this));
        }

        // read data for records from disk
        for (Record record : this.records) {
            record.read(raf);
        }
    }

    private SortedMap<ICell, Integer> getRecordOffsetsSortedByKeyValue() {
        // store primary key value to relative offset mapping
        SortedMap<ICell, Integer> keyValueToRelativeOffsetMap = new TreeMap<>();

        // sort relative offset of each record by primary key value
        for (Record record : this.records) {
            keyValueToRelativeOffsetMap.put(record.getPrimaryKeyCell(), record.getPageRelativeOffset());
        }

        // return map of <cell (cell value), relative_offset>
        return keyValueToRelativeOffsetMap;
    }


    public boolean isPageFull() {
        return getBytesFree() < RECORD_OFFSET_SIZE + this.header.getRecordSize();
    }

    public int getBytesUsedByRecordInfo() {
        int recordOffsetRegionSize = RECORD_OFFSET_SIZE * this.records.size();
        int recordDataRegionSize = this.header.getRecordSize() * this.records.size();
        return recordOffsetRegionSize + recordDataRegionSize;
    }

    public int getBytesUsed() {
        return PAGE_HEADER_SIZE + getBytesUsedByRecordInfo();
    }

    public int getBytesFree() {
        return PAGE_SIZE - this.getBytesUsed();
    }
}
