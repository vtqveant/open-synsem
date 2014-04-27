package ru.eventflow.synsem.modelchecker;

/**
 * Formula like <code>&lt;pi&gt;- alpha</code>
 */
class HLFormula_inverseDiamond extends HLFormula {

    Kind kind = HLFormula.Kind.INVERSE_DIA;

    public static HLFormula newInstance(String label, HLFormula child) {
        HLFormula_inverseDiamond f = null;
        if (child != null && child.parent == null) {
            f = new HLFormula_inverseDiamond();
            child.parent = f;
            f.size = child.size + 2;
            f.label = label;
            f.child = child;
        }
        return f;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        if (!frame.isModality(label)) {
            throw new ModelCheckerException("Modality " + label + " is undefined.");
        }

        // MCFull(g, alpha_1)
        child.mcFull(frame, g);

        // MC<>-(alpha_1)
        // for w \in L(alpha) do
        for (String worldLabel_1 : frame.getWorlds()) {
            if (child.worldEvals.contains(worldLabel_1)) {
                // for v \in R(w) do
                for (String worldLabel_2 : frame.getWorlds()) {
                    if (frame.isAccessible(label, worldLabel_1, worldLabel_2)) {
                        // L(<>-alpha, v) := TRUE
                        worldEvals.add(worldLabel_2);
                    }
                }
            }
        }
    }

}
