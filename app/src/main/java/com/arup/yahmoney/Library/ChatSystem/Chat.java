package com.arup.yahmoney.Library.ChatSystem;

import android.annotation.SuppressLint;

import com.arup.yahmoney.Library.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class Chat {
    User user1, user2;
    String MessageText;
    LinkedList<Message> messages = new LinkedList<>();
    long TotalAmount = 0;

    private boolean MessageValidation(String m) {
        String format = "00/00/0000, 00:00 -";
        int mSize = m.length();
        int formatSize = format.length();
        if (formatSize >= mSize || !m.substring(m.indexOf(":") + 1).contains(":")) {
            return false;
        }

        for (int i = 0; i < formatSize; i++) {
            char curr = m.charAt(i);
            char foo = format.charAt(i);
            if (foo == 48) {
                if(!(48 <= curr && curr <= 57)) {
                    return false;
                }
            }
            else if(curr != foo) {
                return false;
            }
        }
        return true;
    }

    public Chat(User user1, User user2, String prevMessage) {
        this.user1 = user1;
        this.user2 = user2;
        this.MessageText = prevMessage;
        if(prevMessage != null) {
            String[] MessageArray = MessageText.split("\n");
            int size = MessageArray.length;
            int lineNo = 0;

            if(MessageText.length() != 0) {
                while (lineNo < size) {
                    StringBuilder sb = new StringBuilder();
                    if(MessageValidation(MessageArray[lineNo])) {
                        sb.append(MessageArray[lineNo]);
                        lineNo++;
                    }
                    messages.add(new Message(sb.toString()));
                    lineNo++;
                }
            }
        }
    }

    public void addMessage(String message, boolean user) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm - ");
        Calendar calObj = Calendar.getInstance();
        messages.add(new Message(df.format(calObj.getTime()) + (user ? user1 : user2).getName() + ": " + message));
    }

    public void reload() {
        StringBuilder sb = new StringBuilder();
        for(Message message : messages) {
            sb.append(message.toString()).append("\n");
        }
        MessageText = sb.toString();
    }

    public void remove(int pos) {
        messages.remove(pos);
        reload();
    }

    public LinkedList<Message> getMessagesOf(boolean user) {
        LinkedList<Message> list = new LinkedList<>();
        for(Message message : messages) {
            if(message.getName().equals(user ? user1 : user2)) {
                list.add(message);
            }
        }
        return list;
    }

    public Message get(int index) {
        return messages.get(index);
    }

    public int size() {
        return messages.size();
    }

    public User getUser() {
        return user2;
    }

    public LinkedList<Message> getDateMessage(String date) {
        LinkedList<Message> list = new LinkedList<>();
        for (Message message : messages) {
            if(message.getDate().equals(date)) {
                list.add(message);
            }
        }
        return list;
    }

    public void ChangeName(String name, boolean who) {//true - user1, false- user2
        LinkedList<Message> list = new LinkedList<>();
        (who ? user1 : user2).changeName(name);
        for(Message message : messages) {
            if(message.getName().equals(name)) {
                message.ChangeName(name);
                list.add(message);
            }
        }
        messages = list;
        reload();
    }

    public Message getLastMessage() {
        return messages.getLast();
    }

    public boolean side(int position) {
        return messages.get(position).getName().equals(user1.getName());
    }

    public void addTransaction(long num) {
        TotalAmount += num;
    }
    public void subTransaction(long num) {
        TotalAmount -= num;
    }

    public long getAmount() {
        return TotalAmount;
    }

    private void addMessage(Message message) {
        messages.add(message);
        MessageText += "\n";
        MessageText += message.toString();
    }

    public LinkedList<Chat> getMessageWithDates() {
        LinkedList<Chat> chats = new LinkedList<>();
        int size = messages.size();
        int i = 0;
        while (i < size) {
            Chat chat = new Chat(user1, user2, null);
            String date = messages.get(i).getDate();
            while(messages.get(i).getDate().equals(date)) {
                chat.addMessage(messages.get(i));
                i++;
                if(i >= size) {
                    break;
                }
            }
            chats.add(chat);
        }
        return chats;
    }
 }
