package terminal.core;

public class TerminalPrinter{
    private final Terminal terminal;

    public TerminalPrinter(Terminal terminal){
        this.terminal = terminal;
    }

    public void printf(String format, Object... args) {
        Terminal.getOutputComponent(terminal).print(String.format(format, args));
    }

    public void print(String str) {
        Terminal.getOutputComponent(terminal).print(str);
    }

    public void print(Integer n) {
        Terminal.getOutputComponent(terminal).print(n.toString());
    }

    public void print(Double d) {
        Terminal.getOutputComponent(terminal).print(d.toString());
    }

    public void print(Boolean b) {
        Terminal.getOutputComponent(terminal).print(b.toString());
    }

    public void print(Object o) {
        Terminal.getOutputComponent(terminal).print(o.toString());
    }

    public void println(String str) {
        Terminal.getOutputComponent(terminal).println(str);
    }

    public void println(Integer n) {
        Terminal.getOutputComponent(terminal).println(n.toString());
    }

    public void println(Double d) {
        Terminal.getOutputComponent(terminal).println(d.toString());
    }

    public void println(Boolean b) {
        Terminal.getOutputComponent(terminal).println(b.toString());
    }

    public void println(Object o) {
        Terminal.getOutputComponent(terminal).println(o.toString());
    }

    public void println(String str, int PRINT_FORMAT) {
        switch (PRINT_FORMAT) {
            case Terminal.LEFT_ALIGNED:
                Terminal.getOutputComponent(terminal).print(str);
                break;
            case Terminal.CENTERED:
                Terminal.getOutputComponent(terminal).printCentered(str);
                break;
            case Terminal.RIGHT_ALIGNED:
                Terminal.getOutputComponent(terminal).printRightAligned(str);
                break;
            default:
                Terminal.getOutputComponent(terminal).print(str);
                break;
        }
    }
}
