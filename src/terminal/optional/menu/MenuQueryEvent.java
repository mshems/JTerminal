package terminal.optional.menu;

import terminal.core.event.QueryEvent;

public class MenuQueryEvent extends QueryEvent {
    int modifiers=0;

    public MenuQueryEvent(Object source, int modifier) {
        super(source);
        this.modifiers=modifier;
    }

    public MenuQueryEvent(Object source, int modifier, boolean cancelledQuery) {
        super(source, cancelledQuery);
        this.modifiers=modifier;
    }
}
