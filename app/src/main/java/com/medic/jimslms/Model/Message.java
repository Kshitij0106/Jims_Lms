package com.medic.jimslms.Model;

public class Message {
    String senderId, receiverId, textMessage, messageType, messageStatus;

    public Message() {
    }

    public Message(String senderId, String receiverId, String textMessage, String messageType, String messageStatus) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.textMessage = textMessage;
        this.messageType = messageType;
        this.messageStatus = messageStatus;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
