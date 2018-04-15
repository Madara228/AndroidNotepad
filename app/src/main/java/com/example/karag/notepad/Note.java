package com.example.karag.notepad;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.io.Serializable;

public class Note implements Serializable {
    private long mDateTime;
    private String mTitle;
    private String mContent;

    public Note(long dateTime, String title, String content){
        mDateTime = dateTime;
        mTitle = title;
        mContent = content;
    }

    public void setContent(String content){
        mContent = content;
    }
    public void setmDateTime(long dateTime){
        mDateTime = dateTime;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public long getDateTime(){
        return mDateTime;
    }

    public String getTitle(){
        return mTitle;
    }
    public String getContent(){
        return mContent;
    }

    public String getDateTimeFormated(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",
                context.getResources().getConfiguration().locale);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(mDateTime));
    }
}
