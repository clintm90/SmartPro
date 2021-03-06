package com.trackcell.smartpro;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBSmartPro extends SQLiteOpenHelper
{
    private Context mContext;

    public DBSmartPro(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    //TODO: use this method
    public void Init()
    {
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS \"Projects\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"StartDate\" VARCHAR NOT NULL  DEFAULT CURRENT_DATE, \"DueDate\" VARCHAR NOT NULL , \"Progress\" INTEGER NOT NULL );");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS \"Resources\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"Job\" VARCHAR, \"isCustomer\" BOOL NOT NULL  DEFAULT false, \"VCF\" BLOB);");
        this.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS \"Tasks\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL , \"Description\" VARCHAR, \"Date\" VARCHAR NOT NULL  DEFAULT CURRENT_DATE, \"Ended\" BOOL NOT NULL  DEFAULT false);");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Projects\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"StartDate\" VARCHAR NOT NULL  DEFAULT CURRENT_DATE, \"DueDate\" VARCHAR NOT NULL , \"Progress\" INTEGER NOT NULL );");
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Resources\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"Job\" VARCHAR, \"isCustomer\" BOOL NOT NULL  DEFAULT false, \"VCF\" BLOB);");
            db.execSQL("CREATE TABLE IF NOT EXISTS \"Tasks\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL , \"Description\" VARCHAR, \"Date\" VARCHAR NOT NULL  DEFAULT CURRENT_DATE, \"Ended\" BOOL NOT NULL  DEFAULT false);");
            db.execSQL("CREATE TABLE IF NOT EXISTS \"AssignProjectResource\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"ResourceID\" INTEGER NOT NULL , \"ProjectID\" INTEGER NOT NULL );");
            db.execSQL("CREATE TABLE IF NOT EXISTS \"AssignProjectTask\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"TaskID\" INTEGER NOT NULL , \"ProjectID\" INTEGER NOT NULL )");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS \"Projects\";");
        db.execSQL("DROP TABLE IF EXISTS \"Resources\";");
        db.execSQL("DROP TABLE IF EXISTS \"Task\";");
        onCreate(db);
    }

    public boolean NewProject(String name, String description, String startDate, String dueDate, String progress)
    {
        try
        {
            SQLiteDatabase mDatabase = getWritableDatabase();
            mDatabase.execSQL("INSERT INTO \"Projects\" VALUES (NULL,\"" + name + "\",\"" + description + "\",\"" + startDate + "\",\"" + dueDate + "\",\"" + progress + "\");");
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
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

    public boolean NewTask(boolean isEnded, String name, String description, String date)
    {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext.getApplicationContext());
        try
        {
            SQLiteDatabase mDatabase = getWritableDatabase();
            mDatabase.execSQL("INSERT INTO \"Task\" VALUES (NULL,\""+name+"\",\""+description+"\",\""+date+"\",\""+Boolean.valueOf(isEnded)+"\");");
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    //TODO: complete method
    public List<EnumProject> GetProjects(boolean type)
    {
        List<EnumProject> mRTS = new ArrayList<EnumProject>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Projects\";", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumProject(mContext, result.getInt(0), result.getString(1), result.getString(2), EnumProject.CURRENT));
        }

        return mRTS;
    }

    //TODO: complete
    public Object[] GetProjectByID(int id)
    {
        Object[] rts = new Object[10];
        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Projects\" WHERE ID="+String.valueOf(id)+";", null);

        result.moveToNext();

        rts[0] = result.getString(1);
        rts[1] = result.getString(2);
        rts[2] = result.getString(3);
        rts[3] = result.getString(4);

        return rts;
    }

    public List<EnumResource> GetResources(final boolean isCustomer)
    {
        List<EnumResource> mRTS = new ArrayList<EnumResource>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\" WHERE isCustomer=\""+String.valueOf(isCustomer)+"\";", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumResource(mContext, result.getInt(0), result.getString(1), result.getString(2), false, 0));
        }

        return mRTS;
    }

    public int GetResourceCountByID(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"AssignProjectResource\" WHERE ResourceID="+String.valueOf(id)+";", null);

        result.moveToNext();

        return result.getCount();
    }

    public int GetTaskCountByID(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"AssignProjectTask\" WHERE ProjectID="+String.valueOf(id)+";", null);

        result.moveToNext();

        return result.getCount();
    }

    public List<String> GetProjectsNamesByID(int id)
    {
        List<String> rts = new ArrayList<String>();
        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT Projects.Name FROM AssignProjectResource JOIN Projects, Resources ON Projects.ID=AssignProjectResource.ProjectID AND Resources.ID=AssignProjectResource.ResourceID AND Resources.ID="+String.valueOf(id)+";", null);

        while (result.moveToNext())
        {
            rts.add(result.getString(0));
        }

        result.moveToNext();

        return rts;
    }

    public List<EnumTask> GetTasks()
    {
        List<EnumTask> mRTS = new ArrayList<EnumTask>();

        SQLiteDatabase mDatabase = getReadableDatabase();
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Tasks\";", null);

        while(result.moveToNext())
        {
            mRTS.add(new EnumTask(mContext, result.getInt(0), Boolean.valueOf(result.getString(4)), result.getString(1), result.getString(2), result.getString(3)));
        }

        return mRTS;
    }

    public void RemoveProject(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        mDatabase.execSQL("DELETE FROM \"Projects\" WHERE ID=\""+String.valueOf(id)+"\";");
    }

    public void RemoveResource(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        mDatabase.execSQL("DELETE FROM \"Resources\" WHERE ID=\""+String.valueOf(id)+"\";");
    }

    public void RemoveTask(int id)
    {
        SQLiteDatabase mDatabase = getReadableDatabase();
        mDatabase.execSQL("DELETE FROM \"Tasks\" WHERE ID=\""+String.valueOf(id)+"\";");
    }

    //TODO: complete method
    public void AlterProject(int id, String name, String description, String startDate, String dueDate, String progress)
    {
        SQLiteDatabase mDatabase = getWritableDatabase();
        mDatabase.execSQL("UPDATE \"Projects\" SET \"Name\" = \""+name+"\", \"Description\" = \""+description+"\" WHERE \"ID\" = "+id+";");
    }

    public void AlterResource(int id, String name, String description)
    {
        SQLiteDatabase mDatabase = getWritableDatabase();
        mDatabase.execSQL("UPDATE \"Resources\" SET \"Name\" = \""+name+"\", \"Description\" = \""+description+"\" WHERE \"ID\" = "+id+";");
    }

    public void AlterTask(int id, String name, String description, boolean isEnded)
    {
        SQLiteDatabase mDatabase = getWritableDatabase();
        mDatabase.execSQL("UPDATE \"Tasks\" SET \"Name\" = \""+name+"\", \"Description\" = \""+description+"\", \"Ended\" = \""+String.valueOf(isEnded)+"\" WHERE \"ID\" = "+id+";");
    }
}
