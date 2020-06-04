package com.android.messagebusexample.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Senders {
    private static final Logger LOGGER = LoggerFactory.getLogger(Senders.class);

    private Senders() {
    }

    public static Sender simple() {
        return (message, to) -> {
            for (MessageListener messageListener : to) {
                messageListener.receive(message);
            }
        };
    }

    public static Sender continueOnFailure() {
        return (message, to) -> {
            for (MessageListener messageListener : to) {
                try {
                    messageListener.receive(message);
                } catch (MessageListenerException e) {
                    LOGGER.error("Received exception while sending message", e);
                }
            }
        };
    }
}
