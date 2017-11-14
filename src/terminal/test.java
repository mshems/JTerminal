package terminal;

import terminal.core.JTerminal;
import terminal.optional.menu.ListMenu;
import terminal.optional.menu.MenuBuilder;
import terminal.optional.menu.ObjectMenu;
import terminal.optional.properties.PropertiesManager;

import java.util.LinkedList;

public class test{
    public static void main(String[] args){
        JTerminal terminal = new JTerminal("Terminal");
        PropertiesManager.addPropertyManager(terminal);
        terminal.putCommand("menu", ()->{
            LinkedList<String> ll = new LinkedList<>();
            ll.add("1");
            ll.add("2");
            ll.add("3");
            ll.add("4");
            ll.add("5");
            ll.add("6");
            ll.add("7");
            ll.add("8");
            ll.add("9");
            ll.add("10");
            MenuBuilder mb = new MenuBuilder().setDirection(ListMenu.VERTICAL);
            ObjectMenu<String> m =mb.buildMenu(terminal, ll, (String s)-> "Label #"+s);
            String s = ListMenu.queryMenu(terminal, m);
            terminal.out.println("You selected: "+s);
        });

        terminal.putCommand("quit", ()->{
            if(terminal.hasTokens()){
                if(terminal.nextToken().equals("-f")){
                    terminal.close();
                    System.exit(0);
                }
            } else {
                if(terminal.queryYN("Are you sure? [Y/N] : ")){
                    terminal.close();
                    System.exit(0);
                }
            }

        }, "q");
        terminal.start();
    }
}
