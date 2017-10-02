package terminal;

interface TerminalInterface{
    void initFrame(String title);
    void start();
    void advance();
    void clear();
    void newLine();
    void printf(String format, Object... args);
    void print(String str);
    void println(String str);
    String query(String queryPrompt);
    String queryString(String queryPrompt, boolean allowEmptyString);
    boolean queryYN(String queryPrompt);
    Integer queryInteger(String queryPrompt, boolean allowNull);
    Boolean queryBoolean(String queryPrompt, boolean allowNull);
    Double queryDouble(String queryPrompt, boolean allowNull);
}
