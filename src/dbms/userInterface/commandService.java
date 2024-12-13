package dbms.userInterface;

import dbms.userInterface.dmlAndDqlCommands.AbstractCommand;

import java.util.List;

public class commandService {

    public static String execute(String command) {
        String cmd = command.toUpperCase();
        return processCommand(cmd);
    }

    public static String processCommand(String commandString) {
        String result;

        if (!commandString.isEmpty()){
            AbstractCommand abstractCommand = new AbstractCommand(commandString);
            result = abstractCommand.getResult();
        }
        else result = "Please enter a command bro";
        return result;
    }
}
