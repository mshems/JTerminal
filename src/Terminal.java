import javax.swing.*;
import java.awt.*;

public class Terminal implements TerminalEventListener{
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalInput terminalInput;
    private JFrame frame;
    private boolean waiting = false;
    private CommandHandler commandHandler;

    public Terminal(String title){
        this.commandHandler = new CommandHandler(this);
        initFrame(title);
    }

    public void start(){
        terminalInput.start();
        this.frame.setVisible(true);
    }

    private void initFrame(String title){
        this.frame = new JFrame(title);
        this.frame.setSize(windowSize);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        terminalInput = new TerminalInput();
        this.frame.add(terminalInput);
        terminalInput.setTerminalEventListener(this);
    }

    public synchronized String query(String prompt){
        //System.out.println(">>>>QUERYING");
        String s=null;
        terminalInput.setPrompt(prompt);
        terminalInput.prompt();
        //System.out.println(">>BEGIN WAITING FOR SUBMIT");
        this.waiting = true;
        while (waiting){
            try{
                this.wait();
                s = terminalInput.getCommand();
                terminalInput.setPrompt(terminalInput.DEFAULT_PROMPT);
                terminalInput.advance();
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        //System.out.println(">>DONE WAITING");
        //System.out.println(">>>>QUERY COMPLETE");
        return s;
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        //System.out.println(">>SUBMIT EVENT RECEIVED");
        if(waiting){
            waiting = false;
            this.notifyAll();
        } else {
            commandHandler.processCommand(e.commandString);
        }
    }
}
