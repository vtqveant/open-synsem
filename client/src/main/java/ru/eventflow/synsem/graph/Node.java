package ru.eventflow.synsem.graph;

public class Node {

    private String label;
    private String nominals;
    private String propositionalSymbols;

    public Node() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNominals() {
        return nominals;
    }

    public void setNominals(String nominals) {
        this.nominals = nominals;
    }

    public String getPropositionalSymbols() {
        return propositionalSymbols;
    }

    public void setPropositionalSymbols(String propositionalSymbols) {
        this.propositionalSymbols = propositionalSymbols;
    }
}
