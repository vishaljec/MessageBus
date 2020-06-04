package com.android.messagebusexample.lib;

import com.android.messagebusexample.lib.utils.Assert;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Message {
    public static final String ACTION_ANY = "";
    private static final String DESTINATION_UNKNOWN = "<unknown>";

    @NotNull
    private final String destination;

    @NotNull
    private final MessageData extraData;

    @Nullable
    private final String action;

    public Message(@NotNull String destination, @Nullable String action, @NotNull MessageData extraData) {
        Assert.notNull(destination, "destination parameter can't be null.");
        Assert.notNull(extraData, "extraData parameter can't be null.");

        this.destination = destination;
        this.action = action;
        this.extraData = extraData;
    }

    public static Message empty() {
        return new Message(DESTINATION_UNKNOWN, ACTION_ANY, new MessageData());
    }

    public static Message forDestination(@NotNull String originalDestination) {
        return new Message(originalDestination, ACTION_ANY, new MessageData());
    }

    public static Message forDestinationAndAction(@NotNull String originalDestination, @Nullable String action) {
        return new Message(originalDestination, action, new MessageData());
    }

    public static Message forDestinationAndAction(@NotNull String originalDestination,
                                                  @Nullable String action,
                                                  @NotNull MessageData messageData) {
        return new Message(originalDestination, action, messageData);
    }

    @NotNull
    public String getDestination() {
        return destination;
    }

    @Nullable
    public String getAction() {
        return action;
    }

    @NotNull
    public MessageData getExtraData() {
        return extraData;
    }

    public boolean isSameDestination(@NotNull String destination) {
        return this.destination.equals(destination);
    }

    public boolean isSameAction(@NotNull String value) {
        return value.equals(action);
    }

    public boolean isSameAction(@NotNull String[] values) {
        for (String s : values) {
            if (isSameAction(s)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSameDestinationAndAction(@NotNull String destination, @NotNull String action) {
        return isSameDestination(destination) && isSameAction(action);
    }

    public boolean isSameDestinationAndAction(@NotNull String destination, @NotNull String[] actions) {
        return isSameDestination(destination) && isSameAction(actions);
    }

    @Override
    public String toString() {
        return "Message" +
                "{destination='" + destination + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (action != null ? !action.equals(message.action) : message.action != null) return false;
        if (!destination.equals(message.destination)) return false;

        return true;
    }

    @Override
    @SuppressWarnings("all")
    public int hashCode() {
        int result = destination.hashCode();
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
