package com.wingy.moltakana.Objects;

import java.util.ArrayList;

public class MajorChatRoom {
    private String image, name;
    private ArrayList<MinorChatRoom> roomMinors;

    public MajorChatRoom(String image, String name, ArrayList<MinorChatRoom> roomMinors) {
        this.image = image;
        this.name = name;
        this.roomMinors = roomMinors;
    }
    public MajorChatRoom() {

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<MinorChatRoom> getRoomMinors() {
        return roomMinors;
    }

    public void setRoomMinors(ArrayList<MinorChatRoom> roomMinors) {
        this.roomMinors = roomMinors;
    }
}
