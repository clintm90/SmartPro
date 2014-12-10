package com.trackcell.smartpro;

import android.content.Context;
import android.widget.TextView;

import java.io.Serializable;

public class EnumProject implements Serializable
{
    public Context mContext;
    public TextView State;

    public String Name;
    public String Description;

    public final static int CURRENT = 0;
    public final static int WAIT = 1;
    public final static int LATE = 2;

    public EnumProject(Context context, String name, String description, int state)
    {
        mContext = context;
        Name = name;
        Description = description;
    }
}
