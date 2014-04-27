package ru.eventflow.synsem.modelchecker;

/**
 * formula like <code>not(alpha)</code>
 */
class HLFormula_neg extends HLFormula {

    Kind kind = HLFormula.Kind.NEG;

    public static HLFormula_neg newInstance(HLFormula child) {
        HLFormula_neg f = null;
        if (child != null && child.parent == null) {
            f = new HLFormula_neg();
            child.parent = f;
            f.size = child.size + 1;
            f.child = child;
        }
        return f;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        // MCFull(g, alpha_1)
        child.mcFull(frame, g);
        // for w \in W do
        for (String worldLabel : frame.getWorlds()) {
            // if not L(alpha_1, w) then
            if (!child.worldEvals.contains(worldLabel)) {
                // L(alpha, w) := TRUE
                worldEvals.add(worldLabel);
            }
        }
    }

}

