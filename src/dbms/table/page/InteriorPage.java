package dbms.table.page;

import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public class InteriorPage extends AbstractPage {

    private int rowIDKey;

    protected InteriorPage(int rowIDKey, int fileOffset) {
        super(fileOffset);
        this.rowIDKey = rowIDKey;
    }

    protected int getRowIDKey() {
        return this.rowIDKey;
    }

    protected void write(ExtendedRaf raf, PageContainer pageContainer) throws IOException {
        // clear the entire page
        this.zeroOutEntirePage(raf);

        // write the type of b-tree page (0x05 for interior page)
        raf.seek(this.fileOffset);
        raf.writeByte(5);

        // write the number of records in the page (always 0 for interior page)
        raf.seek(this.fileOffset + 2);
        raf.writeShort(0);

        // write the page offset of the start of the cell content area
        raf.seek(this.fileOffset + 4);
        raf.writeShort(PAGE_SIZE);

        // write the page number of the root page of the file (same for all pages)
        raf.seek(this.fileOffset + 10);
        raf.writeShort(pageContainer.getRootPageNumber());

        // write the page number of the parent page (it is the right neighbor for interior page)
        raf.seek(this.fileOffset + 12);
        raf.writeShort(this.rightNeighborPageNumber);

        // write the page number of the right child page
        raf.seek(this.fileOffset + 14);
        raf.writeShort(pageContainer.getRightChildPageNumber(this));

        // write the page number of the left child page
        raf.seek(this.fileOffset + PAGE_SIZE - 6);
        raf.writeShort(pageContainer.getLeftChildPageNumber(this));

        // write the row ID
        raf.seek(this.fileOffset + PAGE_SIZE - 4);
        raf.writeInt(this.rowIDKey);
    }

    protected void read(ExtendedRaf raf) throws IOException {
        // read the page offset of the parent page (it is the right neighbor for interior page)
        raf.seek(this.fileOffset + 12);
        this.rightNeighborPageNumber = raf.readShort();

        // read the row ID
        raf.seek(this.fileOffset + PAGE_SIZE - 4);
        this.rowIDKey = raf.readInt();
    }
}
