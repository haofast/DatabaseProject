package dbms.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ExtendedRaf extends RandomAccessFile {
    public ExtendedRaf(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public String readString(int numBytes) throws IOException {
        byte[] bytes = new byte[numBytes];
        this.read(bytes, 0, numBytes);
        return new String(bytes);
    }
}
