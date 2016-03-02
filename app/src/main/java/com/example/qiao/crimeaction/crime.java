package com.example.qiao.crimeaction;

import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by qiao on 2016/2/17.
 */
public class crime implements Serializable{
    private UUID mId;

    private String mTitle;

    private Date mDate;

    private Boolean mSloved = false;

    private String mimageStore =null;

    private String msuspect;

    private static final String JSON_ID= "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_SUSPECT = "suspect";

    public crime() {
        this.mId = UUID.randomUUID();
        mDate = new Date();
    }

    public crime(JSONObject jsonObject) throws JSONException{
        mId = UUID.fromString((String) jsonObject.get(JSON_ID));
        if (jsonObject.has(JSON_TITLE)) {
            //Returns the value mapped by name if it exists,
            // coercing it if necessary, or throws if no such mapping exists.
            mTitle = jsonObject.getString(JSON_TITLE);
        }
        if (jsonObject.has(JSON_IMAGE)){
            mimageStore = jsonObject.getString(JSON_IMAGE);
        }
        if (jsonObject.has(JSON_SUSPECT)){
            msuspect = jsonObject.getString(JSON_SUSPECT);
        }
        mSloved = jsonObject.getBoolean(JSON_SOLVED);
        mDate = new Date(jsonObject.getString(JSON_DATE));
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public UUID getmId() {
        return mId;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public Boolean getmSloved() {
        return mSloved;
    }

    public void setmSloved(Boolean mSloved) {
        this.mSloved = mSloved;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID,mId.toString());
        jsonObject.put(JSON_DATE,mDate.toString());
        jsonObject.put(JSON_TITLE,mTitle);
        jsonObject.put(JSON_SOLVED,mSloved);
        jsonObject.put(JSON_IMAGE,mimageStore);
        jsonObject.put(JSON_SUSPECT,msuspect);
        //无法直接将FILE存储
        return jsonObject;
    }

    @Override
    public String toString() {
        return mTitle+mSloved;
    }

    public void setImageLocation(String file){
        if (mimageStore!=null){
            new File(mimageStore).delete();
            mimageStore = file;
        }else {
            mimageStore = file;
        }
    }

    public String getMimageLocation(){
        return mimageStore;
    }

    public String getSuspect() {
        return JSON_SUSPECT;
    }

    public void setSuspect(String suspect){
        msuspect = suspect;
    }
}
