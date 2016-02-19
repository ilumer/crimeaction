package com.example.qiao.crimeaction;

import java.util.Date;
import java.util.UUID;

/**
 * Created by qiao on 2016/2/17.
 */
public class crime {
    private UUID mId;

    private String mTitle;

    private Date mDate;

    private Boolean mSloved;

    public crime() {
        this.mId = UUID.randomUUID();
        mDate = new Date();
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

    @Override
    public String toString() {
        return mTitle;
    }
}
