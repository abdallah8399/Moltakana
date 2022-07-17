package com.wingy.moltakana.Objects;

import java.util.ArrayList;

public class MinorChatRoom {
    private String image, name, owner;
    private ArrayList<Member> roomMembers;
    private RoomSettings roomSettings;
    private Boolean VIP;

    public MinorChatRoom(String image, String name, String owner, ArrayList<Member> roomMembers, RoomSettings roomSettings, Boolean VIP) {
        this.image = image;
        this.name = name;
        this.owner = owner;
        this.roomMembers = roomMembers;
        this.VIP = VIP;
        this.roomSettings = roomSettings;
    }
    public MinorChatRoom() {

    }

    public Boolean getVIP() {
        return VIP;
    }

    public void setVIP(Boolean VIP) {
        this.VIP = VIP;
    }

    public RoomSettings getRoomSettings() {
        return roomSettings;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRoomSettings(RoomSettings roomSettings) {
        this.roomSettings = roomSettings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Member> getRoomMembers() {
        return roomMembers;
    }

    public void setRoomMembers(ArrayList<Member> roomMembers) {
        this.roomMembers = roomMembers;
    }
}
