package ru.eventflow.synsem.modelchecker;

public class HLFormula_symbol extends HLFormula {

    public static HLFormula newInstance(String label) {
        HLFormula_symbol f = new HLFormula_symbol();
        f.label = label;
        f.size = 1;
        return f;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) {
        // if the symbol is not among propositional symbols and nominals, then it must be a world variable
        if (frame.isPropSym(label)) {
            kind = Kind.PROP;
            // for w \in W do
            for (String w : frame.getWorlds()) {
                // if w \in V(alpha) then L(alpha, w) := TRUE end if
                if (frame.isPropSymTrue(label, w)) {
                    worldEvals.add(w);
                }
            }
        } else if (frame.isNominal(label)) {
            kind = Kind.NOM;
            String worldLabel = frame.getNominalAssign(label);
            worldEvals.add(worldLabel);
        } else {
            kind = Kind.WVAR;
            // L(alpha, g(alpha)) := TRUE
            String w2 = g.getWVarAssign(label);
            if (w2 != null) {
                worldEvals.add(w2);
            }
        }
    }

    @Override
    boolean clear(String wvarLabel) {
        boolean wvarFound;
        if (kind == Kind.WVAR) {
            wvarFound = (label.equals(wvarLabel));
        } else {
            if (child == null) return false;
            wvarFound = child.clear(wvarLabel);
        }
        if (wvarFound) resetWorldEvals(false);
        return wvarFound;
    }

}
