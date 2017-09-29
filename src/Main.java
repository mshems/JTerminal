public class Main {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.1");
        terminal.start();
    }

    public synchronized static void newPerson(){
        String name = terminal.query("First Name? ");
        System.out.println(name);
    }
}
