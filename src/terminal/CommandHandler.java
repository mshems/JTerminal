package terminal;

import java.util.Collections;
import java.util.HashMap;

public class CommandHandler {
    private Terminal terminal;
    private CommandMap commandMap;

    public CommandHandler(Terminal term){
        terminal = term;
        commandMap = new CommandMap();
        commandMap.put("", ()->{});
        //commandMap.put("help", ()->terminal.println(commandMap.commandList()));
    }

    public void putCommand(String key, TerminalCommand command){
        commandMap.put(key, command);
    }

    public void processCommand(String command){
        String[] input = command
                .trim()
                .split("\\s+");
        Collections.addAll(terminal.getCommandTokens(), input);
        this.doCommand(terminal.getCommandTokens().peek());
    }

    private void doCommand(String token){
        TerminalCommand command = commandMap.get(token);
        if(command != null) {
            command.executeCommand();
        } else {
            terminal.println("Command '"+token+"' not found");
        }
        this.terminal.getCommandTokens().clear();
    }


}
