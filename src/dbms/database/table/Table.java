package dbms.database.table;

import dbms.database.builders.RecordBuilder;
import dbms.database.table.page.Criteria;
import dbms.database.table.page.Record;
import dbms.database.table.page.PageContainer;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private int nextFreeRowID;
    private final Header header;
    private final Indexer indexer;
    private final PageContainer pageContainer;

    public Table(Column.Builder[] columnBuilders) {
        this.nextFreeRowID = 1;
        this.header = new Header(this, columnBuilders);
        this.indexer = new Indexer(this, this.header);
        this.pageContainer = new PageContainer(this, this.header);
    }

    public List<Record> getRecords() {
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

    public void write(String filePath) throws IOException {
        ExtendedRaf raf = new ExtendedRaf(filePath, "rw");
        this.pageContainer.write(raf);
        raf.close();
    }

    public void read(String filePath) throws IOException {
        ExtendedRaf raf = new ExtendedRaf(filePath, "r");
        this.pageContainer.read(raf);
        raf.close();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.header.toString());
        this.getRecords().forEach(r -> sb.append('\n').append(r));
        return sb.toString();
    }

    public boolean doesTableExist(String filePath) {
        boolean tableExists = false;
        try {
            read(filePath);
            tableExists = true;
        }
        catch (Exception e){
            tableExists = false;
        }

        return tableExists;
    }
}
