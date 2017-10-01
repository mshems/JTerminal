package terminal;

public class Main {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.1");
        terminal.start();
    }

    public static void number(){
        int n = terminal.queryInteger("Enter an integer: ", false);
        terminal.printBlock(() -> {
            terminal.print("Your integer is: ");
            terminal.print(n);
            terminal.print("Your double is: " + terminal.queryDouble("Enter a double: ", false)+terminal.makeQueryInline());
        });
    }

    public static void bool(){
        boolean b = terminal.queryBoolean("Enter a boolean: ", false);
        terminal.printBlock(() -> {
                terminal.print("Your boolean is: ");
                terminal.print(b);
        });
    }

    public static void newPerson(){
        String name = terminal.queryString("First Name? ", false);
        String name2 = terminal.queryString("Last Name? ", false);
        terminal.printBlock(()-> {
            terminal.printf("Your name is: %s %s", name, name2);
        });
    }
}
