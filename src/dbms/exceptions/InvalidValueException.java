package dbms.exceptions;

import dbms.table.Column;

public class InvalidValueException extends Exception {
    public InvalidValueException(String reason, String value, Column column) {
        super(new StringBuilder().append("\n")
            .append("Reason: ").append(reason).append('\n')
            .append("Value: ").append(value).append('\n')
            .append("Column: ").append(column).append("\n")
            .toString()
        );
    }
}
