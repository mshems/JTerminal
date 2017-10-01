package terminal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class TerminalInputComponent extends JTextArea{
    private TerminalEventListener listener;
    //private terminal.TerminalEventDispatcher eventDispatcher;
    private int lastPromptPos;
    private boolean allowBackSpace = true;
    private boolean multiline;
    private boolean querying = false;
    private String prompt;
    private static final String DEFAULT_PROMPT = "user@terminal > ";
    private LinkedList<String> history;
    private int historyPointer = 0;
    private boolean onBlankLine = true;


    public TerminalInputComponent(){
        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(new Font("MONOSPACED", Font.PLAIN, 18));
        this.setPrompt(DEFAULT_PROMPT);
        this.setMultiline(true);
        this.remapEnterKey();
        this.remapArrows();
        this.history = new LinkedList<>();
        this.addKeyListener(new TerminalKeylistener(this));
        //this.eventDispatcher = new terminal.TerminalEventDispatcher(this);
    }

    void start(){
        this.setEditable(true);
        this.prompt();
        this.advanceCaret();
    }

    public void append(String s){
        this.setText(this.getText()+s);
    }

    void print(String s){
        this.append(s);
        this.advanceCaret();
        this.onBlankLine = false;
    }

    void println(String s){
        this.append(s+System.lineSeparator());
        this.advanceCaret();
    }

    void newLine(){
        if(multiline) {
            this.append(System.lineSeparator());
        } else {
            this.clear();
        }
        onBlankLine = true;
    }

    void advance(){
        if(multiline) {
            this.appendPrompt();
        } else {
            this.prompt();
        }
        this.advanceCaret();
        onBlankLine = false;
    }

    void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(getPrompt())+getPrompt().length();
        this.setCaretPosition(lastPromptPos);
    }

    public String getCommand(){
        return this.getText().substring(lastPromptPos);
    }

    private void prompt(){
        this.setText(getPrompt());
    }

    private void appendPrompt(){
        this.append("\n" + getPrompt());
        this.advanceCaret();
    }

    void clear(){
        this.setText("");
    }

    void updateHistory(String command){
        if(history.size()>=25){
            history.removeLast();
        }
        history.addFirst(command);
        historyPointer = 0;
    }

    void fireEvent(SubmitEvent e){
        if(listener!=null){
            listener.submitActionPerformed(e);
        }
    }

    void fireEvent(QueryEvent e){
        if(listener!=null){
            listener.queryActionPerformed(e);
        }
    }


    void disableBackSpace(){
        this.allowBackSpace = false;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
    }

    void enableBackSpace(){
        this.allowBackSpace = true;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-previous");
    }

    private void remapEnterKey(){
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)), "");
    }

    private void remapArrows(){
        //LEFT ARROW
        this.getActionMap().put("leftArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(getCaretPosition() > lastPromptPos){
                    setCaretPosition(getCaretPosition()-1);
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "leftArrowAction");

        //UP ARROW
        this.getActionMap().put("upArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!history.isEmpty() && historyPointer < history.size()){
                    historyPointer++;
                    setText(getText().substring(0,lastPromptPos)+history.get(historyPointer-1));
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)), "upArrowAction");

        //DOWN ARROW
        this.getActionMap().put("downArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(historyPointer > 1 ){
                    historyPointer--;
                    setText(getText().substring(0,lastPromptPos)+history.get(historyPointer-1));
                } else if(historyPointer == 1){
                    setText(getText().substring(0,lastPromptPos)+"");
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "downArrowAction");
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    public void resetPrompt(){
        this.prompt = DEFAULT_PROMPT;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public int getLastPromptPos() {
        return lastPromptPos;
    }

    public void setLastPromptPos(int lastPromptPos) {
        this.lastPromptPos = lastPromptPos;
    }

    public boolean isAllowBackSpace() {
        return allowBackSpace;
    }

    public void setAllowBackSpace(boolean allowBackSpace) {
        this.allowBackSpace = allowBackSpace;
    }

    public boolean isQuerying() {
        return querying;
    }

    public void setQuerying(boolean querying) {
        this.querying = querying;
    }

    void setTerminalEventListener(TerminalEventListener listener){
        this.listener = listener;
    }

    TerminalEventListener getTerminalEventListener() {
        return this.listener;
    }

    /*public terminal.TerminalEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void setEventDispatcher(terminal.TerminalEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }*/

    public boolean isOnBlankLine() {
        return onBlankLine;
    }

    public void setOnBlankLine(boolean onBlankLine) {
        this.onBlankLine = onBlankLine;
    }
}
