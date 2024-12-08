package dbms.userInterface;

import dbms.userInterface.dmlAndDqlCommands.AbstractCommand;

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
            AbstractCommand abstractCommand = new AbstractCommand(commandArgs);
            result = abstractCommand.getResult();
        }
        else result = "Please enter a command bro";
        return result;
    }
}
