import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener{
    private Dimension windowSize = new Dimension(800, 600);
    TerminalInput terminalInput;
    private JFrame frame;
    boolean waiting = false;
    CommandHandler commandHandler;
    LinkedBlockingQueue<String> commandQueue;

    public Terminal(String title){
        this.commandHandler = new CommandHandler(this);
        this.commandQueue = new LinkedBlockingQueue<>();
        initFrame(title);
    }

    private void initFrame(String title){
        this.frame = new JFrame(title);
        this.frame.setSize(windowSize);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        terminalInput = new TerminalInput();
        this.frame.add(terminalInput);
        terminalInput.setTerminalEventListener(this);
    }

    public synchronized void start(){
        terminalInput.start();
        this.frame.setVisible(true);
         while(true) {
             try {
                 if (commandQueue.isEmpty()) {
                     wait();
                     commandHandler.processCommand(commandQueue.take());
                 }
             } catch (InterruptedException e) {

             }
         }
    }

    public synchronized String query(String prompt){
        System.out.println(">>>>QUERYING");
        String s=null;
        terminalInput.setPrompt(prompt);
        terminalInput.prompt();
        System.out.println(">>BEGIN WAITING FOR SUBMIT");
        this.waiting = true;
        terminalInput.waiting=true;

        synchronized (this) {
           try{
               this.wait();
               System.out.println(">>DONE WAITING");
               s = terminalInput.getCommand();
               terminalInput.setPrompt(terminalInput.DEFAULT_PROMPT);
               terminalInput.advance();
           }catch (InterruptedException e){

           }
        }
        System.out.println(">>>>QUERY COMPLETE");
        return s;
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        System.out.println(">>SUBMIT EVENT RECEIVED");
        if(waiting){
            waiting = false;
            this.notifyAll();
        } else {
            try {
                commandQueue.put(e.commandString);
                terminalInput.updateHistory(e.commandString);
                notifyAll();
            }catch (InterruptedException ex){

            }
        }
    }
}
