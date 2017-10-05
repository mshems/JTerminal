import terminal.Terminal;
import ui.ObjectListMenu;

import java.util.LinkedHashMap;

public class Testing {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.1", true);

        terminal.putCommand("print", ()->{
            String s = terminal.queryString("Enter a string: ", false);
            terminal.println("You entered: "+s);
        });

        terminal.putCommand("test", ()->{
            terminal.println("CENTERED TEXT",Terminal.CENTERED);
            terminal.println("RIGHT ALIGNED TEXT",Terminal.RIGHT_ALIGNED);
        });

        terminal.putCommand("menu", ()->{
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            map.put("key3", "value3");
            map.put("key4", "value4");
            terminal.queryObjectListMenu(map, ObjectListMenu.HORIZONTAL);
            terminal.queryStringListMenu(new String[]{"key1","key2","key3","key4"}, ObjectListMenu.VERTICAL);
        });

        terminal.putCommand("list", ()->{
            String str = terminal.queryFromList("[ a | b | c | cancel ] : ", new String[]{"1", "2", "3","cancel"}, false, true);
            if(!str.equalsIgnoreCase("cancel")) {
                terminal.println("Your choice: " + str);
            }
        });
        terminal.start();
    }

    public static void number(){
        int n = terminal.queryInteger("Enter an integer: ", false);
        terminal.print("Your integer is: ");
        terminal.println(n);
        terminal.print("Your double is: " + terminal.queryDouble("Enter a double: ", false));
    }

    public static void bool(){
        boolean b = terminal.queryBoolean("Enter a boolean: ", false);
        terminal.print("Your boolean is: ");
        terminal.print(b);
    }

    public static void newPerson(){
        String name = terminal.queryString("First Name? ", false);
        String name2 = terminal.queryString("Last Name? ", false);
        terminal.printf("Your name is: %s %s", name, name2);
    }
}
