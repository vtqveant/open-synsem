package ru.eventflow.synsem.generator;

import opennlp.ccg.realize.Edge;
import opennlp.ccg.realize.PruningStrategy;

import java.util.List;

public class IdentityPruningStrategy implements PruningStrategy {
    @Override
    public List<Edge> pruneEdges(List<Edge> catEdges) {
        return catEdges;
    }
}
