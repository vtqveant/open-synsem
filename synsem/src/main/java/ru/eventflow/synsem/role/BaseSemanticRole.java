package ru.eventflow.synsem.role;

import opennlp.ccg.synsem.LF;

public abstract class BaseSemanticRole {

    LF lf;

    public BaseSemanticRole(LF lf) {
        this.lf = lf;
    }

    public LF getLf() {
        return lf;
    }

    public void setLf(LF lf) {
        this.lf = lf;
    }

    @Override
    public String toString() {
        return lf.prettyPrint("");
    }
}
