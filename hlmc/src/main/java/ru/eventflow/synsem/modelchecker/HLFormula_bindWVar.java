package ru.eventflow.synsem.modelchecker;

/**
 * Downarrow...
 *
 * formula like <code>^ x.alpha</code>
 */
class HLFormula_bindWVar extends HLFormula {

    Kind kind = Kind.BIND_WVAR;

    public static HLFormula newInstance(String wvarLabel, HLFormula child) {
        HLFormula_bindWVar f = null;
        if (child != null && child.parent == null) {
            f = new HLFormula_bindWVar();
            child.parent = f;
            f.size = child.size + 2;
            f.label = wvarLabel;
            f.child = child;
        }
        return f;
    }

    /**
     * Check^(g, x, alpha_1)
     *
     * @param frame
     * @param g
     * @throws ModelCheckerException
     */
    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        WVarAssignment g1 = g.clone();

        // for w \in W do
        for (String worldLabel : frame.getWorlds()) {
            // g(x) := w
            g1.setWVarAssign(label, worldLabel);
            // MCFull(M, g, alpha)
            child.mcFull(frame, g1);
            // if L(alpha, w) then
            if (child.worldEvals.contains(worldLabel)) {
                // Clear(L, x)
                child.clear(label);
                // L(^x.alpha, w) := TRUE
                worldEvals.add(worldLabel);
            } else {
                // Clear(L, x)
                child.clear(label);
            }
        }
    }

    @Override
    boolean clear(String wvarLabel) {
        boolean wvarFound = (this.label.equals(wvarLabel) || child.clear(wvarLabel));
        if (wvarFound) {
            resetWorldEvals(false);
        }
        return wvarFound;
    }

}
