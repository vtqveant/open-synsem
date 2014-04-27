package ru.eventflow.synsem.modelchecker;

import java.util.HashMap;
import java.util.Map;

/**
 * Variable assignment for HL formulas (the ``g'' in $[ \! [ \cdot ] \! ]^{\mathcal{M}, g}$)
 */
public class WVarAssignment {

    /**
     * world variable assignments
     */
    public Map<String, String> wvarAssignments;


    public WVarAssignment() {
        wvarAssignments = new HashMap<String, String>();
    }

    String getWVarAssign(String wvarLabel) {
        if (wvarAssignments.containsKey(wvarLabel)) {
            return wvarAssignments.get(wvarLabel);
        } else {
            return null;
        }
    }

    void setWVarAssign(String wvarLabel, String worldLabel) {
        wvarAssignments.put(wvarLabel, worldLabel);
    }

    @Override
    public WVarAssignment clone() {
        WVarAssignment g1 = new WVarAssignment();
        g1.wvarAssignments.putAll(wvarAssignments);
        return g1;
    }
}
