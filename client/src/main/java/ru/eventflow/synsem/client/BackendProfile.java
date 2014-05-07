package ru.eventflow.synsem.client;

import ru.eventflow.asr.DummyRecognizer;
import ru.eventflow.asr.recognition.IRecognizer;
import ru.eventflow.asr.recognition.RecognizerException;
import ru.eventflow.asr.sphinx.SphinxRecognizer;

import javax.sound.sampled.AudioFormat;

public class BackendProfile {

    public static final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false); // signed, bigEndian

    public enum Vendor {
        SPHINX("Sphinx (local)"),
        DUMMY("Dummy");

        private String s;

        private Vendor(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public static IRecognizer getRecognizer(Vendor vendor) throws RecognizerException {
        IRecognizer recognizer;
        switch (vendor) {
            case SPHINX:
                recognizer = new SphinxRecognizer();
                break;
            default:
                recognizer = new DummyRecognizer();
        }
        return recognizer;
    }
}
