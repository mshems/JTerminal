package terminal.optional.properties;

import terminal.core.CommandAction;
import terminal.core.CommandMap;
import terminal.core.JTerminal;
import terminal.optional.menu.ListMenu;
import terminal.optional.menu.MenuFactory;
import terminal.optional.menu.MenuReturnObject;

import java.util.Properties;

public class PropertiesConfigCommand {
    private CommandMap configCommandMap;

    PropertiesConfigCommand(){
        configCommandMap = new CommandMap();
        //configCommandMap.put("cancel", ()->{});
    }

    void addCustomBehavior(String propertyName, CommandAction commandAction){
        configCommandMap.put(propertyName, commandAction);
    }

    void removeCustomBehavior(String propertyName){
        configCommandMap.remove(propertyName);
    }

    void config(JTerminal terminal, Properties properties) {
        if (terminal.hasTokens()) {
            String propertyName = terminal.nextToken();
            if (configCommandMap.get(propertyName) != null) {
                configCommandMap.get(propertyName).executeCommand();
            } else {
                if (properties.getProperty(propertyName) != null && terminal.hasTokens()) {
                    properties.setProperty(propertyName, terminal.nextToken());
                }
            }
            PropertiesManager.writeProperties(terminal);
        } else {
            MenuReturnObject<CommandAction> m = ListMenu.queryMenu(
                    new MenuFactory()
                    .setDirection(ListMenu.VERTICAL)
                    .buildActionMenu(terminal, configCommandMap));
            if(m!=null && m.returnObject!=null){
                m.returnObject.executeCommand();
            }
            //terminal.out.println("No property specified");
        }
    }
}
