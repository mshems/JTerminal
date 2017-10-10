package terminal.core;

import java.util.Collections;

public class CommandTokenizer {
    public void tokenize(Terminal terminal, String command){
        String[] tokens = command
                .trim()
                .split("\\s+");
        Collections.addAll(terminal.getCommandTokens(), tokens);
    }
}
