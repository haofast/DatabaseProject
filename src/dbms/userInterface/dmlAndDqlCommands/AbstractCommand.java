package dbms.userInterface.dmlAndDqlCommands;

import dbms.database.constants.CommandType;
import dbms.dmlAndDqlCommands.Select;
import dbms.userInterface.CommandParser.CommandParser;

import java.util.List;

public class AbstractCommand {
    String result = "to be developed dun dun dunnn";
    String args;
    CommandType cmdType;
    CommandParser commandParser;

    public AbstractCommand(String inputtedCommand){
        CommandParser commandParser = new CommandParser(inputtedCommand);
        cmdType = commandParser.getCommandType();

        switch (cmdType){
            case CommandType.SELECT -> new Select(inputtedCommand);
        }

    public AbstractCommand(CommandType cmdType, List<String> args){
        this.args = args;
        this.cmdType = cmdType;
    }

//    public Command(CommandType cmdType, List<String> args){
//        this.args = args;
//        this.cmdType = cmdType;
//    }

    public String handleQuery(){
        return "";
    }

    public String getResult(){ return this.result;}
}
