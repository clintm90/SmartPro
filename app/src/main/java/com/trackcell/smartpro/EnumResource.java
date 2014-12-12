package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumResource implements Serializable
{
    public Context mContext;
    public String Name;
    public String Description;
    public boolean State;
    public int Person;

    public EnumResource(Context context, String name, String description, boolean state, int person)
    {
        mContext = context;
        Name = name;
        Description = description;
        State = state;
        Person = person;
    }
}
