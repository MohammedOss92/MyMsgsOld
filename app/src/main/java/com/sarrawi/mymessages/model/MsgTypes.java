package com.sarrawi.mymessages.model;

public class MsgTypes {
    private int id;
    private String name;


    public MsgTypes(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public MsgTypes(String name) {
        this.name = name;
    }
    public MsgTypes ()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
