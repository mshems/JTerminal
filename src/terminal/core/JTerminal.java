package terminal.core;

import terminal.core.behavior.*;
import terminal.core.event.*;
import terminal.core.theme.*;
import terminal.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings("unused")

public class JTerminal implements JTerminalEventListener {

    private final Dimension defaultWindowSize = new Dimension(800, 600);
    public final JTerminalPrinter out = new JTerminalPrinter(this);

    private String title;
    private Theme theme;
    private JFrame frame;
    private JScrollPane scrollPane;
    private JTerminalIOComponent inputComponent;
    private JTerminalIOComponent outputComponent;
    private LinkedBlockingQueue<String> commandQueue;
    private LinkedList<String> tokenBuffer;
    private CommandMap commandMap;

    private CommandExecutor commandExecutor;
    private CommandTokenizer commandTokenizer;
    private ExceptionHandler exceptionHandler;
    private StartBehavior startBehavior;
    private CloseBehavior closeBehavior;

    /**
     * Creates a new instance of a JTerminal.
     * @param title title of the JTerminal window
     */
    public JTerminal(String title) {
        commandQueue = new LinkedBlockingQueue<>();
        tokenBuffer = new LinkedList<>();
        commandMap = new CommandMap();
        commandExecutor = new CommandExecutor(this);
        commandTokenizer = new CommandTokenizer(this);
        exceptionHandler = new ExceptionHandler(this);
        theme = new Theme("default-dark");
        this.title = title;
        addDefaultCommands();
        initUI();
    }

