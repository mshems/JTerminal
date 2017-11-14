package terminal.core.behavior;

import terminal.core.JTerminal;

import java.util.Collections;

public class CommandTokenizer {
    private JTerminal terminal;
    private String regex = "\\s+";

    public CommandTokenizer (JTerminal terminal){
        this.terminal = terminal;
    }

    public void tokenize(String command){
        String[] tokens = command
                .trim()
                .split(regex);
        Collections.addAll(terminal.getTokenBuffer(), tokens);
    }

    public CommandTokenizer setRegEx(String regex){
        this.regex = regex;
        return this;
    }
}
