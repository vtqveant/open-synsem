package ru.eventflow.asr;

import ru.eventflow.asr.recognition.*;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DummyRecognizer implements IRecognizer {

    public DummyRecognizer() {
    }

    @Override
    public void recognize(InputStream inputStream, AudioFormat audioFormat, IRecognitionListener listener) throws RecognizerException, IOException {
        listener.onRecognitionStarted();

        // emulate recognition
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        RecognitionResult result = new RecognitionResult();
        List<RecognitionHypothesis> hypotheses = new ArrayList<RecognitionHypothesis>();
        hypotheses.add(new RecognitionHypothesis("включи лампу", 0, 0));
        hypotheses.add(new RecognitionHypothesis("выключи лампочку на кухне", 0, 0));
        result.setHypotheses(hypotheses);

        listener.onRecognitionResult(result);
    }

    @Override
    public void destroy() throws RecognizerException {
        // do nothing
    }

}
