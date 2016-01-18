package com.monetease.app.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;

/**
 * Created by vikas on 10/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsPayload extends Serializable<SmsPayload> {

    private int id;
    private String senderName;
    private String senderNumber;
    private String message;
    private String senderImageUri;
    private boolean isSelected;

    @Override
    public String serialize() throws IOException {
        String json = mapper.writeValueAsString(this);
        return json;
    }

    @Override
    public SmsPayload deserialize(String json) throws IOException {
        SmsPayload smsPayload = mapper.readValue(json, SmsPayload.class);
        return smsPayload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderImageUri() {
        return senderImageUri;
    }

    public void setSenderImageUri(String senderImageUri) {
        this.senderImageUri = senderImageUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
