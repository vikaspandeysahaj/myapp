package com.monetease.app.controller;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.monetease.app.listener.UserProfileListener;
import com.monetease.app.model.User;

/**
 * Created by vikas on 10/01/16.
 */
public class GoogleController {

    public void fetchGoogleUserProfile(GoogleApiClient mGoogleApiClient, UserProfileListener listener) {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                User user = new User();
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                user.setName(currentPerson.getDisplayName());
                user.setFirst_name(currentPerson.getNickname());
                user.setGender(String.valueOf(currentPerson.getGender()));
                user.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
                user.setBirthday(currentPerson.getBirthday());
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + String.valueOf(400);
                user.setProfile_url(personPhotoUrl);
                listener.setUserProfile(user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
