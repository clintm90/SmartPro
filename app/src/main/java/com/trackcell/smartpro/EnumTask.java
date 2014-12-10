package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumTask implements Serializable
{
    public Context mContext;
    public String Name;
    public String Description;

    public EnumTask(Context context, String name, String description)
    {
        mContext = context;
        Name = name;
        Description = description;
    }
}
