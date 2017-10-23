package terminal;

import terminal.core.JTerminal;
import terminal.menu.ListMenu;
import terminal.menu.MenuBuilder;
import terminal.menu.ObjectMenu;
import terminal.properties.PropertiesManager;

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
            MenuBuilder mb = new MenuBuilder().setDirection(ListMenu.VERTICAL);
            ObjectMenu<String> m =mb.buildMenu(terminal, ll, (String s)-> "Label #"+s);
            String s = ListMenu.queryMenu(terminal, m);
            terminal.out.println("You selected: "+s);
        });

        terminal.putCommand("quit", ()->{
            terminal.close();
            System.exit(0);
        });
        terminal.start();
    }
}
