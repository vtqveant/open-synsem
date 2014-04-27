package ru.eventflow.synsem.client;

import com.darkprograms.speech.recognizer.GoogleRecognizer;
import ru.eventflow.asr.DummyRecognizer;
import ru.eventflow.asr.recognition.IRecognizer;
import ru.eventflow.asr.recognition.RecognizerException;
import ru.eventflow.asr.sphinx.SphinxRecognizer;

import javax.sound.sampled.AudioFormat;

public class BackendProfile {

    public static final AudioFormat DEFAULT_AUDIO_FORMAT = new AudioFormat(16000, 16, 1, true, false);   // signed, bigEndian

    public enum Vendor {
        GOOGLE("Google (proxy)"),
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

    public static AudioFormat getAudioFormat(Vendor vendor) {
        AudioFormat audioFormat;
        if (vendor.equals(Vendor.GOOGLE)) {
            audioFormat = new AudioFormat(8000, 16, 1, true, false);
        } else {
            audioFormat = DEFAULT_AUDIO_FORMAT;
        }
        return audioFormat;
    }

    public static IRecognizer getRecognizer(Vendor vendor) throws RecognizerException {
        IRecognizer recognizer;
        switch (vendor) {
            case SPHINX:
                recognizer = new SphinxRecognizer();
                break;
            case GOOGLE:
                recognizer = new GoogleRecognizer();
                break;
            default:
                recognizer = new DummyRecognizer();
        }
        return recognizer;
    }
}
