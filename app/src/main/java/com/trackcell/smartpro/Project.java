package com.trackcell.smartpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Project extends Activity
{
    private DBSmartPro mDBSmartPro;
    private int mID;
    private String mName;
    private String mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mDBSmartPro = new DBSmartPro(getApplicationContext(), "SmartPro.db", null, 1, null);

        setContentView(R.layout.activity_project);

        mID = getIntent().getExtras().getInt("id");
        mName = getIntent().getExtras().getString("name");
        mDescription = getIntent().getExtras().getString("description");

        setTitle(mName);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        //TODO: implement loading project
        if(mID != -1)
        {
            Object[] rts = mDBSmartPro.GetProjectByID(mID);
        }

        //region old chart
        /*Line l = new Line();
        LinePoint p = new LinePoint();
        p.setX(0);
        p.setY(5);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(8);
        p.setY(8);
        l.addPoint(p);
        p = new LinePoint();
        p.setX(10);
        p.setY(4);
        l.addPoint(p);
        l.setColor(Color.parseColor("#FFBB33"));

        LineGraph li = (LineGraph)findViewById(R.id.graph);
        li.addLine(l);
        li.setRangeY(0, 10);
        li.setLineToFill(0);*/
        //endregion
    }

    public void Save(MenuItem item)
    {
        if(mID != -1)
        {
            mDBSmartPro.AlterProject(mID, "Sample Project", "salut", "2014-12-12", "2014-12-12", "50");
        }
        else
        {
            mDBSmartPro.NewProject(mName, mDescription, "2014-12-12", "2014-12-12", "50");
        }
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        finish();
    }

    public void Delete(MenuItem item)
    {
        mDBSmartPro.RemoveProject(mID);
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK, new Intent().putExtra("result", 1));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_OK, new Intent().putExtra("result", 1));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
