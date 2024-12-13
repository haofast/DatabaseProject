package dbms.database.index.page;

import dbms.utilities.ExtendedRaf;

import java.io.IOException;

public abstract class indexPage {
    public static final int NULL_OFFSET_VALUE = -1;
    public static final int PAGE_SIZE = 4096;

    protected int rightNeighborPageNumber = NULL_OFFSET_VALUE;
    protected final int fileOffset;

    protected indexPage(int fileOffset) {
        this.fileOffset = fileOffset;
    }

    protected void zeroOutEntirePage(ExtendedRaf raf) throws IOException {
        raf.seek(this.fileOffset);
        raf.write(new byte[PAGE_SIZE]);
    }

    public int getAbsoluteOffset() {
        return this.fileOffset;
    }
}
