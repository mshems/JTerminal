package terminal;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommandMap extends LinkedHashMap<String,TerminalCommand> {

    public String commandList(){
        String s= "Commands:";
        for(String key:this.keySet()) {
            s += "\n" + key.toString();
        }
        return s;
    }

}
