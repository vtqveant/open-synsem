package ru.eventflow.asr.sphinx;

import ru.eventflow.asr.recognition.IRecognitionListener;
import ru.eventflow.asr.recognition.RecognitionHypothesis;
import ru.eventflow.asr.recognition.RecognitionResult;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.net.URL;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws Exception {
        Runner runner = new Runner();
        runner.run();
    }

    public Runner() {
    }

    public void run() throws Exception {
        URL sound = getClass().getResource("/sound/test.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sound);
        SphinxRecognizer recognizer = new SphinxRecognizer();
        recognizer.recognize(audioInputStream, audioInputStream.getFormat(), new LoggingRecognitionListener());
    }

    class LoggingRecognitionListener implements IRecognitionListener {
        @Override
        public void onRecognitionResult(RecognitionResult result) {
            List<RecognitionHypothesis> hs = result.getHypotheses();
            if (hs.size() > 0) {
                for (RecognitionHypothesis h : result.getHypotheses()) {
                    System.out.println(h.getOrthography());
                }
            } else {
                System.out.println("nothing was recognized");
            }
        }

        @Override
        public void onRecognitionStarted() {
            System.out.println("recognition started");
        }

        @Override
        public void onRecognitionError(String message) {
            System.out.println("recognition failed");
        }
    }


}
