package terminal.core;

import java.awt.*;

public class JTerminalTheme{
    private static final Color DEFAULT_BACKGROUND_COLOR_DARK = new Color(36, 36, 36);;
    private static final Color DEFAULT_FOREGROUND_COLOR_DARK = new Color(245, 245, 245);
    private static final Color DEFAULT_CARET_COLOR_DARK = new Color(245,245,245);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_DARK = new Color(220, 220, 220);
    private static final Color DEFAULT_BACKGROUND_COLOR_LIGHT = new Color(245, 245, 245);;
    private static final Color DEFAULT_FOREGROUND_COLOR_LIGHT = new Color(33, 33, 33);
    private static final Color DEFAULT_CARET_COLOR_LIGHT = new Color(33,33,33);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_LIGHT = new Color(100, 100, 100);
    private static final Font  DEFAULT_FONT = new Font("consolas", Font.PLAIN, 16);

    public String themeName;
    public Color backgroundColor;
    public Color foregroundColor;
    public Color caretColor;
    public Color highlightColor;
    public Font font;

    public JTerminalTheme(String themeName){
        this.themeName = themeName;
        this.font = DEFAULT_FONT;
        if(themeName.equals("default-dark")){
            backgroundColor = DEFAULT_BACKGROUND_COLOR_DARK;
            foregroundColor = DEFAULT_FOREGROUND_COLOR_DARK;
            caretColor = DEFAULT_CARET_COLOR_DARK;
            highlightColor = DEFAULT_HIGHLIGHT_COLOR_DARK;
        } else if(themeName.equals("default-light")){
            backgroundColor = DEFAULT_BACKGROUND_COLOR_LIGHT;
            foregroundColor = DEFAULT_FOREGROUND_COLOR_LIGHT;
            caretColor = DEFAULT_CARET_COLOR_LIGHT;
            highlightColor = DEFAULT_HIGHLIGHT_COLOR_LIGHT;
        } else {
            backgroundColor = Color.BLACK;
            foregroundColor = Color.WHITE;
            caretColor = Color.WHITE;
            highlightColor = Color.LIGHT_GRAY;
        }
    }
}
