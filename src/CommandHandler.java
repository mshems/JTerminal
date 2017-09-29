import java.util.Collections;
import java.util.LinkedList;

public class CommandHandler {
    private LinkedList<String> tokens;
    private Terminal parent;

    public CommandHandler(Terminal parent){
        this.tokens = new LinkedList<>();
        this.parent = parent;
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
            default:
                break;
        }
        this.tokens.clear();
        parent.terminalInput.advance();
    }
}
