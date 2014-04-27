package ru.eventflow.synsem.synsem;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public static final Logger log = Logger.getLogger(Configuration.class);

    private static Configuration ourInstance = new Configuration();
    private boolean useModelChecker;
    private boolean useTeX;
    private String grammarUrl;
    private String texDirectory;
    private boolean showFeatures;

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/project.properties"));
            grammarUrl = props.getProperty("grammar.name");
            showFeatures = Boolean.parseBoolean("grammar.prefs.showFeatures");
            useModelChecker = Boolean.parseBoolean(props.getProperty("modelchecker.active"));
            useTeX = Boolean.parseBoolean(props.getProperty("tex.active"));
            texDirectory = props.getProperty("tex.outputdir") + "/";
            (new File(texDirectory)).mkdirs();

            log.info((useModelChecker ? "Using" : "Not using") + " model checker.");
        } catch (IOException e) {
            log.error("Could not read project properties", e);
            System.exit(0);
        }
    }

    public boolean isUseModelChecker() {
        return useModelChecker;
    }

    public boolean isUseTeX() {
        return useTeX;
    }

    public String getGrammarUrl() {
        return grammarUrl;
    }

    public String getTexDirectory() {
        return texDirectory;
    }

    public boolean isShowFeatures() {
        return showFeatures;
    }
}
