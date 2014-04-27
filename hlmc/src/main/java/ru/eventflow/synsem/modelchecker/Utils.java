package ru.eventflow.synsem.modelchecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Utils {

    public static void printMCResults(HLFormula formula) {
        if (formula != null) {
            for (String w : formula.worldEvals) {
                System.out.print(w + " ");
            }
        }
        System.out.print("\n");
    }

    public static void printMCResults(Set<String> worlds) {
        List<String> ls = new ArrayList<String>(worlds);
        Collections.sort(ls);
        for (String s : ls) {
            System.out.print(s + " ");
        }
        System.out.print("\n");
    }

}
