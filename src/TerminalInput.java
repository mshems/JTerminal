import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class TerminalInput extends JTextArea{
    private TerminalEventListener listener;
    int lastPromptPos;
    boolean allowBackSpace = true;
    boolean multiline = false;
    String prompt = "test@cterm > ";
    static final String DEFAULT_PROMPT = "test@cterm > ";
    boolean waiting = false;

    public TerminalInput(){
        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(new Font("MONOSPACED", Font.PLAIN, 19));

        remapEnterKey();
        remapArrows();

        this.addKeyListener(new TerminalKeylistener(this));
    }

    synchronized void fireEvent(SubmitEvent e){
        //System.out.println(">>FIRING SUBMIT EVENT");
        if(listener!=null){
            advance();
            listener.submitActionPerformed(e);
        }
    }

    void setTerminalEventListener(TerminalEventListener listener){
        this.listener = listener;
    }

    public void start(){
        this.setEditable(true);
        this.setText(DEFAULT_PROMPT);
        this.advanceCaret();
    }
    public synchronized void setPrompt(String prompt){
        this.prompt = prompt;
    }
    public String getCommand(){
        return this.getText().substring(lastPromptPos);
    }

    void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(prompt)+prompt.length();
        this.setCaretPosition(lastPromptPos);
    }
    void advance(){
        if(multiline) {
            this.appendPrompt();
        } else {
            this.prompt();
        }
        this.advanceCaret();
    }

    void appendPrompt(){
        this.append("\n" + prompt);
        this.advanceCaret();
    }

    void prompt(){
        this.setText(prompt);
        this.advanceCaret();
    }

    void clear(){
        this.setText("");
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
    }
}
