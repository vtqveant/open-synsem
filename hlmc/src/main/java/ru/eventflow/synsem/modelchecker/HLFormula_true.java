package ru.eventflow.synsem.modelchecker;

class HLFormula_true extends HLFormula {

    Kind kind = Kind.TRUE;

    HLFormula_true() {
        super();
        size = 0;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) {
        // for w \in W do
        for (String worldLabel : frame.getWorlds()) {
            // L(alpha, w) := TRUE
            worldEvals.add(worldLabel);
        }
    }

}
