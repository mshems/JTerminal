import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalKeylistener implements KeyListener {
    private TerminalInputComponent parent;

    private static final int SUBMIT_EVENT_ID = 1;

    public TerminalKeylistener(TerminalInputComponent parent){
        this.parent = parent;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(parent.getCaretPosition() < parent.getPrompt().length() && e.getKeyCode() != KeyEvent.VK_RIGHT){
            parent.advanceCaret();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(parent.getCaretPosition() <= parent.getLastPromptPos()){
                parent.disableBackSpace();
            } else {
                if(!parent.isAllowBackSpace()){
                    parent.enableBackSpace();
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(parent.isQuerying()){
                parent.fireEvent(new QueryEvent(parent, SUBMIT_EVENT_ID, "submit-event", parent.getCommand()));
                parent.setQuerying(false);
            } else {
                parent.fireEvent(new SubmitEvent(parent, SUBMIT_EVENT_ID, "submit-event", parent.getCommand()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
