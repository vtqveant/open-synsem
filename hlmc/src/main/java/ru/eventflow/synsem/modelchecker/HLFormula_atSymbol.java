package ru.eventflow.synsem.modelchecker;

public class HLFormula_atSymbol extends HLFormula {

    public static HLFormula newInstance(String label, HLFormula child) {
        HLFormula_atSymbol f = null;
        if (child != null && child.parent == null) {
            f = new HLFormula_atSymbol();
            child.parent = f;
            f.size = child.size + 2;
            f.child = child;
            f.label = label;
        }
        return f;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        if (frame.isNominal(label)) {
            kind = Kind.AT_NOM;
            mcFullAtNomSubroutine(frame, g);
        } else {
            kind = Kind.AT_WVAR;
            mcFullAtWVarSubroutine(frame, g);
        }
    }

    void mcFullAtNomSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {

        // MCFull(g, alpha_1)
        child.mcFull(frame, g);

        // MC_@(i, alpha)
        // let v = V(i)
        String worldLabel_1 = frame.getNominalAssign(label);
        // if L(alpha, v) then
        if (child.worldEvals.contains(worldLabel_1)) {
            // for w \in W do
            for (String worldLabel : frame.getWorlds()) {
                // L(@i alpha, w) := TRUE
                worldEvals.add(worldLabel);
            }
        }
    }

    void mcFullAtWVarSubroutine(HybridFrame frame, WVarAssignment g) throws ModelCheckerException {
        // MCFull(g, alpha_1)
        child.mcFull(frame, g);

        // MC_@(x, alpha)
        // let v = g(x)
        String worldLabel_1 = g.getWVarAssign(label);
        // if L(alpha, v) then
        if (child.worldEvals.contains(worldLabel_1)) {
            // for w \in W do
            for (String worldLabel : frame.getWorlds()) {
                // L(@x alpha, w) := TRUE
                worldEvals.add(worldLabel);
            }
        }
    }

    @Override
    boolean clear(String wvarLabel) {
        if (child == null) return false;
        boolean wvarFound = (this.label.equals(wvarLabel) || child.clear(wvarLabel));
        if (wvarFound) resetWorldEvals(false);
        return wvarFound;
    }
}
