package dbms.userInterface;
import dbms.constants.CommandType;
import java.util.List;

public class commandService {

    public static String execute(String command){
        String cmd = command.toUpperCase();
        List<String> commandArgs = List.of(cmd.split("\\W+"));
        if(cmd.length() < 2){
            return "invalid syntax";
        }
        return processCommand(commandArgs);
    }

    public static String processCommand(List<String> commandArgs){
        CommandType commandType = CommandType.EXIT;
        String firstKey = commandArgs.get(0);
        String secondKey = commandArgs.get(1);
        if(firstKey.compareTo("SHOW") == 0){  }
        else if (firstKey.compareTo("CREATE") + secondKey.compareTo("TABLE") == 0) {
            commandType = CommandType.CREATE_TABLE;
        }
        else if (firstKey.compareTo("CREATE") + secondKey.compareTo("INDEX") == 0) {
            commandType = CommandType.CREATE_INDEX;
        }
        else if (firstKey.compareTo("DROP") + secondKey.compareTo("TABLE") == 0) {
            commandType = CommandType.DROP_TABLE;
        }
        else if (firstKey.compareTo("DROP") + secondKey.compareTo("INDEX") == 0) {
            commandType = CommandType.DROP_INDEX;
        }
        else if (firstKey.compareTo("INSERT") == 0) {commandType = CommandType.INSERT;}
        else if (firstKey.compareTo("DELETE") == 0) {commandType = CommandType.DELETE;}
        else if (firstKey.compareTo("UPDATE") == 0) {commandType = CommandType.UPDATE;}
        else if (firstKey.compareTo("SELECT") == 0) {commandType = CommandType.SELECT;}
        else {
            return "Invalid Syntax "+firstKey;
        }

        Command command = new Command(commandType, commandArgs);
        return command.getResult();
    }
}
