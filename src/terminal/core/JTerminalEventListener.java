package terminal.core;

import terminal.core.event.QueryEvent;
import terminal.core.event.SubmitEvent;

public interface JTerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
}
