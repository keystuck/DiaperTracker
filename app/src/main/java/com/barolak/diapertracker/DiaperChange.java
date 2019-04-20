package com.barolak.diapertracker;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaperChange implements Parcelable {
    public static final String LOG_TAG = DiaperChange.class.getSimpleName();


    public String babyName;
    public Long changeTime;
    public String diaperType;
    public Boolean poopPresent = false;
    public String comments;
    public String timeString;

    public DiaperChange(){}

    public DiaperChange(String babyName, long changeTime, String diaperType, boolean poopPresent, String comments){
        this.babyName = babyName;
        this.changeTime = changeTime;
        this.diaperType = diaperType;
        this.poopPresent = poopPresent;
        this.comments = comments;
        Log.d(LOG_TAG, "creating DiaperChange for " + babyName);

        this.timeString = dateToString(changeTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(babyName);
        dest.writeLong(changeTime);
        dest.writeString(diaperType);
        if (poopPresent){
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(comments);

        Log.d(LOG_TAG, "creating parcel for " + babyName);
    }

    public static final Parcelable.Creator<DiaperChange> CREATOR =
            new Parcelable.Creator<DiaperChange>() {
        public DiaperChange createFromParcel(Parcel in){
            return new DiaperChange(in);
        }

        public DiaperChange[] newArray(int size){
            return new DiaperChange[size];
        }
            };

    private DiaperChange(Parcel in){
        babyName = in.readString();
        changeTime = in.readLong();
        diaperType = in.readString();
        if (in.readInt() == 0){
            poopPresent = false;
        } else {
            poopPresent = true;
        }
        comments = in.readString();

        timeString = dateToString(changeTime);
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    public String getDiaperType() {
        return diaperType;
    }

    public void setDiaperType(String diaperType) {
        this.diaperType = diaperType;
    }

    public Boolean getPoopPresent() {
        return poopPresent;
    }

    public void setPoopPresent(Boolean poopPresent) {
        this.poopPresent = poopPresent;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        String lastChange = timeString
                + "; type: " + diaperType;
        if (poopPresent){
            lastChange += "; poop";
        }
        if (!(comments.isEmpty())) {
            lastChange += "; " + comments;
        } return lastChange;
    }

    public String dateToString(long timeStamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE HH:mm");
        Date date = new Date(timeStamp);

        return simpleDateFormat.format(date);
    }

    public void updateDate(){
        timeString = dateToString(changeTime);
    }

}
