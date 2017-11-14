package terminal.core.event;

import java.awt.event.ActionEvent;

/**
 * Event object that is fired to notify that input has been received in response to a query.
 */
public class QueryEvent extends ActionEvent {

    public QueryEvent(Object source) {
        super(source, 1, "quert-event");
    }
}
