package dbms.userInterface.dmlAndDqlCommands;

import dbms.database.constants.CommandType;
import dbms.userInterface.ddlCommands.IndexCommands;
import dbms.userInterface.ddlCommands.TableCommands;
import dbms.userInterface.dmlAndDqlCommands.Select;
import dbms.userInterface.CommandParser.CommandParser;

import java.io.IOException;
import java.util.List;

public class AbstractCommand {
    String result = "";
    String args;
    CommandType cmdType;
    CommandParser commandParser;

    public AbstractCommand(String inputtedCommand) throws Exception {
        CommandParser commandParser = new CommandParser(inputtedCommand);
        cmdType = commandParser.getCommandType();

        switch (cmdType) {
            case CommandType.SELECT -> new Select(inputtedCommand);
            case CommandType.SHOW_TABLE -> new TableCommands(inputtedCommand);
            case CommandType.INSERT -> new Insert(inputtedCommand);
            case CommandType.CREATE_INDEX -> new IndexCommands(inputtedCommand);
            case CommandType.CREATE_TABLE -> new TableCommands(inputtedCommand);
            case CommandType.DROP_TABLE -> new TableCommands(inputtedCommand);
            case CommandType.UPDATE -> new Update(inputtedCommand);
            case CommandType.DELETE -> new Delete(inputtedCommand);
        }
    }

    public AbstractCommand(CommandType cmdType, String args) {
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
