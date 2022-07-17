package com.wingy.moltakana.Objects;

public class Message {
    private String message, time;
    private int color, size;
    private Member sender;

    public Message(String message, Member sender, int color, String time) {
        this.message = message;
        this.sender = sender;
        this.color = color;
        this.time = time;
    }

    public Message() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getSender() {
        return sender;
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }
}
