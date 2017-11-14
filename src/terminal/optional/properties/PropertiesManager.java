package terminal.optional.properties;

import terminal.core.JTerminal;
import terminal.util.Strings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager{
    private static final String COMMAND_CONFIG = "terminal-config";
    private static final String FILENAME = "terminal-config.properties";
    private static final String PATH = "./"+FILENAME;
    private static Properties properties;

    public static void addPropertyManager(JTerminal terminal, PropertiesConfigurator configurator){
        properties = new Properties();
        initProperties(terminal);
        terminal.putCommand(COMMAND_CONFIG, ()->configurator.config(terminal, properties));
    }
    public static void addPropertyManager(JTerminal terminal){
        properties = new Properties();
        initProperties(terminal);
        terminal.putCommand(COMMAND_CONFIG, ()->new PropertiesConfigurator().config(terminal, properties));
    }

    private static void initProperties(JTerminal terminal){
        OutputStream out = null;
        Path configPath = Paths.get(PATH);
        if(!Files.exists(configPath)){
            try{
                out= new FileOutputStream(FILENAME);

                properties.setProperty("font-size", "16");


                properties.store(out, null);
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(out != null){
                    try{
                        out.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        readProperties(terminal);
    }

    public static void readProperties(JTerminal terminal){
        InputStream in = null;
        try{
            in = new FileInputStream(FILENAME);
            properties.load(in);

            try{
                int fontSize = Integer.parseInt(properties.getProperty("font-size"));
                terminal.setFontSize(fontSize);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeProperties(JTerminal terminal){
        OutputStream out = null;
        try {
            out = new FileOutputStream(FILENAME);

            properties.setProperty("font-size", Integer.toString(terminal.getFontSize()));

            properties.store(out, null);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
