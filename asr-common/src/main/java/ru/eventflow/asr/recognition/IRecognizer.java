package ru.eventflow.asr.recognition;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;

public interface IRecognizer {

    /**
     * Perform speech recognition on the passed in audio input stream.
     *
     * @param inputStream An audio input stream for the recognizer
     * @param audioFormat audio format of the input
     * @param listener
     * @throws RecognizerException
     * @throws IOException
     */
    public void recognize(InputStream inputStream, AudioFormat audioFormat, IRecognitionListener listener) throws RecognizerException, IOException;

    /**
     * Destroys this recognizer, once this has been called the recognizer can't be used again
     */
    public void destroy() throws RecognizerException;

}
