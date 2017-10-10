/**
 * Creator: Matthew Shems
 *
 */

package terminal;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class TerminalIOComponent extends JTextArea{
    private TerminalEventListener listener;
    private boolean allowBackSpace;
    private boolean multiline;
    private boolean querying;
    private int lastPromptPos;
    private String prompt;
    private String defaultPrompt;
    private LinkedList<String> history;
    private int historyPointer = 0;
    private static final int MAX_LINES = 256;
    private int fontSize = 17;

    private static final String USER_NAME = System.getProperty("user.name");
    private static final String DEFAULT_PROMPT = USER_NAME+" ~ ";

    public static final Color[] DEFAULT_THEME = new Color[]{
            new Color(33, 33, 33),
            new Color(245, 245, 245),
            new Color(220, 220, 220)
    };


    public TerminalIOComponent(boolean multi){
        this.addKeyListener(new TerminalKeylistener(this));
        this.remapEnterKey();
        this.remapArrows();
        this.setMargin(new Insets(5,5,5,5));

        this.setBackground(DEFAULT_THEME[0]);
        this.setForeground(DEFAULT_THEME[1]);
        this.setCaretColor(DEFAULT_THEME[2]);

        this.setFont(new Font("consolas", Font.PLAIN, fontSize));
        history = new LinkedList<>();
        multiline = multi;
        defaultPrompt = DEFAULT_PROMPT;
        prompt = defaultPrompt;
        querying = false;
        allowBackSpace = false;
    }

    void start(){
        this.setEditable(true);
        this.prompt();
        this.advanceCaret();
    }

    String getInput(){
        return this.getText().substring(lastPromptPos);
    }

    private boolean isOnNewLine(){
        return (this.getText().endsWith(System.lineSeparator()) || this.getText().endsWith("\n"));
    }

    void newLine(){
        if(multiline) {
            this.append(System.lineSeparator());
        } else {
            this.clear();
        }
    }

    void advance(){
        if(this.getLineCount()>=MAX_LINES){
            int linesToRemove = this.getLineCount()-MAX_LINES;
            try {
                this.replaceRange("",
                        this.getLineStartOffset(0),
                        this.getLineEndOffset(linesToRemove));
            } catch (BadLocationException e){
                //e.printStackTrace();
            }
        }
        this.prompt();
        this.advanceCaret();
    }

    private void prompt(){
        if(multiline){
            if(this.isOnNewLine() || this.isClear()){
                this.append(prompt);
            } else {
                this.append(System.lineSeparator() + prompt);
            }
        } else {
            this.setText(prompt);
        }
    }

    private void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(prompt) + prompt.length();
        this.setCaretPosition(lastPromptPos);
    }

    void clear(){
        this.setText("");
    }

    private boolean isClear(){
        return this.getText().isEmpty();
    }

    void print(String s){
        this.append(s);
    }

    void println(String s){
        this.append(s+System.lineSeparator());
    }

    void printCentered(String str){
        int width = ((this.getWidth()/this.getColumnWidth())-str.length());
        for(int i=0; i<(width-1)/2; i++){
            str = " "+str+" ";
        }
        this.println(str);
    }

    void printRightAligned(String str){
        int width = ((this.getWidth()/this.getColumnWidth())-str.length());
        for(int i=0; i<width-1; i++){
            str = " "+str;
        }
        this.println(str);
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
        allowBackSpace = false;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
    }

    void enableBackSpace(){
        allowBackSpace = true;
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

        //RIGHT ARROW
        this.getActionMap().put("rightArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(getCaretPosition() < getText().length()){
                    setCaretPosition(getCaretPosition()+1);
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)), "rightArrowAction");


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

    int getFontSize() {
        return fontSize;
    }

    void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.setFont(new Font("consolas", Font.PLAIN, fontSize));
    }

    String getPrompt() {
        return prompt;
    }

    void setPrompt(String currPrompt){
        this.prompt = currPrompt;
    }

    void resetPrompt(){
        this.prompt = defaultPrompt;
    }

    String getDefaultPrompt() {
        return defaultPrompt;
    }

    void setDefaultPrompt(String prompt){
        this.defaultPrompt = prompt;
    }

    int getLastPromptPos() {
        return lastPromptPos;
    }

    boolean allowsBackspace() {
        return allowBackSpace;
    }

    boolean isQuerying() {
        return querying;
    }

    void setQuerying(boolean querying) {
        this.querying = querying;
    }

    void setTerminalEventListener(TerminalEventListener listener){
        this.listener = listener;
    }
}
