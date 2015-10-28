package ru.eventflow.asr.recognition;

public class RecognitionHypothesis {

    private String orthography;
    private float score;
    private float confidence;

    public RecognitionHypothesis(String orthography, float score, float confidence) {
        this.orthography = orthography;
        this.score = score;
        this.confidence = confidence;
    }

    public String getOrthography() {
        return orthography;
    }

    public float getScore() {
        return score;
    }

    public float getConfidence() {
        return confidence;
    }
}
