package terminal.core;

import java.awt.event.ActionEvent;

public class SubmitEvent extends ActionEvent {

    public SubmitEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
