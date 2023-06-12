package com.example.simplechat.models;

public class Message {
    private String userName;
    private String textMessage;
    private String messageTime;

    public Message(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getMessageTime() {
        return messageTime;
    }
}
