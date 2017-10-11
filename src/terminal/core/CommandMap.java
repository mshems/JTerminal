package terminal.core;

import java.util.LinkedHashMap;

public class CommandMap extends LinkedHashMap<String,Command> {
    public LinkedHashMap<Command, String > descriptions;

    public CommandMap(){
        this.descriptions = new LinkedHashMap<>();
    }

    public void put(String key, String description, Command command){
        this.put(key, command);
        this.descriptions.put(command, description);
    }

    //TODO
    public String helpMenu(){
        String helpMenu = "";

        for(String key:this.keySet()) {

        }
        return helpMenu;
    }
}
