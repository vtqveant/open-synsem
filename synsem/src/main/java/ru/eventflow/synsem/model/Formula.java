package ru.eventflow.synsem.model;

import java.util.ArrayList;
import java.util.List;

public class Formula {

    private String text;

    /**
     * the worlds where this formula is true
     */
    private List<World> worlds = new ArrayList<World>();

    public Formula(String formula) {

        // TODO quick and dirty
//        formula = formula.replaceAll("\\<num\\>\\(sg\\)", " T ");
//        formula = formula.replaceAll("\\<num\\>\\(pl\\)", " T ");

        this.text = formula;
    }

    public String getText() {
        return text;
    }

    public void addWorld(World w) {
        worlds.add(w);
    }

    public List<World> getWorlds() {
        return worlds;
    }

}
