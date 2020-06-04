package com.android.messagebusexample.lib;

public class MessageBusException extends Exception {

    public MessageBusException(String message) {
        super(message);
    }

    public MessageBusException(Throwable cause) {
        super(cause);
    }

    public MessageBusException(String message, Throwable cause) {
        super(message, cause);
    }
}
