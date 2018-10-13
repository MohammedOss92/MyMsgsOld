package com.sarrawi.mymessages.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Msg implements Parcelable {
    private int ID_Msg;
    private String Msg_Name;
    private int ID_Type;
    private int fav;
    private String Msg_Filter;
    private int origPos;

    public Msg ()
    {

    }

    public Msg(int id, String name, int fav,int name_ID,String msg_Filter) {
        this.ID_Msg = id;
        this.Msg_Name = name;
        this.fav = fav;
        this.ID_Type = name_ID;
        this.Msg_Filter = msg_Filter;
    }
    public Msg(int name_ID,String msg_Filter) {
        this.ID_Type = name_ID;
        this.Msg_Filter = msg_Filter;
    }

    public int getID_Msg() {
        return ID_Msg;
    }

    public void setID_Msg(int ID_Msg) {
        this.ID_Msg = ID_Msg;
    }

    public String getMsg_Name() {
        return Msg_Name;
    }

    public void setMsg_Name(String msg_Name) {
        Msg_Name = msg_Name;
    }

    public int getID_Type() {
        return ID_Type;
    }

    public void setID_Type(int ID_Type) {
        this.ID_Type = ID_Type;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getMsg_Filter() {
        return Msg_Filter;
    }

    public void setMsg_Filter(String msg_Filter) {
        Msg_Filter = msg_Filter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public int getOrigPos() {
        return origPos;
    }

    public void setOrigPos(int origPos) {
        this.origPos = origPos;
    }
}
