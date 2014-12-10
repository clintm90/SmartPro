package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumResource implements Serializable
{
    public Context mContext;
    public String Name;
    public String Description;

    public EnumResource(Context context, String name, String description)
    {
        mContext = context;
        Name = name;
        Description = description;
    }
}
