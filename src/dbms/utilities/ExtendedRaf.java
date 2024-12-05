package dbms.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class ExtendedRaf extends RandomAccessFile {
    public ExtendedRaf(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public String readString(int numBytes) throws IOException {
        byte[] bytes = new byte[numBytes];
        this.read(bytes, 0, numBytes);

        int i = bytes.length - 1;
        while (i > 0 && bytes[i] == 0) i--;
        return new String(Arrays.copyOf(bytes, i + 1));
    }
}
