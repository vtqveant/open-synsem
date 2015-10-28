package ru.eventflow.asr.recognition;

public class RecognizerUnreachableException extends RecognizerException {
	public RecognizerUnreachableException() {
		super();
	}

	public RecognizerUnreachableException(String message) {
		super(message);
	}

	public RecognizerUnreachableException(Throwable cause) {
		super(cause);
	}

	public RecognizerUnreachableException(String message, Throwable cause) {
		super(message, cause);
	}
}
