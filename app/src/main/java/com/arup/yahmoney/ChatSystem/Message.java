package com.arup.yahmoney.ChatSystem;

import androidx.annotation.NonNull;

public class Message {
    private String name;
    private final String message;
    private final String time;
    private final String date;

    public Message(String MessageLine) {
        this.date = MessageLine.substring(0, 10);
        this.time = MessageLine.substring(12, 17);
        String curr = MessageLine.substring(20);
        this.name = curr.substring(0, curr.indexOf(":"));
        this.message = curr.substring(name.length() + 2);
    }

    public void ChangeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    @NonNull
    public String toString() {
        return date + ", " + time + " - " + name + ": " + message;
    }
}
