package dbms.builders;

import dbms.exceptions.InvalidValueCountException;
import dbms.table.Column;
import dbms.table.Header;
import dbms.table.Page;
import dbms.table.Record;

import java.util.List;
import java.util.stream.IntStream;

public class RecordBuilder {
    private final Header header;
    private final List<String> values;

    // used when a new record data is being inserted into the table
    public RecordBuilder(Header header, List<String> values) throws InvalidValueCountException {
        this.header = header;
        this.values = this.initializeValues(values);
    }

    // used when record data is being read from disk instead
    public RecordBuilder(Header header) {
        this.header = header;
        this.values = this.initializeEmptyValues();
    }

    private List<String> initializeValues(List<String> values) throws InvalidValueCountException {
        // extract column information
        Column autoIncrementColumn = this.header.getAutoIncrementColumn();
        int numNonAutoIncrementColumns = this.header.getNonAutoIncrementColumns().size();

        // check for auto increment column and add value if no auto increment value was supplied
        if ((autoIncrementColumn != null) && (numNonAutoIncrementColumns == values.size())) {
            values.add(autoIncrementColumn.getIndex(), String.valueOf(autoIncrementColumn.getLargestValue() + 1));
        }

        // verify that the number of values now matches the number of columns
        if (this.header.getColumns().size() != values.size()) {
            throw new InvalidValueCountException(this.header, values);
        }

        return values;
    }

    private List<String> initializeEmptyValues() {
        return IntStream.range(0, header.getColumns().size()).mapToObj(i -> (String) null).toList();
    }

    public Header getHeader() {
        return header;
    }

    public List<String> getValues() {
        return this.values;
    }

    public String getPrimaryKeyValue() {
        return this.values.get(this.header.getPrimaryKeyColumn().getIndex());
    }

    public dbms.table.Record build(Page page) {
        return new Record(page, this);
    }
}
