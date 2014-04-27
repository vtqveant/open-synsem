package ru.eventflow.synsem.modelchecker;

class HLFormula_false extends HLFormula {

    Kind kind = Kind.FALSE;

    HLFormula_false() {
        super();
        size = 0;
    }

    @Override
    void mcFullSubroutine(HybridFrame frame, WVarAssignment g) {
        // do nothing
    }

}
