package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumResource implements Serializable
{
    public Context mContext;
    public int ID;
    public String Name;
    public String Description;
    public boolean State;
    public int Person;

    public EnumResource(Context context, int id, String name, String description, boolean state, int person)
    {
        mContext = context;
        ID = id;
        Name = name;
        Description = description;
        State = state;
        Person = person;
    }
}
