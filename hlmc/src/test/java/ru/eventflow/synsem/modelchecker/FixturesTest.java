package ru.eventflow.synsem.modelchecker;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Original HLMC fixtures
 */
public class FixturesTest {

    private static Map<String, List<String>> zero = new HashMap<String, List<String>>();
    private static Map<String, List<String>> one = new HashMap<String, List<String>>();

    static {
        // model-00.xml
        zero.put("r & <pi2>(p & j)", Arrays.asList(new String[]{}));
        zero.put("@i(<pi1>(q &!j))", Arrays.asList(new String[]{"w1", "w2", "w3"}));
        zero.put("Bx(E(p & x & @x(j)) & <pi1>-(i & <pi2>(q & @j(i))))", Arrays.asList(new String[]{}));

        // model-01.xml
        List<String> everywhere = Arrays.asList(new String[]{"root_node", "index_node", "b1", "b2", "a1", "a11", "a12", "t1", "a21", "t2", "d2", "a31", "t3", "d3"});
        Collections.sort(everywhere);

        // formula-01-01.hlf - "сущ. две разных книги" - везде
        one.put("@ root (<biblio>( <book> (B x(@ root( <biblio> ( <book> (!x)))))))", everywhere);

        // formula-01-02.hlf
        one.put("@root(<biblio>( <book> (B x( <author> (franceschet) & @root( <biblio> ([book](<author>(franceschet) -> x)))))))", new ArrayList<String>());

        // formula-01-03.hlf - "у всех книг есть атрибут cites" - везде
        one.put("@ root (<biblio> (<book> (B x (<cites> (x)))))", everywhere);

        // formula-01-04.hlf - "есть две разных книги, выпущенных в одном городе" - везде
        one.put("@ root (<biblio>(<book> (B x (<cites>(! x & <cites>(x))))))", everywhere);

        // formula-01-05.hlf
        one.put("@root( <biblio>( <book>( B x ( A ( B y ( (@ y (<book>-(T)) & ! x) -> @ x (<cites>(y))))))))", new ArrayList<String>());

        // formula-01-10.hlf - "книга, у которой авторы marx и de_rijke" - b1
        one.put("<author>(marx) & <author>(de_rijke)", Arrays.asList(new String[]{"b1"}));

        // formula-01-11.hlf - "книга, у которой авторы marx и franceschet" - невыполнимо
        one.put("<author>(marx) & <author>(franceschet)", new ArrayList<String>());

        // formula-01-12.hlf - "не-книга (т.е. статья) афанасьева, вышедшая в 2000 г." - a1
        one.put("<author>(afanasiev) & <cites>(<date>(year_2000) & <book>-(T))", Arrays.asList(new String[]{"a1"}));

        // formula-01-13.hlf - "есть книга с названием hybrid_logics" - везде
        one.put("@ root (<biblio>(<book>(<title>(hybrid_logics))))", everywhere);

        // formula-01-14.hlf - "атрибут paper у корневого узла в модели" - невыполнимо
        one.put("@ root (<paper>(T))", new ArrayList<String>());

        // formula-01-15.hlf - "в узле с номиналом bind_x есть ребро cites, ведущее в него же" - везде
        one.put("@ bind_x (<cites>(bind_x))", everywhere);

        // formula-01-16.hlf - "в модели есть две разных статьи" - невыполнимо
        one.put("@ root ( <biblio>( <paper>( B x ( @ root ( <biblio>( <paper>( ! x )))))))", new ArrayList<String>());

        // formula-01-17.hlf - "ни у одной статьи в модели нет автора marx" - везде
        one.put("@root( <biblio>( [paper]( !<author>(marx))))", everywhere);

        // formula-01-18.hlf
        one.put("A ( <title>(model_checking) -> <book>-(T))", new ArrayList<String>());

        // formula-01-19.hlf
        one.put("B x ( <date>(year_2000) & A (<date>(year_2000) -> x))", new ArrayList<String>());

        // formula-01-20.hlf
        one.put("B x ( <author>(franceschet) & A (<author>(franceschet) -> x))", new ArrayList<String>());
    }

    @Test
    public void testModelZero() throws RecognitionException, ModelCheckerException {
        HybridFrame frame = FixturesHLModelBuilder.buildFixtureZero();
        for (Map.Entry<String, List<String>> entry : zero.entrySet()) {
            String s = entry.getKey();
            HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(s));
            HLFormulaParser parser = new HLFormulaParser(new CommonTokenStream(lexer));

            HLFormula formula = parser.expression();
            formula.mcFull(frame);
            List<String> worlds = new ArrayList<String>(formula.worldEvals);
            Collections.sort(worlds);

            assertEquals(entry.getValue(), worlds);
            Utils.printMCResults(formula);
        }
    }

    @Test
    public void testModelOne() throws RecognitionException, ModelCheckerException {
        HybridFrame frame = FixturesHLModelBuilder.buildFixtureOne();
        for (Map.Entry<String, List<String>> entry : one.entrySet()) {
            String s = entry.getKey();
            HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(s));
            HLFormulaParser parser = new HLFormulaParser(new CommonTokenStream(lexer));

            HLFormula formula = parser.expression();
            formula.mcFull(frame);
            List<String> worlds = new ArrayList<String>(formula.worldEvals);
            Collections.sort(worlds);

            assertEquals(entry.getValue(), worlds);
            Utils.printMCResults(formula);
        }
    }

    @Test
    public void testHeizenbug() throws RecognitionException, ModelCheckerException {
        HybridFrame frame = FixturesHLModelBuilder.buildFixtureOne();

        String s = "@ bind_x (<cites>(bind_x))";
        List<String> everywhere = Arrays.asList(new String[]{"root_node", "index_node", "b1", "b2", "a1", "a11", "a12", "t1", "a21", "t2", "d2", "a31", "t3", "d3"});
        Collections.sort(everywhere);

        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(s));

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);
        HLFormula formula = parser.expression();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(everywhere, worlds);
        Utils.printMCResults(formula);
    }

}
