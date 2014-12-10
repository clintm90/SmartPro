package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumProject implements Serializable
{
    public Context mContext;
    public String Name;
    public String Description;
    public int State;

    public final static int CURRENT = 0;
    public final static int WAIT = 1;
    public final static int LATE = 2;

    public EnumProject(Context context, String name, String description, int state)
    {
        mContext = context;
        Name = name;
        Description = description;
        State = state;
    }
}
