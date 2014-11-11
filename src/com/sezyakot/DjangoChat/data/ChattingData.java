package com.sezyakot.DjangoChat.data;

public class ChattingData {

    private String comment;
    private boolean isMine = false;
    private int chatType = Integer.MIN_VALUE;
    private String name;
    private String imageUrl;
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public boolean isMine() {
        return isMine;
    }
    
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }
    
    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
    
    public int getChatType() {
        return chatType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    
}
