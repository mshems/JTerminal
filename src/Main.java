public class Main {
    private static Terminal terminal;

    public static void main(String[] args){
        terminal = new Terminal("Terminal v0.0.1");
        terminal.start();
    }

    public static void newPerson(){
        terminal.query("First Name? ");
    }
}
