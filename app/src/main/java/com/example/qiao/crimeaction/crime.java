package com.example.qiao.crimeaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by qiao on 2016/2/17.
 */
public class crime {
    private UUID mId;

    private String mTitle;

    private Date mDate;

    private Boolean mSloved = false;

    private static final String JSON_ID= "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";

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
        return jsonObject;
    }

    @Override
    public String toString() {
        return mTitle;
    }


}
