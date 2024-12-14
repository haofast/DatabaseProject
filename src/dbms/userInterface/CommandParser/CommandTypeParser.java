package dbms.userInterface.CommandParser;

import dbms.database.constants.CommandType;

import java.util.List;

public class CommandTypeParser extends CommandParser {


    public static CommandType parseCommandType(List<String> inputtedCommand) {
        CommandType commandType;

        String firstKey = inputtedCommand.get(0);
        String secondKey = inputtedCommand.get(1);

        if (isKeyword(firstKey) && isKeyword(secondKey)){
            commandType = getCommandTypeEnum(firstKey, secondKey);
        } else {
            commandType = getCommandTypeEnum(firstKey);
        }

        return commandType;
    }

    static CommandType getCommandTypeEnum(String arg) {
        CommandType commandType = CommandType.INVALID;

        if (arg.compareTo("INSERT") == 0) {
            commandType = CommandType.INSERT;

        } else if (arg.compareTo("DELETE") == 0) {
            commandType = CommandType.DELETE;

        } else if (arg.compareTo("UPDATE") == 0) {
            commandType = CommandType.UPDATE;
        }

        return commandType;
    }

    private static CommandType getCommandTypeEnum(String arg1, String arg2) {
        CommandType commandType = CommandType.INVALID;

        if (arg1.compareTo("CREATE") + arg2.compareTo("TABLE") == 0) {
            commandType = CommandType.CREATE_TABLE;

        } else if (arg1.compareTo("CREATE") + arg2.compareTo("INDEX") == 0) {
            commandType = CommandType.CREATE_INDEX;

        } else if (arg1.compareTo("DROP") + arg2.compareTo("TABLE") == 0) {
            commandType = CommandType.DROP_TABLE;

        } else if (arg1.compareTo("DROP") + arg2.compareTo("INDEX") == 0) {
            commandType = CommandType.DROP_INDEX;

        } else if (arg1.compareTo("SELECT") + arg2.compareTo("FROM") == 0) {
            commandType = CommandType.SELECT;

        } else if (arg1.compareTo("SHOW") + arg2.compareTo("TABLES") == 0) {
            commandType = CommandType.SHOW_TABLE;
        }

        return commandType;
    }
}
