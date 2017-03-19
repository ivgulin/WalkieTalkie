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
    private String receiver;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String text, String author, Date createDate, String receiver) {
        this.text = text;
        this.author = author;
        this.createDate = createDate;
        this.receiver = receiver;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
