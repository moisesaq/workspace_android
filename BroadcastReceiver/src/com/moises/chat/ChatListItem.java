package com.moises.chat;

import java.io.Serializable;

//CLASE PARA MODIFICACION DE CHAT OPERADOR DE TECHNORIDES
@SuppressWarnings("serial")
public class ChatListItem implements Serializable{

    private boolean inConversation;
    private String id, updatedAt;
    private ChatUser receiver, sender;

    public boolean isInConversation() {
        return inConversation;
    }

    public ChatListItem setInConversation(boolean inConversation) {
        this.inConversation = inConversation;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ChatListItem setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getId() {
        return id;
    }

    public ChatListItem setId(String id) {
        this.id = id;
        return this;
    }

    public ChatUser getReceiver() {
        return receiver;
    }

    public ChatListItem setReceiver(ChatUser receiver) {
        this.receiver = receiver;
        return this;
    }

    public ChatUser getSender() {
        return sender;
    }

    public ChatListItem setSender(ChatUser sender) {
        this.sender = sender;
        return this;
    }
}
