package dbms.exceptions;

import dbms.table.Header;

import java.util.List;

public class InvalidValueCountException extends RuntimeException {
    public InvalidValueCountException(Header header, List<String> values) {
        super(new StringBuilder().append("\n")
            .append("Reason: The number of values supplied did not match the number of columns.\n")
            .append("Columns: ").append(header.getColumns()).append("\n")
            .append("Values: ").append(values).append("\n")
            .toString()
        );
    }
}
