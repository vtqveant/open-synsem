package ru.eventflow.synsem.modelchecker;

/**
 * formula like: <code>alpha and beta</code>
 */
class HLFormula_and extends HLFormula {

    Kind kind = HLFormula.Kind.AND;

    /**
     * reference to left sub expression <var>alpha</var>
     */
    HLFormula left;

    /**
     * reference to right sub expression <var>beta</var>
     */
    HLFormula right;

    public static HLFormula newInstance(HLFormula left, HLFormula right) {
        HLFormula_and f = null;
        if (left != null && right != null && left.parent == null && right.parent == null) {
            f = new HLFormula_and();
            left.parent = f;
            right.parent = f;
            f.size = left.size + right.size + 1;
            f.left = left;
            f.right = right;
        }
        return f;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        // MCFull(g, alpha_1)
        left.mcFull(frame, g);
        // MCFull(g, alpha_2)
        right.mcFull(frame, g);
        // for w \in W do
        for (String worldLabel : frame.getWorlds()) {
            // if L(alpha_1, w) and L(alpha_2, w) then
            if (left.worldEvals.contains(worldLabel) && right.worldEvals.contains(worldLabel)) {
                // L(alpha, w) := TRUE
                worldEvals.add(worldLabel);
            }
        }
    }

    @Override
    void resetWorldEvals(boolean recursively) {
        left.resetWorldEvals(recursively);
        right.resetWorldEvals(recursively);
    }

    @Override
    boolean clear(String wvarLabel) {
        boolean wvarFound = (left.clear(wvarLabel) || right.clear(wvarLabel));
        if (wvarFound) {
            resetWorldEvals(false);
        }
        return wvarFound;
    }
}

