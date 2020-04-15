package com.example.myapplication.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.R;

public class Trailer implements Parcelable {

    //    private int id;
    private String name;
    private String site;
    private String key;


    public Trailer(String name, String site, String key) {
//        this.id = id;
        this.name = name;
        this.site = site;
        this.key = key;
    }

    private Trailer(Parcel parcel) {
        name = parcel.readString();
        site = parcel.readString();
        key = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(key);
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
