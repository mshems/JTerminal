package terminal.properties;

import terminal.core.Terminal;

import java.util.Properties;

public class PropertiesConfigurator{
    public void config(Terminal terminal, Properties properties) {
        switch (terminal.getCommandTokens().pop()) {
            case "font-size":
                if(!terminal.getCommandTokens().isEmpty()) {
                    try {
                        int fontSize = Integer.parseInt(terminal.getCommandTokens().pop());
                        terminal.setFontSize(fontSize);
                        properties.setProperty("font-size", Integer.toString(fontSize));
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        PropertiesManager.writeProperties(terminal);
    }
}
