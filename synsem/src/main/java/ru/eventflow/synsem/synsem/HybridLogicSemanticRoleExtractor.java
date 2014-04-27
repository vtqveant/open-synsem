package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import ru.eventflow.synsem.model.Hypothesis;
import ru.eventflow.synsem.modelchecker.HybridFrame;
import ru.eventflow.synsem.modelchecker.ModelChecker;
import ru.eventflow.synsem.modelchecker.ModelCheckerException;

import java.io.IOException;
import java.util.Set;

public class HybridLogicSemanticRoleExtractor {

    public static void main(String[] args) {
        HybridLogicSemanticRoleExtractor extractor = new HybridLogicSemanticRoleExtractor();
        Hypothesis hypothesis = new Hypothesis("включи красную лампу на кухне и выключи лампочку в прихожей", null, null, null, null);

        try {
            extractor.extractCommands(hypothesis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractCommands(Hypothesis hypothesis) throws IOException, ModelCheckerException {
        String grammarFilename = Configuration.getInstance().getGrammarUrl();
        Grammar grammar = new GrammarLoader().load(grammarFilename);
        ModelChecker modelchecker = new ModelChecker();

        HLDSModelBuilder hldsModelBuilder = new HLDSModelBuilder(grammar);
        hldsModelBuilder.build(hypothesis.getInput());
        HybridFrame frame = hldsModelBuilder.getFrame();

        // patients
        Set<String> worlds = modelchecker.evaluate(frame, "<patient>-(T)");
        System.out.println("Patients: ");
        printPropSymbols(frame, worlds);

        // actors
        worlds = modelchecker.evaluate(frame, "<actor>-(T)");
        System.out.println("Actors: ");
        printPropSymbols(frame, worlds);

        // locations
        worlds = modelchecker.evaluate(frame, "<anchor>-(T)");
        System.out.println("Locations: ");
        printPropSymbols(frame, worlds);

        // TODO extract LF subtree
    }

    private static void printPropSymbols(HybridFrame frame, Set<String> worlds) {
        for (String w : worlds) {
            System.out.print("@" + w + " => ");
            Set<String> propSymbols = frame.getWorldPropSymbols(w);
            if (propSymbols != null) {
                for (String n : propSymbols) {
                    System.out.print(n + " ");
                }
            }
            System.out.print("\n");
        }
    }
}
