package terminal.optional.properties;

import terminal.core.JTerminal;

import java.util.Properties;

public class PropertiesConfigurator{
    public void config(JTerminal terminal, Properties properties) {
        if(terminal.getTokenBuffer().isEmpty()) return;
        switch (terminal.getTokenBuffer().pop()) {
            case "font-size":
                if(terminal.getTokenBuffer().isEmpty())return;
                try {
                    int fontSize = Integer.parseInt(terminal.getTokenBuffer().pop());
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
