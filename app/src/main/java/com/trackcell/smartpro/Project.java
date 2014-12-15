package com.trackcell.smartpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;

public class Project extends Activity
{
    private DBSmartPro mDBSmartPro;
    private int mID;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mDBSmartPro = new DBSmartPro(getApplicationContext(), "SmartPro.db", null, 1, null);

        setContentView(R.layout.activity_project);

        mID = getIntent().getExtras().getInt("id");
        mName = getIntent().getExtras().getString("name");

        setTitle(mName);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        //TODO: implement loading project
        if(mID != -1)
        {
            Object[] rts = mDBSmartPro.GetProjectByID(mID);
        }

        //region old chart
        Line l = new Line();
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
        li.setLineToFill(0);
        //endregion
    }

    public void CloseActivity(MenuItem item)
    {
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
                CloseActivity(null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
