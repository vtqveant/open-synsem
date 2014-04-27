package ru.eventflow.asr.sphinx;

import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import ru.eventflow.asr.recognition.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SphinxRecognizer implements IRecognizer {

    private Recognizer recognizer;
    private final ConfigurationManager cm;

    public SphinxRecognizer() throws RecognizerException {
        try {
            cm = new ConfigurationManager(SphinxRecognizer.class.getResource("/config/voxforge_jsgf.config.xml"));
            System.out.println("Loading...");
            recognizer = (Recognizer) cm.lookup("recognizer");
            recognizer.allocate();
        } catch (Exception e) {
            throw new RecognizerException(e);
        }
    }

    @Override
    public void recognize(InputStream inputStream, AudioFormat audioFormat, IRecognitionListener listener) throws RecognizerException, IOException {

        // send bytes to ASR
        listener.onRecognitionStarted();
        AudioInputStream audioIn = new AudioInputStream(new BufferedInputStream(inputStream), audioFormat, AudioSystem.NOT_SPECIFIED);

        // configure the audio input for the recognizer
        StreamDataSource streamDataSource = (StreamDataSource) cm.lookup("streamDataSource");
        streamDataSource.setInputStream(audioIn, "unused_stream_id");

        // Loop until last utterance in the audio file has been decoded, in which case the recognizer will return null.
        List<RecognitionHypothesis> hs = new ArrayList<RecognitionHypothesis>();
        Result result;
        while ((result = recognizer.recognize()) != null) {
            String text = result.getBestResultNoFiller();
            System.out.println(text);

            hs.add(new RecognitionHypothesis(text, 0f, 0f));
        }

        // release resources
        inputStream.close();
        audioIn.close();

        RecognitionResult recognitionResult = new RecognitionResult();
        recognitionResult.setHypotheses(hs);
        listener.onRecognitionResult(recognitionResult);
    }

    @Override
    public void destroy() throws RecognizerException {
        recognizer.deallocate();
    }

}
