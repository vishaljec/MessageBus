package com.android.messagebusexample.lib;

public interface Sender {
    void send(Message what, Iterable<? extends MessageListener> to) throws MessageListenerException;
}
