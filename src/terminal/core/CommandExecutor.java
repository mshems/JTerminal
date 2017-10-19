package terminal.core;

public class CommandExecutor {
    public void doCommand(JTerminal terminal, String token) throws UnknownCommandException{
        Command command = terminal.getCommand(token);
        if(command != null) {
            terminal.newLine();
            command.executeCommand();
        } else {
            throw new UnknownCommandException(token);
        }
        terminal.clearTokens();
    }
}
