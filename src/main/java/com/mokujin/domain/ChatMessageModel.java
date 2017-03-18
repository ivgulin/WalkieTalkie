package com.mokujin.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Date;

@NodeEntity
public class ChatMessageModel {
    @GraphId
    private Long id;

    private String text;
    private String author;
    private Date createDate;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String text, String author, Date createDate) {
        this.text = text;
        this.author = author;
        this.createDate = createDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "ChatMessageModel{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
