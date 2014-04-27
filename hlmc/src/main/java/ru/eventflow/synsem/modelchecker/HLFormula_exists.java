package ru.eventflow.synsem.modelchecker;

/**
 * formula like <code>E alpha</code>
 */
class HLFormula_exists extends HLFormula {

    Kind kind = HLFormula.Kind.EXISTS;

    public static HLFormula_exists newInstance(HLFormula child) {
        HLFormula_exists f = null;
        if (child != null && child.parent == null) {
            f = new HLFormula_exists();
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

        // MC_E(alpha_1)
        // if L(alpha) =/= 0 then
        for (String worldLabel_1 : frame.getWorlds()) {
            if (child.worldEvals.contains(worldLabel_1)) {
                // for w \in W do
                for (String worldLabel_2 : frame.getWorlds()) {
                    // L(Ealpha, w) := TRUE
                    worldEvals.add(worldLabel_2);
                }
                return;
            }
        }
    }

}


