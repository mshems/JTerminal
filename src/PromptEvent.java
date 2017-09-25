import java.awt.event.ActionEvent;

public class PromptEvent extends ActionEvent {
    public String promptString;

    public PromptEvent(Object source, int id, String command, String promptString) {
        super(source, id, command);
        this.promptString = promptString;
    }
}
