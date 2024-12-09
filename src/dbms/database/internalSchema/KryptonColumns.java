package dbms.database.internalSchema;

import dbms.database.constants.ColumnFlag;
import dbms.database.datatypes.*;
import dbms.database.table.Column;
import dbms.database.table.page.Record;
import dbms.database.table.Table;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class KryptonColumns {

    private static final String COLUMN_NAME      = "column_name";
    private static final String TABLE_ROW_ID     = "table_rowid";
    private static final String DATA_TYPE        = "data_type";
    private static final String DATA_SIZE        = "data_size";
    private static final String ORDINAL_POSITION = "ordinal_position";
    private static final String IS_NULLABLE      = "is_nullable";
    private static final String IS_UNIQUE        = "is_unique";
    private static final String IS_PRIMARY_KEY   = "is_primary_key";

    private static String COLUMNS_TABLE_NAME = "krypton_columns.tbl";
    private static Table kryptonColumnsTable;

    protected static void initKryptonColumnsTable() throws IOException {
        kryptonColumnsTable = initializeTableInMemory();
        if (tableFileExistsOnDisk()) kryptonColumnsTable.read();
    }

    protected static Table getKryptonColumnsTable() {
        return kryptonColumnsTable;
    }

    public static void write() throws IOException {
        kryptonColumnsTable.write();
    }

    protected static Column.Builder[] getColumnBuildersForTable(int tableRowID) {
        // get all records with matching row ID
        List<Record> columnRecords = kryptonColumnsTable
            .searchRecordsByValue(TABLE_ROW_ID, String.valueOf(tableRowID));

        // create map of column records sorted by ordinal position
        Map<Integer, Record> sortedColumnRecordsMap = new TreeMap<>(
            columnRecords.stream().collect(Collectors.toMap(
                columnRecord -> Integer.parseInt(columnRecord.getValueWithColumnName(ORDINAL_POSITION)),
                columnRecord -> columnRecord
            ))
        );

        // create return column builders for columns, in ascending ordinal order
        List<Column.Builder> columnBuilders = sortedColumnRecordsMap.values().stream().map(columnRecord -> {
            String columnName = columnRecord.getValueWithColumnName(COLUMN_NAME);
            Column.Builder builder = new Column.Builder(columnName, getDataType(columnRecord));

            if (columnRecord.getValueWithColumnName(IS_NULLABLE).equals("false")) {
                builder.addExtension(ColumnFlag.NOT_NULL);
            }

            if (columnRecord.getValueWithColumnName(IS_UNIQUE).equals("true")) {
                builder.addExtension(ColumnFlag.UNIQUE);
            }

            if (columnRecord.getValueWithColumnName(IS_PRIMARY_KEY).equals("true")) {
                builder.addExtension(ColumnFlag.PRIMARY_KEY);
            }

            return builder;
        }).toList();

        Column.Builder[] builderArray = new Column.Builder[columnBuilders.size()];
        columnBuilders.toArray(builderArray);
        return builderArray;
    }

    public static void addColumns(int tableRowID, Table table) {
        for (Column column : table.getColumns()) {
            List<String> values = List.of(new String[]{
                column.getName(),
                String.valueOf(tableRowID),
                column.getType().name(),
                String.valueOf(column.getSize()),
                String.valueOf(column.getIndex()),
                String.valueOf(!column.hasFlag(ColumnFlag.NOT_NULL)),
                String.valueOf(column.hasFlag(ColumnFlag.UNIQUE)),
                String.valueOf(column.hasFlag(ColumnFlag.PRIMARY_KEY))
            });

            kryptonColumnsTable.addRecord(values);
        }
    }

    private static AbstractDataType getDataType(Record columnRecord) {
        String dataTypeString = columnRecord.getValueWithColumnName(DATA_TYPE);
        int dataSize = Integer.parseInt(columnRecord.getValueWithColumnName(DATA_SIZE));

        return switch(dataTypeString) {
            case "NULL"     -> new NullType();
            case "BYTE"     -> new ByteType();
            case "SHORT"    -> new ShortType();
            case "INTEGER"  -> new IntegerType();
            case "LONG"     -> new LongType();
            case "FLOAT"    -> new FloatType();
            case "DOUBLE"   -> new DoubleType();
            case "YEAR"     -> new YearType();
            case "TIME"     -> new TimeType();
            case "DATETIME" -> new DateTimeType();
            case "DATE"     -> new DateType();
            case "STRING"   -> new StringType(dataSize);
            case "BOOLEAN"  -> new BooleanType();
            default         -> throw new IllegalStateException("Unexpected value: " + dataTypeString);
        };
    }

    private static Table initializeTableInMemory() {
        return new Table(COLUMNS_TABLE_NAME, new Column.Builder[]{
            new Column.Builder(COLUMN_NAME,      new StringType(20)),
            new Column.Builder(TABLE_ROW_ID,     new IntegerType()),
            new Column.Builder(DATA_TYPE,        new StringType(9)),
            new Column.Builder(DATA_SIZE,        new StringType(9)),
            new Column.Builder(ORDINAL_POSITION, new ShortType()),
            new Column.Builder(IS_NULLABLE,      new BooleanType()),
            new Column.Builder(IS_UNIQUE,        new BooleanType()),
            new Column.Builder(IS_PRIMARY_KEY,   new BooleanType()),
        });
    }

    private static boolean tableFileExistsOnDisk() {
        return new File(COLUMNS_TABLE_NAME).exists();
    }
}
