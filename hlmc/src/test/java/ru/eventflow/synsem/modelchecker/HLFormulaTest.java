package ru.eventflow.synsem.modelchecker;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class HLFormulaTest {

    private final String[] formulae = new String[]{
            "B x (x & y)",
            "x",
            "xyz",
            "x_y_z",
            "x_y_z_0123",
            "(((((x)))))",
            "<m1>(!T)",
            "<m1>-(!T)",
            "[m1](p1)",
            "[m1]-(p1)",
            "(B x ([m1](x)))",
            "!T & (E x (p1 | p2))",
            "((!F) & p1)",
            "x & y",
            "(x & y)",
            "(x) & (y)",
            "((x)) & (y)",
            "(((x)) & (y))",
            "T",
            "F",
            "!T",
            "T & F",
            "T | F",
            "(T)",
            "((T))",
            "!(T)",
            "(!T)",
            "(!(T))",
            "T & (F)",
            "(T) & F",
            "((T) & (F))",
            "(T & F)",
            "E x",
            "E (x)",
            "(E (x))",
            "A x",
            "A (x)",
            "(A (x))",
            "T -> F",
            "(T) -> (F)",
            "(T -> F)",
            "!((!(T) -> (F)))",
            "x -> <m1>(x)",
            "[m1](x) -> [m1]([m1](x))",
            "x -> y -> z",
            "x -> (y -> z)",
            "(x -> y) -> z",
            "x -> y & z"
    };

    @Test
    public void testParser() throws RecognitionException {
        for (String s : formulae) {
            ANTLRStringStream in = new ANTLRStringStream(s);
            HLFormulaLexer lexer = new HLFormulaLexer(in);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HLFormulaParser parser = new HLFormulaParser(tokens);
            HLFormula result = parser.expression();
            assertFalse(lexer.hasErrors());
            assertFalse(parser.hasErrors());
        }
    }

    @Test
    public void testUnicode() throws RecognitionException, IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream("(<Русский-Юникод>(Работает))".getBytes());
        ANTLRInputStream in = new ANTLRInputStream(bin, "UTF-8");
        HLFormulaLexer lexer = new HLFormulaLexer(in);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HLFormulaParser parser = new HLFormulaParser(tokens);

        // model
        HybridFrame frame = new HybridFrame("Russian Unicode Model");
        frame.addWorld("w1");
        frame.addWorld("w2");
        frame.setAccessible("Русский-Юникод", "w1", "w2");
        frame.setPropSym("Работает", "w2");

        parser.expression();
        assertFalse(lexer.hasErrors());
        assertFalse(parser.hasErrors());
    }

    /**
     * Implication is right-associative.
     * Disjunction and conjunction have precedence over implication.
     *
     * @throws RecognitionException
     */
    @Test
    public void testImplicationAssociativityAndPrecedence() throws RecognitionException, ModelCheckerException {
        Map<String, Boolean> fs = new HashMap<String, Boolean>(5);
        fs.put("(F -> F) -> F", false);
        fs.put("F -> (F -> F)", true);
        fs.put("F -> F -> F", true);
        fs.put("F -> T & F", true); // same as F -> (T & F)
        fs.put("F & F -> F", true); // same as (F & F) -> F

        for (Map.Entry<String, Boolean> entry : fs.entrySet()) {
            HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(entry.getKey()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HLFormulaParser parser = new HLFormulaParser(tokens);
            HLFormula formula = parser.expression();

            HybridFrame frame = FixturesHLModelBuilder.build();
            formula.mcFull(frame);
            Set<String> worlds = formula.worldEvals;
            assertEquals(entry.getValue(), worlds.size() != 0);
        }
    }

    @Test
    public void testParserErrorReports() {
        String[] fs = new String[]{
                "((x)",
                "@@",
                "() -> ()"
        };
        for (String f : fs) {
            HLFormulaLexer lexer = new HLFormulaLexer(new ANTLRStringStream(f));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            HLFormulaParser parser = new HLFormulaParser(tokens);

            try {
                parser.expression();
                assertFalse(lexer.hasErrors());
                assertTrue(parser.hasErrors());
            } catch (RecognitionException e) {
                throw new IllegalStateException("Recognition exception is never thrown, only declared.");
            }
        }
    }

    @Test
    public void testBuildHLFormula() throws ModelCheckerException {
        //  @n1(<m1>(p2 & p3))
        HLFormula formulaL = HLFormula_symbol.newInstance("p2");
        HLFormula formulaR = HLFormula_symbol.newInstance("p3");
        HLFormula formula = HLFormula_and.newInstance(formulaL, formulaR);
        formula = HLFormula_diamond.newInstance("m1", formula);
        formula = HLFormula_atSymbol.newInstance("n1", formula);

        HybridFrame frame = FixturesHLModelBuilder.build();
        formula.mcFull(frame);
        List<String> worlds = new ArrayList<String>(formula.worldEvals);
        Collections.sort(worlds);

        assertEquals(Arrays.asList(new String[]{"w1", "w2"}), worlds);
        Utils.printMCResults(formula);
    }

    @Test
    public void testParseFormulaWithoutModel() throws RecognitionException {
        ANTLRStringStream in = new ANTLRStringStream("B y (@root(([m1](x) -> [m1](<m1>(x & y)))))");
        HLFormulaLexer lexer = new HLFormulaLexer(in);
        HLFormulaParser parser = new HLFormulaParser(new CommonTokenStream(lexer));

        parser.expression();
        assertFalse(lexer.hasErrors());
        assertFalse(parser.hasErrors());
    }

}
