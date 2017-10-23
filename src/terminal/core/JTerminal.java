package terminal.core;

import terminal.properties.PropertiesManager;
import terminal.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")
public class JTerminal implements TerminalEventListener {
    private JFrame frame;
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalIOComponent inputComponent;
    private TerminalIOComponent outputComponent;
    private JScrollPane scrollPane;
    private JTerminalTheme theme;
    private LinkedBlockingQueue<String> commandQueue;
    private LinkedList<String> commandTokens;
    private CommandMap commandMap;
    private CommandExecutor commandExecutor;
    private CommandTokenizer commandTokenizer;
    public final TerminalPrinter out;

    public JTerminal(String title) {
        out = new TerminalPrinter(this);
        commandQueue = new LinkedBlockingQueue<>();
        commandTokens = new LinkedList<>();
        commandMap = new CommandMap();
        commandExecutor = new CommandExecutor();
        commandTokenizer = new CommandTokenizer();
        theme = new JTerminalTheme("default-dark");
        addDefaultCommands();
        initUI(title);
    }

    private void initUI(String title) {
        frame = new JFrame(title);
        frame.setPreferredSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        inputComponent = new TerminalIOComponent(this,true);
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
                    try {
                        commandExecutor.doCommand(this, commandTokens.pop());
                    } catch (UnknownCommandException e){
                        newLine();
                        out.println("Unknown command: \""+e.getCommand()+"\"");
                    }
                }
                inputComponent.advance();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void close(){
        PropertiesManager.writeProperties(this);
        frame.dispose();
    }

    public String nextToken(){
        if(!commandTokens.isEmpty()){
            return commandTokens.pop();
        } else {
            return null;
        }
    }

    public Integer nextIntToken() throws IllegalTokenException{
        String token = nextToken();
        try{
            return Integer.parseInt(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    public Double nextDoubleToken() throws  IllegalTokenException{
        String token = nextToken();
        try{
            return Double.parseDouble(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    public Boolean nextBooleanToken(){
        return Boolean.parseBoolean(nextToken());
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
                ex.printStackTrace();
            }
        }
        TerminalIOComponent.unlockLeftArrow(inputComponent);
        newLine();
        return input.trim();
    }

    public Integer nextInt() throws NumberFormatException{
        return Integer.parseInt(this.next());
    }

    public Double nextDouble() throws NumberFormatException{
        return Double.parseDouble(this.next());
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
                ex.printStackTrace();
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
            out.println(Strings.ERROR_EMPTY);
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
                out.println(Strings.ERROR_INTEGER);
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
                out.println(Strings.ERROR_DOUBLE);
            }
        }
        return null;
    }

    public Boolean queryBoolean(String queryPrompt){
        return Boolean.parseBoolean(query(queryPrompt));
    }

    public Boolean queryBoolean(String queryPrompt, boolean matchTFOnly, boolean allowNull) {
        while (true) {
            switch (query(queryPrompt).toLowerCase()) {
                case Strings.MATCH_TRUE:
                case Strings.MATCH_TRUE_SHORT:
                    return true;
                case Strings.MATCH_FALSE:
                case Strings.MATCH_FALSE_SHORT:
                    return false;
                default:
                    if (allowNull && !matchTFOnly){
                        return null;
                    } else if(matchTFOnly){
                        out.println(Strings.ERROR_BOOL);
                    } else {
                        return false;
                    }
            }
        }
    }

    public boolean queryYN(String queryPrompt) {
        switch (query(queryPrompt).toLowerCase()) {
            case Strings.MATCH_YES:
            case Strings.MATCH_YES_SHORT:
                return true;
            default:
                return false;
        }
    }

    void newLine() {
        inputComponent.newLine();
    }

    public void clear(){
        outputComponent.clear();
    }

    public void clearTokens(){
        this.commandTokens.clear();
    }

    public void addDefaultCommands() {
        commandMap.put("", () ->{});
        commandMap.put(Strings.COMMAND_CLEAR, this::clear);
    }

    public void removeDefaultCommands() {
        commandMap.remove("");
        commandMap.remove(Strings.COMMAND_CLEAR);
    }

    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.getActionCommand());
            inputComponent.updateHistory(e.getActionCommand());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }

    public void putCommand(String key, Command command) {
        commandMap.put(key, command);
    }

    public void replaceCommand(String key, Command command) {
        commandMap.replace(key, command);
    }

    public void removeCommand(String key, Command command) {
        commandMap.remove(key, command);
    }

    public void removeCommand(String key) {
        commandMap.remove(key);
    }

    public Command getCommand(String key) {
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

    public TerminalIOComponent getInputComponent() {
        return inputComponent;
    }

    public TerminalIOComponent getOutputComponent() {
        return outputComponent;
    }

    public JFrame getFrame(){
        return frame;
    }

    public JScrollPane getScrollPane(){
        return scrollPane;
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

    public LinkedList<String> getCommandTokens() {
        return commandTokens;
    }

    public int getFontSize() {
        return outputComponent.getFontSize();

    }

    public void setFontSize(int fontSize) {
        this.inputComponent.setFontSize(fontSize);
        this.outputComponent.setFontSize(fontSize);
    }

    public JTerminalTheme getTheme(){
        return theme;
    }

    public void setTheme(JTerminalTheme theme){
        this.theme = theme;
    }
}