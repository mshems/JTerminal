package terminal.core.event;

import java.awt.event.ActionEvent;

public class QueryEvent extends ActionEvent {

    public QueryEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
