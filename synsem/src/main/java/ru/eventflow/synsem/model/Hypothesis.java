package ru.eventflow.synsem.model;

import opennlp.ccg.parse.DerivationHistory;
import opennlp.ccg.synsem.Category;
import opennlp.ccg.synsem.LF;
import opennlp.ccg.synsem.Sign;

import java.util.ArrayList;
import java.util.List;

public class Hypothesis implements Comparable<Hypothesis> {

    private String input;
    private LF lf;
    private DerivationHistory derivationHistory;
    private float score = 1.0f;
    private List<Command> commands;
    private Category category;

    /**
     * reference to the initial query (i.e. an ASR result) that this hypothesis is based upon
     */
    private Query query;

    /**
     *
     * @param input
     * @param category
     * @param derivationHistory - the actual parse is derivationHistory.getOutput()
     * @param lf
     */
    public Hypothesis(String input, Category category, DerivationHistory derivationHistory, LF lf, Query query) {
        this.input = input;
        commands = new ArrayList<Command>();
        this.category = category;
        this.lf = lf;
        this.derivationHistory = derivationHistory;
        this.query = query;
    }

    public Sign getParse() {
        return derivationHistory.getOutput();
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getInput() {
        return input;
    }

    public LF getLF() {
        return lf;
    }

    public float getScore() {
        return score;
    }

    public DerivationHistory getDerivationHistory() {
        return derivationHistory;
    }

    public Category getCategory() {
        return category;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Query getQuery() {
        return query;
    }

    @Override
    public int compareTo(Hypothesis hypothesis) {
        if (this.getScore() > hypothesis.getScore()) {
            return 1;
        }
        if (this.getScore() == hypothesis.getScore()) {
            return 0;
        }
        return -1;
    }
}
