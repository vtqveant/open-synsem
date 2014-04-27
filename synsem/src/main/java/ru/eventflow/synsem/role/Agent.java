package ru.eventflow.synsem.role;

import opennlp.ccg.synsem.LF;

/**
 * Agent: deliberately performs the action (e.g., <b>Bill</b> ate his soup quietly.).
 */
public class Agent extends BaseSemanticRole {
    public Agent(LF lf) {
        super(lf);
    }
}
