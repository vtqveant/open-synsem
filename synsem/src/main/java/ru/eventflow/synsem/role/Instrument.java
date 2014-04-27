package ru.eventflow.synsem.role;

import opennlp.ccg.synsem.LF;

/**
 * Instrument: used to carry out the action (e.g., Jamie cut the ribbon <b>with a pair of scissors</b>.).
 */
public class Instrument extends BaseSemanticRole {
    public Instrument(LF lf) {
        super(lf);
    }
}
