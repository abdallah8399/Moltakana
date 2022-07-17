package com.wingy.moltakana.Objects;

public class Member {
    private String name, status, image, id;
    private int memberType, textColor, textSize;
    Boolean blocked, arabic, privateMessage, stopMic;

    public Member(String name, String status, String image, int memberType, String id, int textColor
    , int textSize, Boolean arabic, Boolean privateMessage, Boolean stopMic) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.memberType = memberType;
        this.id = id;
        this.blocked= false;

        this.textColor = textColor;
        this.textSize = textSize;
        this.arabic = arabic;
        this.privateMessage = privateMessage;
        this.stopMic = stopMic;

    }

    public Member() {
        blocked=false;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Boolean getArabic() {
        return arabic;
    }

    public void setArabic(Boolean arabic) {
        this.arabic = arabic;
    }

    public Boolean getPrivateMessage() {
        return privateMessage;
    }

    public void setPrivateMessage(Boolean privateMessage) {
        this.privateMessage = privateMessage;
    }

    public Boolean getStopMic() {
        return stopMic;
    }

    public void setStopMic(Boolean stopMic) {
        this.stopMic = stopMic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
