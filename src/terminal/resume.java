package terminal;

import terminal.core.CommandMap;
import terminal.core.JTerminal;
import terminal.core.JTerminalPrinter;
import terminal.core.theme.Theme;
import terminal.optional.menu.ListMenu;
import terminal.optional.menu.MenuFactory;
import terminal.optional.properties.PropertiesManager;
import terminal.optional.theme.ThemeManager;

import java.util.Arrays;
import java.util.List;


public class resume {
    /**
     * Creates and runs a JTerminal for demonstration
     * @param args command-line arguments
     */
    public static void main(String[] args){
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_THEME());
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_LIGHT_THEME());
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_DARK_THEME());

        //Create new JTerminal instance
        JTerminal terminal = new JTerminal("JTerminal");
        //Set new app icon
        terminal.setAppIcon("./res/jterminal-icon.png");
        //Add properties manager
        PropertiesManager.addPropertiesManager(terminal);

        //Add theme property and config command action
        PropertiesManager.addProperty("theme","matrix", ()-> {
            //Theme selection menu
            String themeName = ListMenu.queryMenu(new MenuFactory()
                    .setDirection(ListMenu.VERTICAL)
                    .setTitle("Themes:")
                    .buildObjectMenu(terminal, ThemeManager.themeList, (str) -> str)).returnObject;
            if (themeName == null) return;
            //Set theme property and load selected theme
            PropertiesManager.setProperty("theme", themeName);
            ThemeManager.setTheme(terminal, themeName);
        });

        //Define startup behavior
        terminal.setStartBehavior((t)->{
            //Read properties from file
            PropertiesManager.readProperties(terminal);
            //Get list of themes
            ThemeManager.makeThemesList();
            //Load theme
            ThemeManager.setTheme(terminal, PropertiesManager.getProperty("theme"));

            try{
                //Load font size
                t.setFontSize(Integer.parseInt(PropertiesManager.getProperty("font-size")));
            } catch (NumberFormatException e){
                //Default to font size in theme
                t.setFontSize(terminal.getTheme().font.getSize());
            }

        });

        //Define close behavior
        terminal.setCloseBehavior(PropertiesManager::writeProperties);

//Add commands:
        terminal.putCommand("about", ()->{
                terminal.out.println("About:");
        });

        terminal.putCommand("contact", ()->{
            terminal.out.println("Contact Info:");
        });

        terminal.putCommand("exp", ()->{
            terminal.out.println("Experience:");
        });

        terminal.putCommand("skill", ()->{
            terminal.out.println("Skills:");
        });

        terminal.replaceCommand("clear", ()->{
            terminal.clear();
        });

        //Quit command with options
        terminal.putCommand("quit", ()->{
            if(terminal.hasTokens()){
                String token = terminal.nextToken();
                if(token.equals("-f") || token.equals("--force")){
                    terminal.close();
                    System.exit(0);
                }
            } else {
                if(terminal.queryYN("Are you sure? [Y/N] : ")){
                    terminal.close();
                    System.exit(0);
                }
            }
        },"q");

        /*terminal.putCommand("restart", ()->{
            terminal.close();
            terminal.start();
        });*/

        //Start the terminal
        terminal.start();
    }
}
