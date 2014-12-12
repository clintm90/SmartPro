package com.trackcell.smartpro;

import android.content.Context;

import java.io.Serializable;
import java.util.Date;

public class EnumTask implements Serializable
{
    public Context mContext;
    public boolean isEnd = false;
    public String Name;
    public String Description;
    public Date Date;

    public EnumTask(Context context, boolean ended, String name, String description, Date date)
    {
        mContext = context;
        isEnd = ended;
        Name = name;
        Description = description;
        Date = date;
    }
}
