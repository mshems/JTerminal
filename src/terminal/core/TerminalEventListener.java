package terminal.core;

import terminal.menus.MenuEvent;

public interface TerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
    void menuActionPerformed(MenuEvent e);
}
