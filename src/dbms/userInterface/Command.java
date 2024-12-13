package dbms.userInterface;

import dbms.constants.CommandType;
import dbms.dmlAndDqlCommands.Select;
import dbms.userInterface.CommandParser.CommandParser;

import java.util.List;

public class Command {
    String result = "to be developed dun dun dunnn";
    String args;
    CommandType cmdType;
    CommandParser commandParser;

    public Command(String inputtedCommand){
        CommandParser commandParser = new CommandParser(inputtedCommand);
        cmdType = commandParser.getCommandType();

        switch (cmdType){
            case CommandType.SELECT -> new Select(inputtedCommand);
        }

    }

//    public Command(CommandType cmdType, List<String> args){
//        this.args = args;
//        this.cmdType = cmdType;
//    }

    public String getResult(){ return this.result;}
}
