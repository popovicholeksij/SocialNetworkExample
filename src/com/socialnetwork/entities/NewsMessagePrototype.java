package com.socialnetwork.entities;

public class NewsMessagePrototype {

    public static final int FRIEND_ADDED = 1;
    public static final int PHOTOS_ADDED = 2;
    public static final int WALL_MESSAGE_ADDED = 3;
    public static final int FRIEND_DELETED = 4;

    private int id;
    private User sender;
    private int messageType;
    private int objectId;
    private long creationDate;
    private String formattedCreationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getFormattedCreationDate() {
        return formattedCreationDate;
    }

    public void setFormattedCreationDate(String formattedCreationDate) {
        this.formattedCreationDate = formattedCreationDate;
    }

    public static int getFriendAdded() {
        return FRIEND_ADDED;
    }

    public static int getPhotosAdded() {
        return PHOTOS_ADDED;
    }

    public static int getWallMessageAdded() {
        return WALL_MESSAGE_ADDED;
    }

    public static int getFriendDeleted() {
        return FRIEND_DELETED;
    }

}
