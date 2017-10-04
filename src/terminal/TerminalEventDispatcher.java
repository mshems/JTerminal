package terminal;

public class TerminalEventDispatcher {
    private TerminalIOComponent inputComponent;
    private TerminalEventListener inputListener;

    public TerminalEventDispatcher(TerminalIOComponent inputComponent){
        this.inputComponent = inputComponent;
        this.inputListener = this.inputComponent.getTerminalEventListener();
    }

    void fireEvent(SubmitEvent e){
        if(inputListener!=null){
            inputListener.submitActionPerformed(e);
        }
    }

    void fireEvent(QueryEvent e){
        if(inputListener!=null){
            inputListener.queryActionPerformed(e);
        }
    }
}
