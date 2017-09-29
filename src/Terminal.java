import javax.swing.*;
import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener{
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalInputComponent inputComponent;
    private JFrame frame;
    private CommandHandler commandHandler;
    private LinkedBlockingQueue<String> commandQueue;

    public Terminal(String title){
        this.commandHandler = new CommandHandler(this);
        this.commandQueue = new LinkedBlockingQueue<>();
        initFrame(title);
    }

    private void initFrame(String title){
        this.frame = new JFrame(title);
        this.frame.setSize(windowSize);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        inputComponent = new TerminalInputComponent();
        this.frame.add(inputComponent);
        inputComponent.setTerminalEventListener(this);
    }

    public synchronized void start(){
        inputComponent.start();
        this.frame.setVisible(true);
         while(true) {
             try {
                 wait();
                 if (!commandQueue.isEmpty()) {
                     commandHandler.processCommand(commandQueue.take());
                     inputComponent.advance();
                 }
             } catch (InterruptedException e) {
                //e.printStackTrace();
             }
         }
    }

    public void print(String s){
        inputComponent.append("\n"+s);
    }

    public synchronized String query(String query){
        String s=null;
        inputComponent.setPrompt(query);
        inputComponent.advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
           try{
               this.wait();
               s = inputComponent.getCommand();
               inputComponent.resetPrompt();
           }catch (InterruptedException ex){
                ex.printStackTrace();
           }
        }
        return s;
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.commandString);
            //System.out.println(commandQueue.toString());
            inputComponent.updateHistory(e.commandString);
        }catch (InterruptedException ex){
            //ex.printStackTrace();
        }
    }

    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }
}
