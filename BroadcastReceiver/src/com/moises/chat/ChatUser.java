package com.moises.chat;

import java.io.Serializable;

//ESTA CLASE ES PARA REPRESENTAR UN RECEIVER O SENDER EN CHAT DE TECHNORIDES
@SuppressWarnings("serial")
public class ChatUser implements Serializable{

	private int id;
    private String firstName, lastName, phone, email, typeUser;

    public int getId() {
        return id;
    }

    public ChatUser setId(int id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ChatUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ChatUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ChatUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ChatUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public ChatUser setTypeUser(String typeUser) {
        this.typeUser = typeUser;
        return this;
    }
}
