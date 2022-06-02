package com.arup.yahmoney.Library.ChatSystem;

import com.arup.yahmoney.Library.User;

import java.util.LinkedList;

public class Chats {
    private User user;
    LinkedList<Chat> chats = new LinkedList<>();
    LinkedList<String> names = new LinkedList<>();

    private void loadNames() {
        LinkedList<String> names = new LinkedList<>();
        for(Chat chat : chats) {
            names.add(chat.getUser().getName());
        }
        this.names = names;
    }

    public Chats(User user) {
        this.user = user;
        loadNames();
    }

    public User getUser() {
        return user;
    }

    public void ChangeName(String newName) {
        user.changeName(newName);
    }

    public void remove(int pos) {
        chats.remove(pos);
    }

    public int getIndex(String name) {
        int i = 0;
        for(Chat chat : chats) {
            if(chat.getUser().getName().equals(name)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void remove(String name) {
        remove(getIndex(name));
    }

    public Message[] getAllLastMessage() {
        Message[] messages  = new Message[chats.size()];
        int i = 0;
        for(Chat chat : chats) {
            messages[i] = chat.getLastMessage();
        }
        return messages;
    }

    public Message getLastMessage(int index) {
        return chats.get(index).getLastMessage();
    }

    public Message getLastMessage(String name) {
        return getLastMessage(getIndex(name));
    }

    public int size() {
        return chats.size();
    }

    public LinkedList<String> getNames() {
        return names;
    }

    public void add(Chat chat) {
        chats.add(chat);
    }

    public Chat get(int index) {
        return chats.get(index);
    }
}
