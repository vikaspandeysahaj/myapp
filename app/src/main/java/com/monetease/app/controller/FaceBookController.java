package com.monetease.app.controller;

import android.content.Context;

import com.monetease.app.listener.UserProfileListener;
import com.monetease.app.service.FaceBookService;

/**
 * Created by vikas on 08/01/16.
 */
public class FaceBookController {

    private FaceBookService faceBookService;

    public FaceBookController(Context context){
        if(faceBookService==null){
            faceBookService = new FaceBookService(context);
        }
    }

    public void fetchFaceBookUserProfile(UserProfileListener listener) {
        faceBookService.fetchFaceBookUserProfile(listener);
    }
}
