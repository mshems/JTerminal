package terminal.core.theme;

import java.awt.*;
import java.util.Arrays;

public class Theme {
    private static final Color DEFAULT_BACKGROUND_COLOR_DARK = new Color(36, 36, 36);;
    private static final Color DEFAULT_FOREGROUND_COLOR_DARK = new Color(245, 245, 245);
    private static final Color DEFAULT_CARET_COLOR_DARK = new Color(245,245,245);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_DARK = new Color(220, 220, 220);
    private static final Color DEFAULT_BACKGROUND_COLOR_LIGHT = new Color(245, 245, 245);;
    private static final Color DEFAULT_FOREGROUND_COLOR_LIGHT = new Color(33, 33, 33);
    private static final Color DEFAULT_CARET_COLOR_LIGHT = new Color(33,33,33);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_LIGHT = new Color(100, 100, 100);
    private static final Font  DEFAULT_FONT_MONO = new Font(Font.MONOSPACED, Font.PLAIN, 16);
//    private static final Font  DEFAULT_FONT_CONSOLAS = new Font("consolas", Font.PLAIN, 16);

    private String themeName;
    public Color backgroundColor;
    public Color foregroundColor;
    public Color caretColor;
    public Color highlightColor;
    public Font font;

    public Theme(String themeName){
        this.themeName = themeName;
        this.font = DEFAULT_FONT_MONO;

        switch (themeName) {
            case "default-dark":
                backgroundColor = DEFAULT_BACKGROUND_COLOR_DARK;
                foregroundColor = DEFAULT_FOREGROUND_COLOR_DARK;
                caretColor = DEFAULT_CARET_COLOR_DARK;
                highlightColor = DEFAULT_HIGHLIGHT_COLOR_DARK;
                break;
            case "default-light":
                backgroundColor = DEFAULT_BACKGROUND_COLOR_LIGHT;
                foregroundColor = DEFAULT_FOREGROUND_COLOR_LIGHT;
                caretColor = DEFAULT_CARET_COLOR_LIGHT;
                highlightColor = DEFAULT_HIGHLIGHT_COLOR_LIGHT;
                break;
            default:
                if(backgroundColor==null) backgroundColor = Color.BLACK;
                if(foregroundColor==null) foregroundColor = Color.WHITE;
                if(caretColor==null) caretColor = Color.WHITE;
                if(highlightColor==null) highlightColor = Color.LIGHT_GRAY;
                break;
        }
    }
}
