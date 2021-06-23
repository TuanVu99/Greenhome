package com.example.GreenHome.Model;

public class ModelChat {
    String receiver, message,sender,timeSend;
    boolean isSeen;

    public ModelChat() {
    }

    public ModelChat(String receiver, String message, String sender, String timeSend, boolean isSeen) {
        this.receiver = receiver;
        this.message = message;
        this.sender = sender;
        this.timeSend = timeSend;
        this.isSeen = isSeen;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
