package com.monetease.app.helper;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vikas on 14/01/16.
 */
public class UiHelper {
    private Context context;

    public UiHelper(Context context){
        this.context = context;
    }

    public TextDrawable getTextDrawableWithRadius(String text, int radius, int fontSize) {
        String initial = "N";
        if(text.length()>0) {
            initial = String.valueOf(text.charAt(0));
        }
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getColor(text);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(fontSize)
                .bold()
                .endConfig()
                .buildRoundRect(initial.toUpperCase(), color1, radius);
        return drawable;
    }

    public String formatDateTime(Date sentAt) {

        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
        return format.format(sentAt);
    }

}
