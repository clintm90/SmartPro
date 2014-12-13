package com.trackcell.smartpro;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBResources extends SQLiteOpenHelper
{
    private Context mContext;
    private SQLiteDatabase mDatabase;

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
            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"Resources\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"Job\" VARCHAR, \"isCustomer\" BOOL NOT NULL  DEFAULT false, \"VCF\" BLOB)");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        mDatabase.execSQL("DROP TABLE IF EXISTS \"Resources\";");
    }

    public void NewResource(String name)
    {
        mDatabase.execSQL("INSERT INTO \"Resources\" VALUES (0,\"John Loizeau\",\"Mécanicien\",\"Collaborateur\",\"false\",null);");
    }

    public void Populate()
    {
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\";", null);

        while(result.moveToNext())
        {
            Toast.makeText(mContext, result.getString(2), Toast.LENGTH_SHORT).show();
        }
    }
}
