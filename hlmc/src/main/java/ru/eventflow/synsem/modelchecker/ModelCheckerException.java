package ru.eventflow.synsem.modelchecker;

public class ModelCheckerException extends Exception {
    public ModelCheckerException() {
    }

    public ModelCheckerException(String s) {
        super(s);
    }

    public ModelCheckerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ModelCheckerException(Throwable throwable) {
        super(throwable);
    }
}
