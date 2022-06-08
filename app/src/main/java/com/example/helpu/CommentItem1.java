package com.example.helpu;

public class CommentItem1 {
    private String comment;
    private String authorName;
    private String authorUid;
    private String id;

    public CommentItem1(String comment, String authorName, String authorUid, String id){
        this.comment=comment;
        this.authorName=authorName;
        this.authorUid=authorUid;
        this.id=id;
    }
    public String getComment(){
        return this.comment;
    }
    public String getAuthorName(){return this.authorName;}
    public String getAuthorUid(){return  this.authorUid;}
    public String getId(){
        return  this.id;
    }
}
