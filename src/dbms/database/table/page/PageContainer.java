package dbms.database.table.page;

import dbms.database.builders.RecordBuilder;
import dbms.database.table.Header;
import dbms.database.table.Table;
import dbms.utilities.ExtendedRaf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PageContainer {

    private final Table table;
    private final Header header;

    List<InteriorPage> interiorPages = new ArrayList<>();
    List<LeafPage> leafPages = new ArrayList<>();

    public PageContainer(Table table, Header header) {
        this.table = table;
        this.header = header;
        this.initializeFirstPage();
    }

    private void initializeFirstPage() {
        LeafPage newLeafPage = new LeafPage(this.table, this.header, this.getLeafPageFileOffset(this.leafPages.size()));
        this.leafPages.add(newLeafPage);
    }

    protected void addNewPage(int rowIDKey) {
        InteriorPage newInteriorPage = new InteriorPage(rowIDKey, this.getInteriorPageFileOffset(this.interiorPages.size()));
        if (!this.interiorPages.isEmpty())
            this.interiorPages.getLast().rightNeighborPageNumber = this.interiorPages.size();
        this.interiorPages.add(newInteriorPage);

        LeafPage newLeafPage = new LeafPage(this.table, this.header, this.getLeafPageFileOffset(this.leafPages.size()));
        this.leafPages.getLast().rightNeighborPageNumber = this.leafPages.size();
        this.leafPages.add(newLeafPage);
    }

    public boolean addRecord(RecordBuilder recordBuilder, int rowID) {
        if (this.leafPages.getLast().isPageFull()) this.addNewPage(rowID);
        return this.leafPages.getLast().addRecord(recordBuilder);
    }

    public List<Record> getRecords() {
        return new ArrayList<>(this.leafPages.stream().map(LeafPage::getRecords).flatMap(List::stream).toList());
    }

    public Record getRecordByRowID(int rowID) {
        int leafPageIndex = IntStream.range(0, this.interiorPages.size())
            .filter(i -> rowID < this.interiorPages.get(i).getRowIDKey())
            .findFirst().orElse(this.interiorPages.size());

        return this.leafPages.get(leafPageIndex).getRecordByRowID(rowID);
    }

    protected int getInteriorPageFileOffset(int pageIndex) {
        return (pageIndex * 2 + 1) * AbstractPage.PAGE_SIZE;
    }

    protected int getLeafPageFileOffset(int pageIndex) {
        return pageIndex * 2 * AbstractPage.PAGE_SIZE;
    }

    protected int getRootPageNumber() {
        if (this.interiorPages.isEmpty()) return 0;
        return this.interiorPages.size();
    }

    protected int getRightChildPageNumber(InteriorPage interiorPage) {
        return this.interiorPages.indexOf(interiorPage) + 1;
    }

    protected int getLeftChildPageNumber(InteriorPage interiorPage) {
        return this.interiorPages.indexOf(interiorPage);
    }

    protected int getParentPageNumber(LeafPage leafPage) {
        if (this.interiorPages.isEmpty()) return AbstractPage.NULL_OFFSET_VALUE;
        return Math.max(0, this.leafPages.indexOf(leafPage) - 1);
    }

    public void write(ExtendedRaf raf) throws IOException {
        // write leaf and interior pages
        for (int i = 0; i < interiorPages.size(); i++) {
            this.leafPages.get(i).write(raf, this);
            this.interiorPages.get(i).write(raf, this);
        }

        // there is always 1 more leaf page than interior page
        this.leafPages.getLast().write(raf, this);
    }

    public void read(ExtendedRaf raf) throws IOException {
        this.interiorPages.clear();
        this.leafPages.clear();

        this.initializeFirstPage();
        this.leafPages.getFirst().read(raf);

        // read in leaf pages
        while (this.leafPages.getLast().rightNeighborPageNumber != AbstractPage.NULL_OFFSET_VALUE) {
            LeafPage leafPage = new LeafPage(this.table, this.header, this.getLeafPageFileOffset(this.leafPages.size()));
            this.leafPages.add(leafPage);
            leafPage.read(raf);
        }

        // read in interior pages
        for (int i = 0; i < leafPages.size() - 1; i++) {
            InteriorPage interiorPage = new InteriorPage(-1, this.getInteriorPageFileOffset(this.interiorPages.size()));
            this.interiorPages.add(interiorPage);
            interiorPage.read(raf);
        }
    }
}
