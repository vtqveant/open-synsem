package ru.eventflow.asr.recognition;

import java.util.ArrayList;
import java.util.List;

public class RecognitionResult {

    private List<RecognitionHypothesis> hypotheses = new ArrayList<RecognitionHypothesis>();

    public RecognitionResult() {
    }

    public List<RecognitionHypothesis> getHypotheses() {
        return hypotheses;
    }

    public void setHypotheses(List<RecognitionHypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public String getError() {
        return null;
    }
}
