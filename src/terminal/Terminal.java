package terminal;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener, TerminalInterface{
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalInputComponent inputComponent;
    private JFrame frame;
    private CommandHandler commandHandler;
    private LinkedBlockingQueue<String> commandQueue;

    private Color backgroundColor;
    private Color foregroundColor;
    private Color caretColor;
    private Font textFont;

    public Terminal(String title){
        commandHandler = new CommandHandler(this);
        commandQueue = new LinkedBlockingQueue<>();
        initFrame(title);
    }

    @Override
    public void initFrame(String title){
        frame = new JFrame(title);
        frame.setSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        inputComponent = new TerminalInputComponent(true);
        inputComponent.setTerminalEventListener(this);
        JScrollPane scrollPane = new JScrollPane(inputComponent);
        frame.add(scrollPane);
    }

    @Override
    public synchronized void start(){
        frame.setVisible(true);
        inputComponent.start();
        while(true) {
             try {
                 wait();
                 if (!commandQueue.isEmpty()) {
                     commandHandler.processCommand(commandQueue.take());
                     advance();
                 }
             } catch (InterruptedException e) {
                //e.printStackTrace();
             }
         }
    }

    @Override
    public synchronized String query(String queryPrompt){
        String s="";
        inputComponent.setCurrPrompt(queryPrompt);
        advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try{
                this.wait();
                s = inputComponent.getCommand();
                inputComponent.resetPrompt();
            }catch (InterruptedException ex){
                //ex.printStackTrace();
            }
        }
        return s.trim();
    }

    @Override
    public String queryString(String query, boolean allowEmptyString){
        while(true) {
            String s = query(query);
            if (s.isEmpty() && allowEmptyString) {
                return s;
            } else if (!s.isEmpty()){
                return s;
            }
            println("Empty input not allowed");
        }
    }

    @Override
    public boolean queryYN(String query){
        switch(query(query).toLowerCase()){
            case "y":
            case "yes":
                return true;
            default:
                return false;
        }
    }

    @Override
    public Integer queryInteger(String query, boolean allowNull){
        while(true) {
            try {
                return Integer.parseInt(query(query));
            } catch (NumberFormatException e) {
                if(allowNull){
                    break;
                }
                println("Not an integer value");
            }
        }
        return null;
    }

    @Override
    public Double queryDouble(String query, boolean allowNull) {
        while (true) {
            try {
                return Double.parseDouble(query(query));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                println("Not a double value");
            }
        }
        return null;
    }

    @Override
    public Boolean queryBoolean(String query, boolean allowNull){
        while(true) {
            switch (query(query).toLowerCase()){
                case "t":
                case "true":
                    return true;
                case "f":
                case "false":
                    return false;
                default:
                    if(allowNull){
                        return null;
                    }
                    println("Not a boolean value");
            }
        }
    }

    @Override
    public void advance(){
        inputComponent.advance();
    }

    @Override
    public void clear(){
        inputComponent.clear();
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.commandString);
            inputComponent.updateHistory(e.commandString);
            newLine();
        }catch (InterruptedException ex){
            //ex.printStackTrace();
        }
    }

    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        newLine();
        this.notifyAll();
    }

    @Override
    public void newLine(){
        inputComponent.newLine();
    }

    @Override
    public void printf(String format, Object... args){
        inputComponent.print(String.format(format, args));
    }
    @Override
    public void print(String s){
        inputComponent.print(s);
    }

    public void print(Integer n){
        inputComponent.print(n.toString());
    }
    public void print(Double d){
        inputComponent.print(d.toString());
    }
    public void print(Boolean b){
        inputComponent.print(b.toString());
    }
    public void println(String s){
        inputComponent.println(s);
    }
    public void println(Integer n){
        inputComponent.println(n.toString());
    }
    public void println(Double d){
        inputComponent.println(d.toString());
    }
    public void println(Boolean b){
        inputComponent.println(b.toString());
    }
}
