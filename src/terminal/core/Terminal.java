package terminal.core;

import terminal.menus.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")
public class Terminal implements TerminalEventListener {
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalIOComponent inputComponent;
    private TerminalIOComponent outputComponent;
    private JFrame frame;
    private JScrollPane scrollPane;
    private LinkedBlockingQueue<String> commandQueue;
    private LinkedList<String> commandTokens;
    private CommandMap commandMap;
    private CommandExecutor commandExecutor;
    private CommandTokenizer commandTokenizer;
    private Properties properties;

    public final TerminalPrinter out;

    public static final int LEFT_ALIGNED = 0;
    public static final int CENTERED = 1;
    public static final int RIGHT_ALIGNED = 2;


    public Terminal(String title) {
        out = new TerminalPrinter(this);
        commandQueue = new LinkedBlockingQueue<>();
        commandTokens = new LinkedList<>();
        commandMap = new CommandMap();
        commandExecutor = new CommandExecutor();
        commandTokenizer = new CommandTokenizer();
        properties = new Properties();
        addDefaultCommands();
        initUI(title);
        PropertyHandler.initProperties(this);
    }

    private void initUI(String title) {
        frame = new JFrame(title);
        frame.setMinimumSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        inputComponent = new TerminalIOComponent(true);
        inputComponent.setTerminalEventListener(this);
        outputComponent = inputComponent;

        scrollPane = new JScrollPane(outputComponent);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }

    public synchronized void start() {
        frame.setVisible(true);
        inputComponent.start();
        while (true) {
            try {
                wait();
                if (!commandQueue.isEmpty()) {
                    commandTokenizer.tokenize(this, commandQueue.take());
                    commandExecutor.doCommand(this, commandTokens.peek());
                }
                inputComponent.advance();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                break;
            }
        }
    }

    public void close(){
        PropertyHandler.writeProperties(this);
        frame.dispose();
    }

