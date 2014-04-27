package ru.eventflow.synsem.model;

import java.util.ArrayList;
import java.util.List;

public class World {

    private String label;
    private String nominal;
    private final List<String> propSymbols = new ArrayList<String>();

    public World(String label) {
        this.label = label;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getPropSymbols() {
        return propSymbols;
    }
}
