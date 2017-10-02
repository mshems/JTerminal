import terminal.Terminal;

public class Testing {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.1");

        terminal.putCommand("print", ()->{
            String s = terminal.queryString("Enter a string: ", false);
            terminal.println("You entered: "+s);
        });

        terminal.putCommand("test", ()->{
            terminal.println("test");
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
