package ru.eventflow.synsem.modelchecker;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ModelCheckerTest {

    @Test
    public void testSatisfiability() throws RecognitionException, ModelCheckerException {
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("@n1(<m1>(p2 & p3))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HLFormulaParser parser = new HLFormulaParser(tokens);
        HybridFrame frame = FixturesHLModelBuilder.build();
        HLFormula formula = parser.expression();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(Arrays.asList(new String[]{"w1", "w2"}), worlds);
        Utils.printMCResults(formula);
    }

    @Test
    public void testBind() throws RecognitionException, ModelCheckerException {
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("B x (@n1(<m1>(x | p2)))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HLFormulaParser parser = new HLFormulaParser(tokens);
        HybridFrame frame = FixturesHLModelBuilder.build();
        HLFormula formula = parser.expression();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(Arrays.asList(new String[]{"w1", "w2"}), worlds);
        Utils.printMCResults(formula);
    }

    @Test
    public void testSuspiciousWVar() throws RecognitionException, ModelCheckerException {
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("<m1>((p2))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HLFormulaParser parser = new HLFormulaParser(tokens);
        HybridFrame frame = FixturesHLModelBuilder.build();
        HLFormula formula = parser.expression();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(Arrays.asList(new String[]{"w1"}), worlds);
        Utils.printMCResults(formula);
    }

    /**
     * @throws RecognitionException
     * @throws ModelCheckerException
     */
    @Test
    public void testBindNonexistantWVar() throws RecognitionException, ModelCheckerException {
        HybridFrame frame = FixturesHLModelBuilder.build();
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("B x (x & <m1>(p3))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);
        HLFormula formula = parser.expression();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(Arrays.asList(new String[]{"w1"}), worlds);
        Utils.printMCResults(formula);
    }

    @Test(expected = ModelCheckerException.class)
    public void testNonexistantModality() throws RecognitionException, ModelCheckerException {
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("@n1(<nonexistant>(p2 & p3))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);
        HLFormula formula = parser.expression();
        formula.mcFull(FixturesHLModelBuilder.build());
    }

    /**
     * It is ok to have an undeclared wvar where it's treated as a false propositional symbol
     * (i.e. everywhere except under a downarrow).
     *
     * @throws RecognitionException
     * @throws ModelCheckerException
     */
    @Test
    public void testUndeclaredWVar() throws RecognitionException, ModelCheckerException {
        HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream("@n1(<m1>(nonexistant & p3))"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);
        HLFormula formula = parser.expression();
        formula.mcFull(FixturesHLModelBuilder.build());
    }

    @Test
    public void testEvaluateCorrectFormula() throws ModelCheckerException {
        ModelChecker mc = new ModelChecker();
        Set<String> worlds = mc.evaluate(FixturesHLModelBuilder.build(), "B x (@n1(<m1>(x | p2)))");
        List<String> result = new ArrayList<String>(worlds);
        Collections.sort(result);
        assertEquals(Arrays.asList(new String[]{"w1", "w2"}), result);
    }

    @Test(expected = ModelCheckerException.class)
    public void testEvaluateIncorrectFormula() throws ModelCheckerException {
        ModelChecker mc = new ModelChecker();
        mc.evaluate(FixturesHLModelBuilder.build(), "(");
    }

}
