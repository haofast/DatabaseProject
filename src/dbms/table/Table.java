package dbms.table;

import dbms.builders.RecordBuilder;
import dbms.exceptions.InvalidValueCountException;
import dbms.exceptions.InvalidValueException;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Table {

    private final Header header;
    private final List<Page> pages;

    public Table(Column.Builder[] columnBuilders) {
        this.header = new Header(this, columnBuilders);
        this.pages = IntStream.range(0, 10).mapToObj(i -> new Page(this, this.header)).toList();
    }

    protected List<Page> getPages() {
        return new ArrayList<>(this.pages);
    }

    public List<Record> getRecords() {
        return this.pages.stream().map(Page::getRecords).flatMap(List::stream).toList();
    }

    public void addRecord(List<String> values) throws InvalidValueCountException, InvalidValueException {
        RecordBuilder recordBuilder = new RecordBuilder(this.header, values);
        Page page = this.pages.get(Integer.parseInt(recordBuilder.getPrimaryKeyValue()) % this.pages.size());
        page.addRecord(recordBuilder);
    }

    public void write(String filePath) throws IOException {
        ExtendedRaf raf = new ExtendedRaf(filePath, "rw");
        for (Page p : this.pages) { p.write(raf); }
        raf.close();
    }

    public void read(String filePath) throws IOException {
        ExtendedRaf raf = new ExtendedRaf(filePath, "r");
        for (Page p : this.pages) { p.read(raf); }
        raf.close();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.header.toString());
        this.getRecords().forEach(r -> sb.append('\n').append(r));
        return sb.toString();
    }
}
