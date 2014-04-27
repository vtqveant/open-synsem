package ru.eventflow.synsem.role;

import opennlp.ccg.synsem.LF;

/**
 * Location: where the action occurs (e.g., Johnny and Linda played carelessly <b>in the park</b>. I'll be <b>at Julie's house</b> studying for my test.).
 */
public class Location extends BaseSemanticRole {

    public Location(LF lf) {
        super(lf);
    }

}
