package dbms.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class CsvRaf extends RandomAccessFile {


    public CsvRaf(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public List<String> readLineData() throws IOException {
        String line = this.readLine();
        return (line == null) ? null : new ArrayList<>(Arrays.asList(line.split(",")));
    }

    public void forEachLineData(BiConsumer<List<String>, Integer> fn) throws IOException {
        List<String> lineData;
        int lineIndex = 0;

        while ((lineData = this.readLineData()) != null) {
            fn.accept(lineData, lineIndex);
            lineIndex += 1;
        }
    }
}
