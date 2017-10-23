package terminal.properties;

import terminal.core.JTerminal;

import java.util.Properties;

public class PropertiesConfigurator{
    public void config(JTerminal terminal, Properties properties) {
        if(terminal.getCommandTokens().isEmpty()) return;
        switch (terminal.getCommandTokens().pop()) {
            case "font-size":
                if(terminal.getCommandTokens().isEmpty())return;
                try {
                    int fontSize = Integer.parseInt(terminal.getCommandTokens().pop());
                    terminal.setFontSize(fontSize);
                    properties.setProperty("font-size", Integer.toString(fontSize));
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        PropertiesManager.writeProperties(terminal);
    }
}
