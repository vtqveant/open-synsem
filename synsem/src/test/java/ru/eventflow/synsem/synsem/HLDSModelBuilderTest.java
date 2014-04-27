package ru.eventflow.synsem.synsem;

import opennlp.ccg.grammar.Grammar;
import org.junit.Before;
import org.junit.Test;
import ru.eventflow.synsem.modelchecker.*;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class HLDSModelBuilderTest {

    public static final String GRAMMAR_XML = "/grammar/grammar.xml";
    private Grammar grammar;
    private ModelChecker modelchecker;


    @Before
    public void setUp() {
        try {
            URL url = getClass().getResource(GRAMMAR_XML);
            grammar = new GrammarLoader().load(url.getFile());
            modelchecker = new ModelChecker();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHLDSModelBuilder() throws IOException {
        HLDSModelBuilder hldsModelBuilder = new HLDSModelBuilder(grammar);
        hldsModelBuilder.build("включи красную лампу на столе на кухне");
        HybridFrame frame = hldsModelBuilder.getFrame();
        String s = GraphMLWriter.serialize(frame);
        System.out.println(s);
    }

    @Test
    public void testExtractPatient() throws ModelCheckerException, IOException {
        HLDSModelBuilder hldsModelBuilder = new HLDSModelBuilder(grammar);
        hldsModelBuilder.build("включи красную лампу на столе на кухне");
        HybridFrame frame = hldsModelBuilder.getFrame();
        Set<String> worlds = modelchecker.evaluate(frame, "<patient>-(T)");
        assertEquals(1, worlds.size());
        Utils.printMCResults(worlds);
    }

    @Test
    public void testExtractMultiplePatients() throws ModelCheckerException, IOException {
        HLDSModelBuilder hldsModelBuilder = new HLDSModelBuilder(grammar);
        hldsModelBuilder.build("включи красную лампу на столе и выключи лампочку на кухне");
        HybridFrame frame = hldsModelBuilder.getFrame();
        Set<String> worlds = modelchecker.evaluate(frame, "<patient>-(T)");
        assertEquals(2, worlds.size());
        Utils.printMCResults(worlds);
    }

}
