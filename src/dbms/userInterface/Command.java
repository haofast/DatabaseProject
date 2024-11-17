package dbms.userInterface;

import dbms.constants.CommandType;
import java.util.List;

public class Command {
    String result = "to be developed dun dun dunnn";
    List<String> args;
    CommandType cmdType;

    public Command(CommandType cmdType, List<String> args){
        this.args = args;
        this.cmdType = cmdType;
    }

    public String getResult(){ return this.result;}
}
