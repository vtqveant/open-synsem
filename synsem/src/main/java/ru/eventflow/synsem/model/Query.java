package ru.eventflow.synsem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is basically a wrapper model for the data from the ASR
 * s. also ru.eventflow.asr.nuance.json.JSONHypothesis
 */
public class Query {

    private String orthography;

    /**
     * ASR score
     */
    private int score;

    /**
     * ASR confidence
     */
    private int confidence;

    private List<Hypothesis> hypotheses;

    public Query(String orthography) {
        this(orthography, 0, 0);
    }

    public Query(String orthography, int score, int confidence) {
        this.orthography = orthography;
        this.score = score;
        this.confidence = confidence;
        hypotheses = new ArrayList<Hypothesis>();
    }

    public String getOrthography() {
        return orthography;
    }

    public int getScore() {
        return score;
    }

    public List<Hypothesis> getHypotheses() {
        return hypotheses;
    }

    public void setHypotheses(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }
}