    public synchronized String next(){
        String input = "";
        inputComponent.setQuerying(true);
        inputComponent.setCaretPosition(inputComponent.getText().length());
        int position = inputComponent.getCaretPosition();
        TerminalIOComponent.lockLeftArrow(inputComponent, position);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getText().substring(position, inputComponent.getText().length());
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }
        TerminalIOComponent.unlockLeftArrow(inputComponent);
        newLine();
        return input.trim();
    }

    public Integer nextInt(){
        try{
            return Integer.parseInt(this.next());
        } catch (NumberFormatException e){
            return null;
        }
    }
    public Double nextDouble(){
        try{
            return Double.parseDouble(this.next());
        } catch (NumberFormatException e){
            return null;
        }
    }

    public Boolean nextBoolean(){
        return Boolean.parseBoolean(this.next());
    }

    private synchronized String query(String queryPrompt) {
        String input = "";
        inputComponent.setPrompt(queryPrompt);
        inputComponent.advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getInput();
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }
        inputComponent.resetPrompt();
        newLine();
        return input.trim();
    }

    public String queryString(String queryPrompt, boolean allowEmptyString) {
        while (true) {
            String input = query(queryPrompt);
            if (input.isEmpty() && allowEmptyString) {
                return input;
            } else if (!input.isEmpty()) {
                return input;
            }
            out.println("Empty input not allowed");
        }
    }

    public boolean queryYN(String queryPrompt) {
        switch (query(queryPrompt).toLowerCase()) {
            case "y":
            case "yes":
                return true;
            default:
                return false;
        }
    }

    public Integer queryInteger(String queryPrompt, boolean allowNull) {
        while (true) {
            try {
                return Integer.parseInt(query(queryPrompt));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                out.println("Not an integer value");
            }
        }
        return null;
    }

    public Double queryDouble(String queryPrompt, boolean allowNull) {
        while (true) {
            try {
                return Double.parseDouble(query(queryPrompt));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                out.println("Not a double value");
            }
        }
        return null;
    }

    public Boolean queryBoolean(String queryPrompt, boolean allowNull) {
        while (true) {
            switch (query(queryPrompt).toLowerCase()) {
                case "t":
                case "true":
                    return true;
                case "f":
                case "false":
                    return false;
                default:
                    if (allowNull) {
                        return null;
                    }
                    out.println("Not a boolean value");
            }
        }
    }

    private synchronized <E> E queryMenu(ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        inputComponent.removeKeyListener(inputComponent.getKeyListeners()[0]);
        inputComponent.addKeyListener(menuKeyListener);
        inputComponent.unmapArrows();
        scrollPane.setViewportView(menu);
        scrollPane.getViewport().setViewPosition(new Point(0,inputComponent.getHeight()));
        menu.addKeyListener(menuKeyListener);
        menu.requestFocusInWindow();
        synchronized (this) {
            try{
                this.wait();
                obj = menu.getSelectedItem();
            }catch (InterruptedException ex){
                //ex.printStackTrace();
            }
        }
        frame.remove(menu);
        inputComponent.removeKeyListener(menuKeyListener);
        //inputComponent.addTerminalKeyListener();
        inputComponent.addKeyListener(new TerminalKeylistener(inputComponent));
        inputComponent.remapArrows();
        scrollPane.setViewportView(outputComponent);
        outputComponent.requestFocusInWindow();
        return obj;
    }

    public synchronized <E> E queryObjectListMenu(Map<String, E> map, int direction){
        ObjectListMenu<E> menu = new ObjectListMenu<E>(this, map, direction);
        return queryMenu(menu);
    }
    public synchronized String queryStringListMenu(String[] strings, int direction){
        StringListMenu menu = new StringListMenu(this, strings , direction);
        return (String) queryMenu(menu);
    }
    void newLine() {
        inputComponent.newLine();
    }

    void clear(){
        outputComponent.clear();
    }

    public void addDefaultCommands() {
        commandMap.put("", () ->{});
        commandMap.put("clear", this::clear);
        commandMap.put("terminal-config", this::config);
    }

    public void removeDefaultCommands() {
        commandMap.remove("");
        commandMap.remove("clear");
        commandMap.remove("terminal-config");
    }

    private void config() {
        commandTokens.pop();
        switch (commandTokens.pop()) {
            case "font-size":
                if(!commandTokens.isEmpty()) {
                    try {
                        int fontSize = Integer.parseInt(commandTokens.pop());
                        setFontSize(fontSize);
                        properties.setProperty("font-size", Integer.toString(fontSize));
                    } catch (NumberFormatException e){
                        //e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        PropertyHandler.writeProperties(this);
    }

    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.getActionCommand());
            inputComponent.updateHistory(e.getActionCommand());
        } catch (InterruptedException ex) {
            //ex.printStackTrace();
        }
    }

    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }

    public synchronized void menuActionPerformed(MenuEvent e) {
        this.notifyAll();
    }



    public void putCommand(String key, TerminalCommand command) {
        commandMap.put(key, command);
    }

    public void replaceCommand(String key, TerminalCommand command) {
        commandMap.replace(key, command);
    }

    public void removeCommand(String key, TerminalCommand command) {
        commandMap.remove(key, command);
    }

    public void removeCommand(String key) {
        commandMap.remove(key);
    }

    public TerminalCommand getCommand(String key) {
        return this.commandMap.get(key);
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

    public CommandExecutor getCommandExecutor(){
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public CommandTokenizer getCommandTokenizer(){
        return commandTokenizer;
    }

    public void setCommandTokenizer(CommandTokenizer commandTokenizer){
        this.commandTokenizer = commandTokenizer;
    }

    public static TerminalIOComponent getInputComponent(Terminal terminal) {
        return terminal.inputComponent;
    }

    public static TerminalIOComponent getOutputComponent(Terminal terminal) {
        return terminal.outputComponent;
    }

    public String getDefaultPrompt(){
        return inputComponent.getDefaultPrompt();
    }

    public void setDefaultPrompt(String prompt) {
        inputComponent.setDefaultPrompt(prompt);
    }

    public String getPrompt(String prompt) {
        return inputComponent.getPrompt();
    }

    public void setPrompt(String prompt) {
        inputComponent.setPrompt(prompt);
    }

    LinkedList<String> getCommandTokens() {
        return commandTokens;
    }

    Properties getProperties() {
        return this.properties;
    }

    public int getFontSize() {
        return outputComponent.getFontSize();

    }

    void setFontSize(int fontSize) {
        this.inputComponent.setFontSize(fontSize);
        this.outputComponent.setFontSize(fontSize);
    }
}