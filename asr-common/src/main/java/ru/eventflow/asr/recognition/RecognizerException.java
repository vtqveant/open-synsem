package ru.eventflow.asr.recognition;

public class RecognizerException extends Exception {

	public RecognizerException(String message) {
		super(message);
	}

	public RecognizerException(Throwable cause) {
		super(cause);
	}

	public RecognizerException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecognizerException() {
		super();
	}
}
