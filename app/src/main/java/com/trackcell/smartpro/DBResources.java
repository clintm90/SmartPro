package com.trackcell.smartpro;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBResources extends SQLiteOpenHelper
{
    private Context mContext;

    public DBResources(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Resources\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"Job\" VARCHAR, \"isCustomer\" BOOL NOT NULL  DEFAULT false, \"VCF\" BLOB)");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS \"Resources\";");
        onCreate(db);
    }

    public boolean NewResource(String name, String description, String job, String isConsumer, String VCF)
    {
        try
        {
            SQLiteDatabase mDatabase = getWritableDatabase();
            mDatabase.execSQL("INSERT INTO \"Resources\" VALUES (NULL,\"" + name + "\",\"" + description + "\",\"" + job + "\",\"" + isConsumer + "\",\"" + VCF + "\");");
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public List<EnumResource> GetCustomers()
    {
        List<EnumResource> mRTS = new ArrayList<EnumResource>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\" WHERE isCustomer=true;", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumResource(mContext, result.getString(1), result.getString(2), true, 0));
        }

        return mRTS;
    }

    public List<EnumResource> GetUsers()
    {
        List<EnumResource> mRTS = new ArrayList<EnumResource>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\" WHERE isCustomer=false;", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumResource(mContext, result.getString(1), result.getString(2), true, 0));
        }

        return mRTS;
    }
}
