package ru.eventflow.synsem.client;

import ru.eventflow.asr.recognition.IRecognitionListener;
import ru.eventflow.asr.recognition.IRecognizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

class RecognizerRunnable implements Runnable {

    File audioFile;
    IRecognitionListener listener;
    IRecognizer recognizer;

    public RecognizerRunnable(IRecognizer recognizer, File audioFile, IRecognitionListener listener) {
        this.audioFile = audioFile;
        this.listener = listener;
        this.recognizer = recognizer;
    }

    @Override
    public void run() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            recognizer.recognize(audioInputStream, audioInputStream.getFormat(), listener);
//            recognizer.destroy();
        } catch (Exception e) {
            listener.onRecognitionError(e.getMessage());
        }
    }

}

