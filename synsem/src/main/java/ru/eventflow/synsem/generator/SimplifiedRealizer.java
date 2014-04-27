package ru.eventflow.synsem.generator;

import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.SatOp;
import opennlp.ccg.realize.*;
import opennlp.ccg.synsem.SignScorer;

import java.util.List;

public class SimplifiedRealizer {

    Chart chart = null;

    Grammar grammar = null;

    public SimplifiedRealizer(Grammar grammar) {
        this.grammar = grammar;
    }

    public List<Edge> realize(List<SatOp> preds) {
        EdgeFactory factory = new EdgeFactory(grammar, preds, SignScorer.nullScorer);
        PruningStrategy pruningStrategy = new IdentityPruningStrategy();
        chart = new Chart(factory, pruningStrategy);
        chart.initialize();
        chart.combine(-1, true);
        return chart.bestEdges();
    }


}
