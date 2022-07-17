package com.wingy.moltakana.Objects;

public class RoomSettings {
    private String roomTitle, welcomeMessage, closeReason, designURL;
    private int visitorTime, memberTime, adminTime, SAdminTime, masterTime, talk, camera, chat, roomClose;

    public RoomSettings(String roomTitle, String welcomeMessage, String closeReason, String designURL, int visitorTime, int memberTime, int adminTime, int SAdminTime, int masterTime, int talk, int camera, int chat, int roomClose) {
        this.roomTitle = roomTitle;
        this.welcomeMessage = welcomeMessage;
        this.closeReason = closeReason;
        this.visitorTime = visitorTime;
        this.memberTime = memberTime;
        this.adminTime = adminTime;
        this.SAdminTime = SAdminTime;
        this.masterTime = masterTime;
        this.talk = talk;
        this.camera = camera;
        this.chat = chat;
        this.designURL = designURL;
        this.roomClose = roomClose;
    }
    public RoomSettings() {

    }

    public String getDesignURL() {
        return designURL;
    }

    public void setDesignURL(String designURL) {
        this.designURL = designURL;
    }

    public String getRoomTitle() {
        return roomTitle;
    }


    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }

    public int getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(int visitorTime) {
        this.visitorTime = visitorTime;
    }

    public int getMemberTime() {
        return memberTime;
    }

    public void setMemberTime(int memberTime) {
        this.memberTime = memberTime;
    }

    public int getAdminTime() {
        return adminTime;
    }

    public void setAdminTime(int adminTime) {
        this.adminTime = adminTime;
    }

    public int getSAdminTime() {
        return SAdminTime;
    }

    public void setSAdminTime(int SAdminTime) {
        this.SAdminTime = SAdminTime;
    }

    public int getMasterTime() {
        return masterTime;
    }

    public void setMasterTime(int masterAdmin) {
        this.masterTime = masterAdmin;
    }

    public int getTalk() {
        return talk;
    }

    public void setTalk(int talk) {
        this.talk = talk;
    }

    public int getCamera() {
        return camera;
    }

    public void setCamera(int camera) {
        this.camera = camera;
    }

    public int getChat() {
        return chat;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }

    public int getRoomClose() {
        return roomClose;
    }

    public void setRoomClose(int roomClose) {
        this.roomClose = roomClose;
    }
}
