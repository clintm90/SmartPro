package com.trackcell.smartpro;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBTask extends SQLiteOpenHelper
{
    private Context mContext;

    public DBTask(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Task\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL , \"Description\" VARCHAR, \"Date\" DATETIME NOT NULL  DEFAULT CURRENT_DATE, \"Ended\" BOOL NOT NULL  DEFAULT false);");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS \"Task\";");
        onCreate(db);
    }

    public boolean NewTask(String name, String description, String date)
    {
        try
        {
            SQLiteDatabase mDatabase = getWritableDatabase();
            mDatabase.execSQL("INSERT INTO \"Task\" VALUES (NULL,\""+name+"\",\""+description+"\",\""+date+"\",\"false\");");
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public List<EnumTask> GetTasks()
    {
        List<EnumTask> mRTS = new ArrayList<EnumTask>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Task\";", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumTask(mContext, false, result.getString(1), result.getString(2), new Date(0)));
        }

        return mRTS;
    }
}
