package dbms.database.table;

import dbms.database.builders.RecordBuilder;
import dbms.database.table.page.Criteria;
import dbms.database.table.page.PageContainer;
import dbms.database.table.page.Record;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private int nextFreeRowID;
    private final String name;
    private final Header header;
    private final Indexer indexer;
    private final PageContainer pageContainer;

    public Table(String name, Column.Builder[] columnBuilders) {
        this.nextFreeRowID = 1;
        this.name = name;
        this.header = new Header(this, columnBuilders);
        this.indexer = new Indexer(this, this.header);
        this.pageContainer = new PageContainer(this, this.header);
    }

    public Table(String name, Column.Builder[] columnBuilders, int lastUsedRowID) {
        this(name, columnBuilders);
        this.nextFreeRowID = lastUsedRowID + 1;
    }

    public int getLastUsedRowID() {
        return this.nextFreeRowID - 1;
    }

    public String getName() {
        return this.name;
    }

    public List<Column> getColumns() {
        return this.header.getColumns();
    }

    public List<dbms.database.table.page.Record> getRecords() {
        return this.pageContainer.getRecords();
    }

    public Record getRecordByRowID(int rowID) {
        return this.pageContainer.getRecordByRowID(rowID);
    }

    public void addRecord(List<String> userValues) {
        // add value for row ID
        List<String> values = new ArrayList<>(userValues);
        values.addFirst(String.valueOf(nextFreeRowID));

        // attempt to add new record int page, update index if successful
        if (this.pageContainer.addRecord(new RecordBuilder(this.header, values), nextFreeRowID)) {
            this.indexer.updateIndices(this.pageContainer.getRecordByRowID(nextFreeRowID));
            nextFreeRowID++;
        }
    }

    public void createIndex(String indexName, String columnName) {
        this.indexer.createIndex(indexName, columnName);
    }

    public List<Record> searchRecordsByValue(String columnName, String searchKeyValue) {
        return this.indexer.searchRecordsByValue(columnName, searchKeyValue);
    }

    public List<Record> searchRecordsByCriteria(Criteria criteria) {
        return this.indexer.searchRecordsByCriteria(criteria);
    }

    public void write() throws IOException {
        ExtendedRaf raf = new ExtendedRaf(this.name, "rw");
        this.pageContainer.write(raf);
        raf.close();
    }

    public void read() throws IOException {
        ExtendedRaf raf = new ExtendedRaf(this.name, "r");
        this.pageContainer.read(raf);
        raf.close();

        List<Record> records = this.getRecords();
        if (!records.isEmpty()) this.nextFreeRowID = records.getLast().getRowIDValue() + 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.header.toString());
        this.getRecords().forEach(r -> sb.append('\n').append(r));
        return sb.toString();
    }
}
