package terminal;

import java.util.Collections;
import java.util.LinkedList;

public class CommandHandler {
    private LinkedList<String> tokens;
    private Terminal terminal;

    public CommandHandler(Terminal term){
        this.tokens = new LinkedList<>();
        this.terminal = term;
    }

    public void processCommand(String command){
        String[] input = command
                .trim()
                .split("\\s+");
        Collections.addAll(tokens, input);
        this.doCommand(tokens.peek());
    }

    private void doCommand(String token){
        switch(token){
            case "new":
                Main.newPerson();
                break;
            case "test":
                Main.number();
                Main.bool();
                Main.newPerson();
                break;
            case "num":
                Main.number();
                break;
            case "bool":
                Main.bool();
                break;
            case "quit":
                if(terminal.queryYN("Are you sure? [Y/N] ")) {
                    System.exit(0);
                }
                break;
            case "fq":
                System.exit(0);
                break;
            default:
                terminal.printBlock(() -> terminal.print("You entered: '" + token + "'"));
                break;
        }
        this.tokens.clear();
        terminal.advance();
    }
}
