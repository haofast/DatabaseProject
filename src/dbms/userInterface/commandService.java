package dbms.userInterface;

import dbms.userInterface.dmlAndDqlCommands.AbstractCommand;

import java.io.IOException;
import java.util.List;

public class commandService {

    public static String execute(String command) {
        String cmd = command.toUpperCase();
        return processCommand(cmd);
    }

    public static String processCommand(String commandString) {
        String result;

        try {
            if (!commandString.isEmpty()){
                AbstractCommand abstractCommand = new AbstractCommand(commandString);
                result = abstractCommand.getResult();
            }
            else result = "Please enter a command bro";
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
