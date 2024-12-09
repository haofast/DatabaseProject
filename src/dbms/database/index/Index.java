package dbms.database.index;

import dbms.database.cell.CellFactory;
import dbms.database.cell.ICell;
import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.utilities.ExtendedRaf;
import dbms.utilities.btree.BTree;
import dbms.utilities.btree.BTreeKey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Index {

    private final String name;
    private final Column column;
    private final BTree<ICell, dbms.database.table.page.Record> indexTree;

    public Index(String name, Column column, List<dbms.database.table.page.Record> records) {
        this.name = name;
        this.column = column;
        this.indexTree = new BTree<>();
        this.initializeTreeElements(records);
    }

    private void initializeTreeElements(List<dbms.database.table.page.Record> records) {
        int columnIndex = this.column.getIndex();
        records.forEach(r -> this.indexTree.insert(new BTreeKey<>(r.getCellByIndex(columnIndex), r)));
    }

    public String getName() {
        return this.name;
    }

    public Column getColumn() {
        return this.column;
    }

    public String getFilename() {
        return this.name + ".ndx";
    }

    public void addRecord(dbms.database.table.page.Record record) {
        this.indexTree.insert(new BTreeKey<>(record.getCellByIndex(this.column.getIndex()), record));
    }

    public List<dbms.database.table.page.Record> getRecordsByKeyValue(Column column, String keyValue) {
        ICell fakeCell = CellFactory.build(null, column, keyValue);
        return new ArrayList<>(this.indexTree.searchKeys(fakeCell).stream().map(BTreeKey::getObjectValue).toList());
    }

    public List<Record> getRecordsInOrder() {
        return new ArrayList<>(this.indexTree.getKeys().stream().map(BTreeKey::getObjectValue).toList());
    }

    public void write() throws IOException {
        ExtendedRaf raf = new ExtendedRaf(this.getFilename(), "rw");
        // @todo write the data to disk
        raf.close();
    }

    public void read() throws IOException {
        ExtendedRaf raf = new ExtendedRaf(this.getFilename(), "r");
        // @todo read the data from disk
        raf.close();
    }
}
