package terminal;

import java.awt.*;
import java.util.HashMap;

public class TerminalTheme {
    public static final int BACKGROUND_COLOR_INDEX = 0;
    public static final int FOREGROUND_COLOR_INDEX = 1;
    public static final int HIGHLIGHT_COLOR_INDEX = 2;

    public static final Color[] DEFAULT_THEME = new Color[]{new Color(33, 33, 33),new Color(245, 245, 245),new Color(220, 220, 220)};

    public static final Color[] DARK_THEME = new Color[]{new Color(33, 33, 33),new Color(245, 245, 245),new Color(220, 220, 220)};

    public static final Color[] MATRIX_THEME = new Color[]{new Color(33, 33, 33),new Color(110, 234, 30),new Color(220, 220, 220)};

    public static final Color[] LIGHT_THEME = new Color[]{new Color(245, 245, 245),new Color(33, 33, 33),new Color(66, 66, 66)};

    public static HashMap<String, Color[]> themes = new HashMap<>();

    public static Color[] getTheme(String key){
        return themes.get(key);
    }

    public static void addTheme(String key, Color[] colors){
        themes.put(key, colors);
    }
}
