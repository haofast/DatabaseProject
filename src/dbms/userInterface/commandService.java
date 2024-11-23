package dbms.userInterface;

import java.util.List;

public class commandService {

    public static String execute(String command) {
        String cmd = command.toUpperCase();
        List<String> commandArgs = List.of(cmd.split("\\W+"));
        return processCommand(commandArgs);
    }

    public static String processCommand(List<String> commandArgs) {
        String result;

        if (!commandArgs.isEmpty()){
            Command command = new Command(commandArgs);
            result = command.getResult();
        }
        else result = "Please enter a command bro";
        return result;
    }
}
