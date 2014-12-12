package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumTask implements Serializable
{
    public Context mContext;
    public boolean isEnd = false;
    public String Name;
    public String Description;

    public EnumTask(Context context, boolean ended, String name, String description)
    {
        mContext = context;
        isEnd = ended;
        Name = name;
        Description = description;
    }
}
