import java.awt.event.ActionEvent;

public interface TerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
}
