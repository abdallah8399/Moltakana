package com.wingy.moltakana.Objects;

public class LogoutHistoryObject {
    String name, id, location, in, out, time;

    public LogoutHistoryObject(String name, String id, String location, String in, String out, String time) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.in = in;
        this.out = out;
        this.time = time;
    }

    public LogoutHistoryObject() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
