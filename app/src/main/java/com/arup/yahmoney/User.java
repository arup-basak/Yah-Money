package com.arup.yahmoney;

import androidx.annotation.NonNull;

public class User {
    String name;
    String phone;
    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
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

    @NonNull
    public String toString(){
        return name + ": " + phone;
    }

    public void set(User user) {
        name = user.getName();
        phone = user.getPhone();
    }
}
