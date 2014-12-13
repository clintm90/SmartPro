package com.trackcell.smartpro;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBSmartPro extends SQLiteOpenHelper
{
    private Context mContext;

    public DBSmartPro(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
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
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Task\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL , \"Description\" VARCHAR, \"Date\" DATETIME NOT NULL  DEFAULT CURRENT_DATE, \"Ended\" BOOL NOT NULL  DEFAULT false);");
            //db.execSQL("CREATE TABLE IF NOT EXISTS \"AssignProjectResource\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"ProjectID\" INTEGER NOT NULL , \"ResourceID\" INTEGER NOT NULL )");
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
        //db.execSQL("DROP TABLE IF EXISTS \"AssignProjectResource\";");
        db.execSQL("DROP TABLE IF EXISTS \"Task\";");
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

    public boolean NewTask(boolean isEnded, String name, String description, Date date)
    {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext.getApplicationContext());
        try
        {
            SQLiteDatabase mDatabase = getWritableDatabase();
            mDatabase.execSQL("INSERT INTO \"Task\" VALUES (NULL,\""+name+"\",\""+description+"\",\""+dateFormat.format(date)+"\",\""+Boolean.valueOf(isEnded)+"\");");
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
            mRTS.add(new EnumTask(mContext, result.getInt(0), Boolean.valueOf(result.getString(4)), result.getString(1), result.getString(2), new Date(12313123)));
        }

        return mRTS;
    }

    public List<EnumResource> GetCustomers()
    {
        List<EnumResource> mRTS = new ArrayList<EnumResource>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\" WHERE isCustomer=\"true\";", null);

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
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\" WHERE isCustomer=\"false\";", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumResource(mContext, result.getString(1), result.getString(2), true, 0));
        }

        return mRTS;
    }

    public void RemoveTask(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        mDatabase.execSQL("DELETE FROM \"Task\" WHERE ID=\""+String.valueOf(id)+"\";");
    }
}
