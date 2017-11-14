package terminal.core.behavior;

import terminal.core.Command;
import terminal.core.JTerminal;
import terminal.core.UnknownCommandException;

public class CommandExecutor {
    private JTerminal terminal;

    public CommandExecutor(JTerminal terminal){
        this.terminal = terminal;
    }

    public void doCommand(String token) throws UnknownCommandException {
        Command command = terminal.getCommand(token);
        if(command != null) {
            command.executeCommand();
        } else {
            throw new UnknownCommandException(token);
        }
        terminal.clearBuffer();
    }
}
