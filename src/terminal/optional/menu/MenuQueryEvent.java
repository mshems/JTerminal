package terminal.optional.menu;

import terminal.core.event.QueryEvent;

public class MenuQueryEvent extends QueryEvent {
    public MenuQueryEvent(Object source) {
        super(source);
    }

    public MenuQueryEvent(Object source, boolean cancelledQuery) {
        super(source, cancelledQuery);
    }
}
