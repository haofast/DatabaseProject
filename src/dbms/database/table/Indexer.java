package dbms.database.table;

import dbms.database.index.Index;
import dbms.database.table.page.Criteria;
import dbms.database.table.page.Record;

import java.util.*;

public class Indexer {

    private final Table table;
    private final Header header;
    private final List<Index> indices;

    protected Indexer(Table table, Header header) {
        this.table = table;
        this.header = header;
        this.indices = new ArrayList<>();
    }

    /* creates a new index */
    protected void createIndex(String indexName, String columnName) {
        Column column = getColumnByName(columnName);

        // cannot create an index if the index name is already taken
        verifyIndexNameIsAvailable(indexName);

        // cannot create index on column that already has an index
        verifyColumnHasNoIndex(column);

        // create and add the index
        Index index = new Index(indexName, column, this.table.getRecords());
        this.indices.add(index);
    }

    /* updates all indices with a new entry */
    protected void updateIndices(dbms.database.table.page.Record record) {
        this.indices.forEach(i -> i.addRecord(record));
    }

    /* search records in the index based on a specific column value */
    protected List<dbms.database.table.page.Record> searchRecordsByValue(String columnName, String searchKeyValue) {
        Column column = getColumnByName(columnName);
        Index index = this.getIndexByColumn(column);

        return (index == null)
                ? searchRecordsByLinearSearch(column.getIndex(), searchKeyValue)
                : index.getRecordsByKeyValue(column, searchKeyValue);
    }

    /* search records in the index based on a multiple column values */
    protected List<dbms.database.table.page.Record> searchRecordsByCriteria(Criteria criteria) {
        List<Column> columnObjects = getColumnsByName(criteria.getColumnNames());
        List<Index> indexs = this.getIndexsForColumns(columnObjects);

        return searchRecordsByLinearSearch(columnObjects, criteria);

        //todo add index functinoality to multiple column search
//        return (indexs.isEmpty())
//                ? searchRecordsByLinearSearch(columnObjects, criteria)
//                : getRecordsByKeyValues(indexs, criteria);
    }

    private List<dbms.database.table.page.Record> searchRecordsByLinearSearch(int columnIndex, String searchKeyValue) {
        return new ArrayList<>(this.table.getRecords().stream().filter(r -> r.getCellByIndex(columnIndex).getValue().equals(searchKeyValue)).toList());
    }

    private List<dbms.database.table.page.Record> searchRecordsByLinearSearch(List<Column> columnObjects, Criteria criteria) {

        int initalColumnIndex = columnObjects.getFirst().getIndex();
        String searchKeyValue = criteria.getValue(columnObjects.getFirst().getName());

        List<dbms.database.table.page.Record> records = this.table.getRecords().stream().filter(r -> r.getCellByIndex(initalColumnIndex).getValue().equals(searchKeyValue)).toList();
        ArrayList<Record> resultSet = new ArrayList<>();
        for (Record record : records) {
            if(record.meetsCriteria(criteria)){
                resultSet.add(record);
            };
        }

        return resultSet;
    }

    private Index getIndexByName(String indexName) {
        return this.indices.stream().filter(i -> i.getName().equals(indexName)).findFirst().orElse(null);
    }

    private Index getIndexByColumn(Column column) {
        return this.indices.stream().filter(i -> i.getColumn().equals(column)).findFirst().orElse(null);
    }

    private List<Index> getIndexsForColumns(List<Column> columns) {
        List<Index> indexObjects = new ArrayList<>(List.of());
        for (Column column : columns) {
            indexObjects.add(this.indices.stream().filter(i -> i.getColumn().equals(column)).findFirst().orElse(null));
        }
        return indexObjects;
    }

    private Column getColumnByName(String columnName) {
        verifyColumnExists(columnName);
        return this.header.getColumnByName(columnName);
    }

    private List<Column> getColumnsByName(Set<String> columnNames) {
        for (String columnName : columnNames) verifyColumnExists(columnName);
        return this.header.getColumnsByName(columnNames);
    }

    private void verifyColumnExists(String columnName) {
        if (this.header.getColumnByName(columnName) == null) {
            throw new RuntimeException("Column with name \"" + columnName + "\" does not exist");
        }
    }

    private void verifyIndexNameIsAvailable(String indexName) {
        if (getIndexByName(indexName) != null) {
            throw new RuntimeException("Index with name \"" + indexName + "\" already exists");
        }
    }

    private void verifyColumnHasNoIndex(Column column) {
        if (this.indices.stream().anyMatch(i -> i.getColumn().equals(column))) {
            throw new RuntimeException("An index on this column already exists");
        }
    }
}
