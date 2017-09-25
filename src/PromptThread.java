public class PromptThread implements Runnable{
    private String response;
    private String prompt;
    private Thread thread;
    private String threadName;
    private Terminal terminal;

    public PromptThread(Terminal terminal, String threadName, String prompt){
        this.terminal = terminal;
        this.threadName = threadName;
        this.prompt = prompt;
    }

    public void start(){
        if(threadName==null){
            thread = new Thread(this,threadName);
            thread.start();
        }
    }

    @Override
    public void run() {
        this.response = terminal.query(prompt);
    }

    public String getResponse(){
        return this.response;
    }
}
