package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumProject implements Serializable
{
    public Context mContext;
    public int ID;
    public String Name;
    public String Description;
    public int State;

    public final static int CURRENT = 0;
    public final static int WAIT = 1;
    public final static int LATE = 2;

    public EnumProject(Context context, int id, String name, String description, int state)
    {
        mContext = context;
        ID = id;
        Name = name;
        Description = description;
        State = state;
    }
}
