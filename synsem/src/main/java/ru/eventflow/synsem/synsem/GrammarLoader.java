package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GrammarLoader {

    public static final Logger log = Logger.getLogger(GrammarLoader.class);

    public Grammar load(String grammarFilename) throws IOException {
        Grammar grammar = new Grammar(resolveGrammarLocation(grammarFilename));
        if (grammar.getName() != null) {
            log.info("Grammar '" + grammar.getName() + "' loaded.");
        }

        // set grammar preferences
        grammar.prefs.showSem = true;
        grammar.prefs.showFeats = true;
        grammar.prefs.featsToShow = "";

        return grammar;
    }

    private URL resolveGrammarLocation(String grammarFilename) throws IOException {
        File file = new File(grammarFilename);
        URL url;
        if (file.canRead()) {
            url = file.toURI().toURL();
        } else {
            url = getClass().getResource("/" + grammarFilename);  // if packaged in a jar
        }

        log.info("Loading grammar from URL: " + url);
        return url;
    }

}