    private void initUI() {
        frame = new JFrame(title);
        frame.setPreferredSize(defaultWindowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/appicon.png"));
        frame.setLayout(new BorderLayout());

        inputComponent = new JTerminalIOComponent(theme,true);
        inputComponent.setTerminalEventListener(this);
        outputComponent = inputComponent;

        scrollPane = new JScrollPane(outputComponent);
        scrollPane.setBorder(BorderFactory.createLineBorder(theme.backgroundColor,8));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }

    /**
     * Starts the JTerminal. If <code>startBehavior != null</code>, executes that behavior first.
     */
    public synchronized void start() {
        frame.setVisible(true);
        inputComponent.start();
        if(startBehavior !=null) startBehavior.doBehavior(this);
        while (true) {
            try {
                if(commandQueue.isEmpty()) wait();
                newLine();
                if (!commandQueue.isEmpty()) {
                    commandTokenizer.tokenize(commandQueue.take());
                    try {
                        commandExecutor.doCommand(tokenBuffer.pop());
                    } catch (UnknownCommandException e){
                        exceptionHandler.handleException(e);
                    }
                }
                inputComponent.advance();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Closes the JTerminal. If <code>closeBehavior != null</code>, executes that behavior before disposing.
     */
    public void close(){
        if(closeBehavior!=null) closeBehavior.doBehavior(this);
        frame.dispose();
    }

    /**
     * Returns whether JTerminal has tokens in its token buffer.
     * @return true if token buffer is not empty.
     */
    public boolean hasTokens(){
        return !tokenBuffer.isEmpty();
    }

    /**
     * Returns the nextInput token in the token buffer.
     * @return <code>String</code> token at top of token buffer.
     */
    public String nextToken(){
        if(hasTokens()){
            return tokenBuffer.pop();
        } else {
            return null;
        }
    }

    /**
     * Returns the nextInput token in the token buffer as an <code>int</code>.
     * @return <code>int</code> token from buffer
     * @throws IllegalTokenException if token can not be parsed into an <code>int</code>
     */
    public int nextIntToken() throws IllegalTokenException {
        String token = nextToken();
        try{
            return Integer.parseInt(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    /**
     * Returns the nextInput token in the token buffer as a <code>double</code>.
     * @return <code>double</code> token from buffer
     * @throws IllegalTokenException if token can not be parsed into a <code>double</code>
     */
    public double nextDoubleToken() throws  IllegalTokenException{
        String token = nextToken();
        try{
            return Double.parseDouble(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    /**
     * Returns the nextInput token in the token buffer as a <code>boolean</code>.
     * @return <code>boolean</code> token from buffer
     */
    public boolean nextBooleanToken(){
        return Boolean.parseBoolean(nextToken());
    }

    /**
     * Waits for and then reads input from user.
     * @return <code>String</code> input from user
     */
    private synchronized String nextInput(){
        String input = "";
        inputComponent.setQuerying(true);
        inputComponent.setCaretPosition(inputComponent.getText().length());
        int position = inputComponent.getCaretPosition();
        JTerminalIOComponent.lockLeftArrow(inputComponent, position);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getText().substring(position, inputComponent.getText().length());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        JTerminalIOComponent.unlockLeftArrow(inputComponent);
        newLine();
        return input.trim();
    }

    /**
     * Waits for and then reads input from user.
     * @return <code>String</code> input from user
     */
    public String next(){
        return nextInput();
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as an <code>int</code>
     * @throws NumberFormatException if input cannot be parsed as an <code>int</code>
     */
    public int nextInt() throws NumberFormatException{
        return Integer.parseInt(this.nextInput());
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as a <code>double</code>
     * @throws NumberFormatException if input cannot be parsed as a <code>double</code>
     */
    public double nextDouble() throws NumberFormatException{
        return Double.parseDouble(this.nextInput());
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as a <code>boolear</code>
     */
    public boolean nextBoolean(){
        return Boolean.parseBoolean(this.nextInput());
    }

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>String</code>
     */
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

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>String</code>
     */
    public String queryString(String queryPrompt) {
        while (true) {
            String input = query(queryPrompt);
            if (!input.isEmpty()) {
                return input;
            } else {
                out.println(Strings.ERROR_EMPTY);
            }
        }
    }

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as an <code>int</code>
     */
    public int queryInteger(String queryPrompt) {
        while (true) {
            try {
                return Integer.parseInt(query(queryPrompt));
            } catch (NumberFormatException e) {
                out.println(Strings.ERROR_INTEGER);
            }
        }
    }

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>double</code>
     */
    public double queryDouble(String queryPrompt) {
        while (true) {
            try {
                return Double.parseDouble(query(queryPrompt));
            } catch (NumberFormatException e) {
                out.println(Strings.ERROR_DOUBLE);
            }
        }
    }

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>boolean</code>
     */
    public boolean queryBoolean(String queryPrompt){
        return Boolean.parseBoolean(query(queryPrompt));
    }

    /**
     * Displays a prompt, then waits for an reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return true if input is "y" or "yes" (non-case-sensitive)
     */
    public boolean queryYN(String queryPrompt) {
        switch (query(queryPrompt).toLowerCase()) {
            case Strings.MATCH_YES:
            case Strings.MATCH_YES_SHORT:
                return true;
            default:
                return false;
        }
    }

    private void newLine() {
        inputComponent.newLine();
    }

    public void clear(){
        outputComponent.clear();
    }

    public void clearBuffer(){
        this.tokenBuffer.clear();
    }

    /**
     * Maps default commands.
     */
    public void addDefaultCommands() {
        commandMap.put("", ()->{});
        commandMap.put(Strings.COMMAND_CLEAR, this::clear);
    }

    /**
     * Un-maps default commands
     */
    public void removeDefaultCommands() {
        commandMap.remove("", commandMap.get(""));
        commandMap.remove(Strings.COMMAND_CLEAR, commandMap.get(Strings.COMMAND_CLEAR));
    }

    /**
     * Method fired after recieving a <code>SubmitEvent</code>.
     * Notifies that input has been recieved, then puts the input into the command queue and command history.
     * @param e event recieved
     */
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.getActionCommand());
            inputComponent.updateHistory(e.getActionCommand());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method fired after recieving a <code>QueryEvent</code>.
     * Notifies that input has been recieved as response to a query.
     * @param e
     */
    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }

    /**
     * Maps a command.
     * @param key the command name
     * @param command the command to be mapped
     * @param aliases alternate command names
     */
    public void putCommand(String key, Command command, String...aliases) {
        commandMap.put(key, command);
        for(String a:aliases){
            commandMap.put(a, command);
        }
    }

    public void replaceCommand(String key, Command command) { commandMap.replace(key, command); }

    public void removeCommand(String key, Command command) { commandMap.remove(key, command); }

    public Command getCommand(String key) { return this.commandMap.get(key); }

    public CommandMap getCommandMap() { return commandMap; }

    public CommandExecutor getCommandExecutor(){ return commandExecutor; }

    public void setCommandExecutor(CommandExecutor commandExecutor) { this.commandExecutor = commandExecutor; }

    public CommandTokenizer getCommandTokenizer(){ return commandTokenizer; }

    public void setCommandTokenizer(CommandTokenizer commandTokenizer){ this.commandTokenizer = commandTokenizer; }

    public ExceptionHandler getExceptionHandler() { return exceptionHandler; }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }

    public JTerminalIOComponent getInputComponent() { return inputComponent; }

    public JTerminalIOComponent getOutputComponent() { return outputComponent; }

    public JFrame getFrame() { return frame;}

    public JScrollPane getScrollPane(){ return scrollPane; }

    public String getDefaultPrompt(){ return inputComponent.getDefaultPrompt(); }

    public void setDefaultPrompt(String prompt) { inputComponent.setDefaultPrompt(prompt); }

    public String getPrompt(String prompt) { return inputComponent.getPrompt(); }

    public void setPrompt(String prompt) { inputComponent.setPrompt(prompt); }

    public void resetPrompt() { inputComponent.resetPrompt(); }

    public LinkedList<String> getTokenBuffer() { return tokenBuffer; }

    public int getFontSize() { return outputComponent.getFontSize(); }

    public void setFontSize(int fontSize) {
        this.inputComponent.setFontSize(fontSize);
        if(inputComponent!=outputComponent) this.outputComponent.setFontSize(fontSize);
    }

    public Theme getTheme(){ return theme; }

    public void setTheme(Theme theme){ this.theme = theme; }
}