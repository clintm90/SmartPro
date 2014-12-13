package com.trackcell.smartpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class About extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
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
        getMenuInflater().inflate(R.menu.about, menu);
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
