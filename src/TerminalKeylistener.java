import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalKeylistener implements KeyListener {
    private TerminalInput parent;

    private static final int SUBMIT_EVENT_ID = 1;

    public TerminalKeylistener(TerminalInput parent){
        this.parent = parent;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(parent.getCaretPosition() < parent.prompt.length() && e.getKeyCode() != KeyEvent.VK_RIGHT){
            parent.advanceCaret();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(parent.getCaretPosition() <= parent.lastPromptPos){
                parent.disableBackSpace();
            } else {
                if(!parent.allowBackSpace){
                    parent.enableBackSpace();
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            //System.out.println("event created");
            parent.fireEvent(new SubmitEvent(parent, SUBMIT_EVENT_ID, "submit-event", parent.getCommand()));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
