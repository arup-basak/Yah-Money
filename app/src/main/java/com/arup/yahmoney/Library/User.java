package com.arup.yahmoney.Library;

import androidx.annotation.NonNull;

public class User {
    String name;
    String phone;
    String uid = "";
    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public User(String name, String phone, String uid) {
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    @NonNull
    public String toString(){
        return name + ": " + phone;
    }

    public void set(User user) {
        name = user.getName();
        phone = user.getPhone();
    }
}
