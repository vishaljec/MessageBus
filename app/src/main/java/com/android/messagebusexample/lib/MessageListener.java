package com.android.messagebusexample.lib;

public interface MessageListener {
    void receive(Message message) throws MessageListenerException;
}
