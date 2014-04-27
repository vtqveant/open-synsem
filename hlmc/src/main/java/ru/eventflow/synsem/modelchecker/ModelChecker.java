package ru.eventflow.synsem.modelchecker;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.util.List;
import java.util.Set;

public class ModelChecker {

    /**
     * TODO read a model from a file
     *
     * @param filename
     * @param formula
     * @return
     * @throws ModelCheckerException
     */
    public List<String> evaluate(String filename, String formula) throws ModelCheckerException {
        return null;
    }

    /**
     * Evaluates a formula in HLMC format against an HLModel and
     * produces an array of world labels that satisfy the formula.
     *
     * @param frame
     * @param formula
     * @return
     * @throws ModelCheckerException
     */
    public Set<String> evaluate(HybridFrame frame, String formula) throws ModelCheckerException {
        if (frame == null) {
            throw new ModelCheckerException("Hybid Kripke frame is null");
        }

        ANTLRStringStream in = new ANTLRStringStream(formula);
        HLFormulaLexer lexer = new HLFormulaLexer(in);
        HLFormulaParser parser = new HLFormulaParser(new CommonTokenStream(lexer));

        HLFormula f;
        try {
            f = parser.expression();
        } catch (RecognitionException e) {
            // normally RecognitionException is never thrown, consult parser.getErrors() for the actual exception.
            throw new ModelCheckerException(e);
        }
        if (lexer.hasErrors() || parser.hasErrors()) {
            throw new ModelCheckerException("Invalid HL formula");
        }

        f.mcFull(frame);

        return f.worldEvals;
    }

}
