package com.collegeproject.model.staticModel;

public class CommentModel {
    private String comment;
    private String commentBy;
    private String commentDate;

    public CommentModel(String comment, String commentBy, String commentDate) {
        this.comment = comment;
        this.commentBy = commentBy;
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
