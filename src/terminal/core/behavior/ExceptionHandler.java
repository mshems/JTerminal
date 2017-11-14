package terminal.core.behavior;

import terminal.core.JTerminal;
import terminal.core.UnknownCommandException;

public class ExceptionHandler {
    private JTerminal terminal;

    public ExceptionHandler(JTerminal term){
        this.terminal = term;
    }

    public void handleException(UnknownCommandException e){
        terminal.out.println("Unknown command: \""+e.getCommand()+"\"");
    }
}
