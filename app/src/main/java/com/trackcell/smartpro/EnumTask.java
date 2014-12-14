package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;

public class EnumTask implements Serializable
{
    public Context mContext;
    public int ID;
    public boolean isEnd = false;
    public String Name;
    public String Description;
    public String Date;

    public EnumTask(Context context, int id, boolean ended, String name, String description, String date)
    {
        mContext = context;
        ID = id;
        isEnd = ended;
        Name = name;
        Description = description;
        Date = date;
    }
}
