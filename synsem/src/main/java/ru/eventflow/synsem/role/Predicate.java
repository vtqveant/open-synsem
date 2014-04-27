package ru.eventflow.synsem.role;

import opennlp.ccg.hylo.Proposition;
import opennlp.ccg.synsem.LF;

/**
 * This is some generic verb-like thing, not a semantic role actually
 */
public class Predicate extends BaseSemanticRole {

    public Predicate(LF lf) {
        super(lf);
    }

    @Override
    public String toString() {
        if (lf instanceof Proposition) {
            return ((Proposition) lf).getName();
        } else {
            return lf.prettyPrint("");
        }
    }
}
