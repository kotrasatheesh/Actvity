package com.example.asutosh.aebug.bean;

/**
 * Created by Asutosh on 06-09-2017.
 */

public class commentBean {
    private String CommentId;
    private String TicketId;
    private String CommentText;
    private String CommentedBy;

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getCommentText() {
        return CommentText;
    }

    public void setCommentText(String commentText) {
        CommentText = commentText;
    }

    public String getCommentedBy() {
        return CommentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        CommentedBy = commentedBy;
    }

    public String getCommentedOn() {
        return CommentedOn;
    }

    public void setCommentedOn(String commentedOn) {
        CommentedOn = commentedOn;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getCommentedById() {
        return CommentedById;
    }

    public void setCommentedById(String commentedById) {
        CommentedById = commentedById;
    }

    private String CommentedOn;
    private String ProfilePhoto;
    private String CommentedById;

}
