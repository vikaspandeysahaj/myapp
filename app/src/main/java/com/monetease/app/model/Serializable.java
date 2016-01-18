package com.monetease.app.model;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by vikas on 16/07/15.
 */
public abstract class Serializable<T> {

    protected static final ObjectMapper mapper = new ObjectMapper();;

    public Serializable() {
    }

   public abstract String serialize() throws IOException;
    public abstract T deserialize(String json) throws IOException;

}
