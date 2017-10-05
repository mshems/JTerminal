package terminal;

import ui.ListMenu;

import java.util.LinkedHashMap;

public class Testing {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.2", true);

        terminal.putCommand("print", ()->{
            String s = terminal.queryString("Enter a string: ", false);
            terminal.println("You entered: "+s);
        });

        terminal.putCommand("align", ()->{
            terminal.println("CENTERED TEXT",Terminal.CENTERED);
            terminal.println("RIGHT ALIGNED TEXT",Terminal.RIGHT_ALIGNED);
        });

        terminal.putCommand("test", ()->{
            number();
            bool();
            newPerson();
        });

        terminal.putCommand("menu", ()->{
            menu();
            terminal.queryStringListMenu(new String[]{"key1","key2","key3","key4"}, ListMenu.HORIZONTAL);
        });

        terminal.start();
    }

    public static void menu(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        terminal.queryObjectListMenu(map, ListMenu.VERTICAL);
    }

    public static void number(){
        int n = terminal.queryInteger("Enter an integer: ", false);
        terminal.print("Your integer is: ");
        terminal.println(n);
        terminal.println("Your double is: " + terminal.queryDouble("Enter a double: ", false));
    }

    public static void bool(){
        boolean b = terminal.queryBoolean("Enter a boolean: ", false);
        terminal.print("Your boolean is: ");
        terminal.println(b);
    }

    public static void newPerson(){
        String name = terminal.queryString("First Name? ", false);
        String name2 = terminal.queryString("Last Name? ", false);
        terminal.printf("Your name is: %s %s\n", name, name2);
    }
}
