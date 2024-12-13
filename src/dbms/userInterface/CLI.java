package dbms.userInterface;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class CLI {
    Scanner ins;
    String input = "";

    public CLI(){
        ins = new Scanner(System.in);
        this.startCLI();
    }

    public void startCLI(){
        while (!isExitCommand()) {
            getInput();
            processInput();
        }
        tearDownDbms();
    }

    public void getInput(){
        System.out.print("kyptonDB> ");
        input = ins.nextLine();
    }

    public boolean isExitCommand(){
        return input.toUpperCase().matches("^\\s*EXIT\\s*[;]");
    }

    public void processInput(){
        List<String> commands = List.of(input.split("/[;]/"));
        Stream<Object> resultMessages = commands.stream().map(commandService::execute);
        resultMessages.forEach(System.out::println);
    }

    public void tearDownDbms(){
        System.out.println("Exiting kypton DBMS");
    }
}
