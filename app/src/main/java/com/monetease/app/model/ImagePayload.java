package com.monetease.app.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;

/**
 * Created by vikas on 14/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagePayload extends Serializable<ImagePayload> {

    private int id;
    private String uri;
    private String title;
    private String createdDate;
    private boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String serialize() throws IOException {
        String json = mapper.writeValueAsString(this);
        return json;
    }

    @Override
    public ImagePayload deserialize(String json) throws IOException {
        ImagePayload smsPayload = mapper.readValue(json, ImagePayload.class);
        return smsPayload;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
