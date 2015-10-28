package ru.eventflow.asr.recognition;

public interface IRecognitionListener {

	/**
	 * Called when a recognition result is available.
	 * @param result The recognition result
	 */
	public void onRecognitionResult(RecognitionResult result);
	
	/**
	 * Called when speech recognition has begun
	 */
	public void onRecognitionStarted();

    /**
     * Called when a recoverable error occurs
     */
    public void onRecognitionError(String message);

}
