package dbms.userInterface.CommandParser;

import dbms.database.constants.CommandType;

import java.util.Arrays;
import java.util.List;

public class CommandParser {
    public static final String[] keyWords = {"EXIT", "SHOW", "CREATE", "TABLE", "INDEX", "DROP", "DELETE", "UPDATE", "SELECT", "WHERE", "IN", "VALUES", "SET", "UNIQUE", "FROM",};
    static List<String> inputtedCommand;

    public CommandParser() {}

    public CommandParser(List<String> inputtedCommand) {
        this.inputtedCommand = inputtedCommand;
    }

    public static boolean isKeyword(String word) {
        return Arrays.stream(keyWords).equals(word);
    }

    public CommandType getCommandType(){
        return CommandTypeParser.parseCommandType(inputtedCommand);
    }
}
