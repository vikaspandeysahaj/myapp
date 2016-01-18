package com.monetease.app.service;

import android.content.Context;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.monetease.app.listener.UserProfileListener;
import com.monetease.app.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vikas on 08/01/16.
 */
public class FaceBookService {

    private Context context;

    public FaceBookService(Context context){
        this.context = context;
    }

    public void fetchFaceBookUserProfile(final UserProfileListener listener){
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                AccessToken.getCurrentAccessToken().getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject object = response.getJSONObject();
                            User user = new User();
                            user = user.deserialize(object.toString());
                            fetchFaceBookUserProfileImage(user, listener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "first_name,last_name,name,gender,link,email,birthday,website");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void fetchFaceBookUserProfileImage(final User user, final UserProfileListener listener) {

        GraphRequest requestPhoto = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture",
                null,
                HttpMethod.GET,
                    new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            String url = response.getJSONObject().getJSONObject("data").getString("url");
                            user.setProfile_url(url);
                            listener.setUserProfile(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parametersPhoto = new Bundle();
        parametersPhoto.putString("type", "large");
        parametersPhoto.putString("width", "250");
        parametersPhoto.putString("height", "250");
        parametersPhoto.putString("redirect", "false");
        requestPhoto.setParameters(parametersPhoto);
        requestPhoto.executeAsync();
    }
}
