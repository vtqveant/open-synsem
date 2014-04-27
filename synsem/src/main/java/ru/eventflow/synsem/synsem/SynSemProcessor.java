package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.HyloHelper;
import opennlp.ccg.hylo.Nominal;
import opennlp.ccg.parse.ParseException;
import opennlp.ccg.parse.Parser;
import opennlp.ccg.synsem.Category;
import opennlp.ccg.synsem.LF;
import opennlp.ccg.synsem.Sign;
import org.apache.log4j.Logger;
import ru.eventflow.synsem.model.*;
import ru.eventflow.synsem.modelchecker.*;
import ru.eventflow.synsem.role.Patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SynSemProcessor {

    public static final Logger log = Logger.getLogger(SynSemProcessor.class);

    private Parser parser;

    private final ModelChecker modelChecker = new ModelChecker();

    private boolean useModelChecker;
    private SemanticRoleExtractor extractor = new SemanticRoleExtractor();
    private HybridFrame frame;


    public SynSemProcessor(Grammar grammar, HybridFrame frame) {
        Configuration config = Configuration.getInstance();
        useModelChecker = config.isUseModelChecker();
        this.frame = frame;
        parser = new Parser(grammar);
    }

    public SynSemProcessor(String grammarFilename, HybridFrame frame) {
        Configuration config = Configuration.getInstance();
        useModelChecker = config.isUseModelChecker();
        this.frame = frame;

        try {
            GrammarLoader grammarLoader = new GrammarLoader();
            Grammar grammar = grammarLoader.load(grammarFilename);
            grammar.prefs.showFeats = config.isShowFeatures();
            parser = new Parser(grammar);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void process(List<Query> queries) {
        for (Query q : queries) {
            process(q);
        }
    }

    /**
     * I iterate through hypotheses multiple times in order to have a clear separation of concerns
     * between a parser, a command extractor, an HL formula builder and a model checker. This is by intention.
     *
     * @param query
     */
    public void process(Query query) {
        String input = query.getOrthography();
        log.info("Query: " + input);
        List<Hypothesis> hypotheses = new ArrayList<Hypothesis>();

        // 1. parser
        try {
            parser.parse(input);
            List<Sign> parses = parser.getResult();
            Sign[] results = new Sign[parses.size()];
            parses.toArray(results);

            log.info("Parses found: " + results.length);

            // iterate through parses
            for (int i = 0; i < results.length; i++) {
                Category cat = results[i].getCategory();
                log.info("Parse #" + (i + 1) + ": " + cat.toString());

                LF convertedLF = null;
                if (cat.getLF() != null) {
                    cat = cat.copy();
                    Nominal index = cat.getIndexNominal();
                    convertedLF = HyloHelper.compactAndConvertNominals(cat.getLF(), index, results[i]);
                    cat.setLF(null);
                }

                Hypothesis h = new Hypothesis(input, cat, results[i].getDerivationHistory(), convertedLF, query);
                hypotheses.add(h);
            }
        } catch (ParseException e) {
            log.info("Unable to parse: \"" + input + "\"", e);
        }

        // 2. extract commands
        for (Hypothesis h : hypotheses) {
            extractor.extractCommands(h);
        }

        for (Hypothesis h : hypotheses) {
            for (Command command : h.getCommands()) {
                for (Patient patient : command.getPatients()) {
                    // 3. HL formula builder
                    Formula f = extractor.toFormula(patient.getLf());
                    command.addFormula(f);

                    // 4. model checker
                    if (useModelChecker && frame != null) {
                        log.info("HL: " + f.getText());
                        check(f);
                    }
                }
            }
        }

        // 5. score (TODO find a better place?)
        score(hypotheses);

        query.setHypotheses(hypotheses);
    }

    private static void score(List<Hypothesis> hs) {
        for (Hypothesis h : hs) {
            calculateScore(h);
        }
    }

    /**
     * TODO this is just a sample scorer, think more and rewrite
     * <p/>
     * TODO add penalty for unspecified objects like in "включи свет у красной лампы" (т.е. неопределенный источник света рядом с красной лампой)
     * <p/>
     * First of all, scores should distinguish between ambiguous stuff, that is, it seems to require a step by step
     * reassessment with normalization across ambiguities and not across everything that is available.
     *
     * @param h
     */
    private static void calculateScore(Hypothesis h) {
        float score;
        if (h.getQuery().getScore() > 0) {
            score = h.getQuery().getScore();             // initial score from the ASR (initial score is around 5000)
        } else {
            score = 5000f;                               // this is to emulate an ASR score assignment for a text input
        }
        score *= (1 + h.getDerivationHistory().complexity());  // parse tree complexity, 1 if there's only one word
        if (h.getCategory().isFragment()) {
            score *= 0.4;                                // penalty for incomplete phrases
        }
        if (h.getCommands().size() == 0) {
            score *= 0.3;                                // penalty for non-commands
        }
        for (Command command : h.getCommands()) {
            List<Formula> forlmulae = command.getFormulae();
            if (forlmulae.size() == 0) {
                score *= 0.8;                            // malformed - is it possible (to have a malformed formula here) at all?
            }
            for (Formula f : forlmulae) {
                if (f.getWorlds().size() > 1) {
                    score *= 0.9;                        // penalty for each indeterminate object resolution is small
                } else if (f.getWorlds().size() == 0) {
                    score *= 0.3;                        // penalty for not being able to resolve is big
                }
            }
        }
        score /= 100;
        h.setScore(score);
    }

    /**
     * This is a general satisfiability in a predefined model. It means that the result is dependent on the way
     * the model is constructed. Basically, this is just a sanity check to rule out semantically incorrect requests,
     * which could have been produced at earlier stages of hypothesis generation.
     *
     * @param formula
     * @return
     * @throws IOException
     */
    private void check(Formula formula) {
        try {
            Set<String> worlds = modelChecker.evaluate(frame, formula.getText());
            for (String label : worlds) {
                if (label != null && !label.equals("")) {
                    World w = new World(label);
                    formula.addWorld(w);
                }
            }
        } catch (ModelCheckerException e) {
            log.error(e);
        }
    }



}
