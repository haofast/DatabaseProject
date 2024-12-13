package dbms.userInterface;

import java.util.List;

public class commandService {

    public static String execute(String command) {
        String cmd = command.toUpperCase();
        return processCommand(cmd);
    }

    public static String processCommand(String commandString) {
        String result;

        if (!commandString.isEmpty()){
            Command command = new Command(commandString);
            result = command.getResult();
        }
        else result = "Please enter a command bro";
        return result;
    }
}
