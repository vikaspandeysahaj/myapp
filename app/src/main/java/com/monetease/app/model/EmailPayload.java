package com.monetease.app.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;

/**
 * Created by vikas on 13/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailPayload extends Serializable<EmailPayload>{

    private String id;
    private String senderName;
    private String senderId;
    private String snippet;
    private String payload;
    private String receivedTime;
    private String subject;
    private boolean isSelected;

    @Override
    public String serialize() throws IOException {
        String json = mapper.writeValueAsString(this);
        return json;
    }

    @Override
    public EmailPayload deserialize(String json) throws IOException {
        EmailPayload smsPayload = mapper.readValue(json, EmailPayload.class);
        return smsPayload;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
