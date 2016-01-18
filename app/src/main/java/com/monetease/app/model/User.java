package com.monetease.app.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;

/**
 * Created by vikas on 08/01/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Serializable<User> {

    private String first_name;
    private String last_name;
    private String name;
    private String gender;
    private String link;
    private String email;
    private String profile_url;
    private String mobile;
    private String birthday;
    private String address;
    private String provider;
    private String website;

    public User(){

    }

    @Override
    public String serialize() throws IOException {
        String json = mapper.writeValueAsString(this);
        return json;
    }

    @Override
    public User deserialize(String json) throws IOException {
        User user = mapper.readValue(json, User.class);
        return user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

