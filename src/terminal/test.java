package terminal;

import terminal.core.JTerminal;
import terminal.core.JTerminalPrinter;
import terminal.core.theme.Theme;
import terminal.optional.menu.ListMenu;
import terminal.optional.menu.MenuBuilder;
import terminal.optional.menu.ObjectMenu;
import terminal.optional.properties.PropertiesManager;

import java.util.Arrays;
import java.util.List;


public class test{
    /**
     * Creates and runs a JTerminal for demonstration
     * @param args command-line arguments
     */
    public static void main(String[] args){
        JTerminal terminal = new JTerminal("JTerminal");
        PropertiesManager.addPropertiesManager(terminal);

        //Add property tracking light/dark/default theme
        PropertiesManager.addProperty("theme","dark", ()->{
                String themeName = ListMenu.queryMenu(terminal, new MenuBuilder()
                        .setDirection(ListMenu.HORIZONTAL)
                        .buildBasicMenu(terminal, new String[]{"light", "dark", "default"}));
                PropertiesManager.setProperty("theme", themeName);
                if(themeName.equals("light")) terminal.setTheme(Theme.DEFAULT_LIGHT_THEME());
                else if(themeName.equals("dark")) terminal.setTheme(Theme.DEFAULT_DARK_THEME());
                else terminal.setTheme(Theme.DEFAULT_THEME());
        });

        //Read properties from file
        PropertiesManager.readProperties(terminal);

        //Get font size and theme, display version on startup
        terminal.setStartBehavior((JTerminal t)->{
            try{
                int fontSize = Integer.parseInt(PropertiesManager.getProperty("font-size"));
                t.setFontSize(fontSize);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
            if(PropertiesManager.getProperty("theme")!=null){
                String themeName = PropertiesManager.getProperty("theme");
                if(themeName.equals("light")) t.setTheme(Theme.DEFAULT_LIGHT_THEME());
                else if(themeName.equals("dark"))  t.setTheme(Theme.DEFAULT_DARK_THEME());
                else t.setTheme(Theme.DEFAULT_THEME());
            }
            t.out.println("JTerminal v0.1.0", JTerminalPrinter.CENTERED);
            t.out.println("------------------------", JTerminalPrinter.CENTERED);
        });

        //Write properties file on close
        terminal.setCloseBehavior(PropertiesManager::writeProperties);

        terminal.putCommand("menu", ()->{
            List<String> ll = Arrays.asList("1","2","3","4","5","6","7","8","9");

            String s = ListMenu.queryMenu(terminal, new MenuBuilder()
                    .setDirection(ListMenu.HORIZONTAL)
                    .buildObjectMenu(terminal, ll, (String str)-> "Label #"+str));
            terminal.out.println("You selected: "+s);
        },"m");
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

        terminal.start();
    }
}
