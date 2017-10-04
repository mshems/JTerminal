package terminal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalKeylistener implements KeyListener {
    private TerminalInputComponent inputComponent;

    public TerminalKeylistener(TerminalInputComponent inputComponent){
        this.inputComponent = inputComponent;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(inputComponent.getCaretPosition() < inputComponent.getCurrPrompt().length() && e.getKeyCode() != KeyEvent.VK_RIGHT){
            inputComponent.advanceCaret();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(inputComponent.getCaretPosition() <= inputComponent.getLastPromptPos()){
                inputComponent.disableBackSpace();
            } else {
                if(!inputComponent.isAllowBackSpace()){
                    inputComponent.enableBackSpace();
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(inputComponent.isQuerying()){
                inputComponent.fireEvent(new QueryEvent(inputComponent, 1, "query-event", inputComponent.getCommand()));
                inputComponent.setQuerying(false);
            } else {
                inputComponent.fireEvent(new SubmitEvent(inputComponent, 1, "submit-event", inputComponent.getCommand()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
