package ru.eventflow.synsem.modelchecker;

import java.util.HashSet;
import java.util.Set;

public abstract class HLFormula {

    /**
     * size (complexity) of the formula
     */
    int size;

    /**
     * reference to parent element in formula's tree
     */
    HLFormula parent;

    /**
     * reference to subexpression <var>alpha</var>
     */
    HLFormula child;

    /**
     * evalutation of the formula in a model's world
     */
    Set<String> worldEvals;

    String label;

    /**
     * Kind of the formula
     */
    enum Kind {
        TRUE,        // boolean constant <code>T</code>
        FALSE,       // boolean constant <code>F</code>
        PROP,        // formula like: <code>p, q, r, ...</code>
        NOM,         // formula like: <code>i, j, k, ...</code>
        WVAR,        // formula like: <code>x, y, z, ...</code>
        NEG,         // formula like: <code>not(alpha)</code>
        AND,         // formula like: <code>alpha and beta</code>
        DIAMOND,     // formula like: <code>&lt;pi&gt; alpha</code>
        INVERSE_DIA, // formula like: <code>&lt;pi&gt;- alpha</code>
        EXISTS,      // formula like: <code>E alpha</code>
        AT_NOM,      // formula like: <code>@i alpha</code>
        AT_WVAR,     // formula like: <code>@x alpha</code>
        BIND_WVAR    // formula like: <code>^x.alpha</code>
    }

    Kind kind;

    public HLFormula() {
        parent = null;
        worldEvals = new HashSet<String>();
    }

    public void mcFull(HybridFrame frame) throws ModelCheckerException {
        mcFull(frame, new WVarAssignment());
    }

    /**
     * This function is called recursively by nested subformulae.
     *
     * @param frame
     * @param g
     */
    void mcFull(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        resetWorldEvals(true);
        mcFullSubroutine(frame, g);
    }

    /**
     *
     * @param frame
     * @param g
     */
    abstract void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException;

    /**
     * for beta \in sub(\alpha) do
     * for w \in W do
     * L(\beta, w) := FALSE
     * end for
     * end for
     */
    void resetWorldEvals(boolean recursively) {
        if (worldEvals != null) {
            worldEvals.clear();
            if (child != null && recursively) {
                child.resetWorldEvals(true);
            }
        }
    }

    /*
     * clear L(alpha, w)
     */
    boolean clear(String wvarLabel) {
        if (child == null) {
            return false;
        }
        boolean wvarFound = child.clear(wvarLabel);
        if (wvarFound) {
            resetWorldEvals(false);
        }
        return wvarFound;
    }

}
