package com.example.gp;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Post {

    long PostPicCount;
    String PostTexts, docId, PostAuthor, PostGroup;
    Date PostTimes;

    public Post(){}

    public Post (long postPicCount, String postTexts, Date postTimes, String docId, String postAuthor, String postGroup) {
        this.PostPicCount = postPicCount;
        this.PostTexts = postTexts;
        this.PostTimes = postTimes;
        this.docId = docId;
        this.PostAuthor = postAuthor;
        this.PostGroup = postGroup;
    }

    public String getPostGroup() {
        return PostGroup;
    }

    public void setPostGroup(String postGroup) {
        this.PostGroup = postGroup;
    }

    public long getPostPicCount() {
        return PostPicCount;
    }

    public void setPostPicCount(long postPicCount) {
        this.PostPicCount = postPicCount;
    }

    public String getPostTexts() {
        return PostTexts;
    }

    public void setPostTexts(String postTexts) {
        this.PostTexts = postTexts;
    }

    public String getPostAuthor() {
        return PostAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.PostAuthor = postAuthor;
    }

    public Date getPostTimes() {
        return PostTimes;
    }

    public void setPostTimes(Date postTimes) {
        this.PostTimes = postTimes;
    }

    @DocumentId
    public String getDocId() {
        return docId;
    }

    @DocumentId
    public void setDocId(String docId) {
        this.docId = docId;
    }
}
