package com.android.messagebusexample.lib;

import com.android.messagebusexample.lib.utils.Assert;
import com.android.messagebusexample.lib.utils.StringUtils;
import com.google.common.base.Optional;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

public class MessageBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageBus.class);

    private final Map<String, List<MessageListenerWithPriority>> listeners = new ConcurrentHashMap<>();
    private final Executor executor;
    private final Sender defaultSender = Senders.simple();

    public MessageBus(Executor executor) {
        this.executor = executor;
    }

    public void sendMessage(String destination) throws MessageBusException {
        sendMessage(Message.forDestination(destination));
    }

    public void sendMessage(Message message) throws MessageBusException {
        sendMessage(message, defaultSender);
    }

    public void sendMessage(Message message, Sender sender) throws MessageBusException {
        Assert.notNull(message, "message parameter can't be null.");
        Assert.notNull(sender, "sender parameter can't be null.");

        doSendMessage(message, sender);
    }

    public void sendMessageSilently(String destination) {
        sendMessageSilently(Message.forDestination(destination));
    }

    public void sendMessageSilently(Message message) {
        sendMessageSilently(message, defaultSender);
    }

    public void sendMessageSilently(Message message, Sender sender) {
        try {
            sendMessage(message, sender);
        } catch (MessageBusException e) {
            LOGGER.error("failed to deliver message: {}", message, e);
        }
    }

    public void sendMessageAsync(String destination) {
        sendMessageAsync(Message.forDestination(destination));
    }

    public void sendMessageAsync(final Message message) {
        sendMessageAsync(message, defaultSender);
    }

    public void sendMessageAsync(final Message message, final Sender sender) {
        executor.execute(() -> sendMessageSilently(message, sender));
    }

    private void doSendMessage(Message message, Sender sender) throws MessageBusException {
        String destination = message.getDestination();
        if (StringUtils.isEmpty(destination)) {
            throw new MessageBusException("Message destination can't be empty: " + message);
        }

        try {
            Optional<? extends List<MessageListenerWithPriority>> messageListeners = getListenersForDestination(destination);
            if (messageListeners.isPresent() && !messageListeners.get().isEmpty()) {
                sender.send(message, messageListeners.get());
            } else {
                LOGGER.warn("No listeners for destination {}", destination);
            }
        } catch (MessageListenerException | RuntimeException e) {
            throw new MessageBusException("Failed to send a message: " + message, e);
        }
    }

    public void registerListener(String[] destination, MessageListener listener) {
        registerListener(destination, Priority.NORMAL, listener);
    }

    public synchronized void registerListener(String[] destination, Priority priority, MessageListener listener) {
        for (String dest : destination) {
            registerListener(dest, priority, listener);
        }
    }

    public void registerListener(String destination, MessageListener listener) {
        registerListener(destination, Priority.NORMAL, listener);
    }

    @SuppressWarnings("FeatureEnvy")
    public synchronized void registerListener(String destination, Priority priority, MessageListener listener) {
        Assert.hasLength(destination, "destination parameter can't be null or empty.");
        Assert.notNull(priority, "priority parameter can't be null.");
        Assert.notNull(listener, "listener parameter can't be null.");

        Optional<? extends List<MessageListenerWithPriority>> messageListenersSortedByHighToLowPriority =
                getListenersForDestination(destination);
        if (!messageListenersSortedByHighToLowPriority.isPresent()) {
            messageListenersSortedByHighToLowPriority = Optional.of(new CopyOnWriteArrayList<>());
            listeners.put(destination, messageListenersSortedByHighToLowPriority.get());
        }

        MessageListenerWithPriority messageListenerWithPriority = new MessageListenerWithPriority(listener, priority);
        if (messageListenersSortedByHighToLowPriority.get().contains(messageListenerWithPriority)) {
            LOGGER.debug("Attempt to add duplicate listener [{}] for {}", listener.getClass().getCanonicalName(), destination);
            return;
        }

        insertMessageListenerIntoListByPriority(messageListenerWithPriority, messageListenersSortedByHighToLowPriority.get());
    }

    private static void insertMessageListenerIntoListByPriority(MessageListenerWithPriority listenerToInsert,
                                                                List<MessageListenerWithPriority> messageListenersSortedByHighToLowPriority) {
        Priority priority = listenerToInsert.getPriority();

        int indexToInsert = 0;
        for (MessageListenerWithPriority listener : messageListenersSortedByHighToLowPriority) {
            Priority listItemPriority = listener.getPriority();
            if (priority.isHigherThan(listItemPriority) || priority == listItemPriority) {
                break;
            } else {
                indexToInsert += 1;
            }
        }

        messageListenersSortedByHighToLowPriority.add(indexToInsert, listenerToInsert);
    }

    public synchronized void unregisterListener(String[] destination, MessageListener listener) {
        for (String dest : destination) {
            unregisterListener(dest, listener);
        }
    }

    public synchronized void unregisterListener(String destination, MessageListener listener) {
        Optional<? extends List<MessageListenerWithPriority>> messageListeners = getListenersForDestination(destination);
        if (messageListeners.isPresent()) {
            messageListeners.get().remove(new MessageListenerWithPriority(listener, Priority.NORMAL));
        }
    }

    private synchronized Optional<? extends List<MessageListenerWithPriority>> getListenersForDestination(String destination) {
        return Optional.fromNullable(listeners.get(destination));
    }

    private static final class MessageListenerWithPriority implements MessageListener {
        private final MessageListener listener;
        private final Priority priority;

        private MessageListenerWithPriority(@NotNull MessageListener listener, @NotNull Priority priority) {
            this.listener = listener;
            this.priority = priority;
        }

        private MessageListener getMessageListener() {
            return listener;
        }

        private Priority getPriority() {
            return priority;
        }

        public void receive(Message message) throws MessageListenerException {
            getMessageListener().receive(message);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MessageListenerWithPriority)) {
                return false;
            }

            MessageListenerWithPriority ml = (MessageListenerWithPriority) o;
            return listener.equals(ml.getMessageListener());
        }

        @Override
        public int hashCode() {
            return listener.hashCode();
        }
    }

}
