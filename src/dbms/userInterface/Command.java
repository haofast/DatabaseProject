package dbms.userInterface;

import dbms.constants.CommandType;
import dbms.userInterface.CommandParser.CommandParser;

import java.util.List;

public class Command {
    String result = "to be developed dun dun dunnn";
    List<String> args;
    CommandType cmdType;
    CommandParser commandParser;

    public Command(List<String> inputtedCommand){
        CommandParser commandParser = new CommandParser(inputtedCommand);
        cmdType = commandParser.getCommandType();
    }

    public Command(CommandType cmdType, List<String> args){
        this.args = args;
        this.cmdType = cmdType;
    }

    public String getResult(){ return this.result;}
}
