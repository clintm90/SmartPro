package com.trackcell.smartpro;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class Main extends Activity implements ActionBar.TabListener
{
    DBSmartPro mDBSmartPro;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Context parent;
    private ListView mResourcesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        parent = getApplicationContext();

        setContentView(R.layout.activity_main);

        mDBSmartPro = new DBSmartPro(getApplicationContext(), "SmartPro.db", null, 1, null);

        //region first sqlite implement
        /*mDatabase = openOrCreateDatabase("SmartPro.db", MODE_APPEND, null);
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS \"Resources\" (\"ID\" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , \"Name\" VARCHAR NOT NULL  UNIQUE , \"Description\" VARCHAR, \"Job\" VARCHAR, \"isCustomer\" BOOL NOT NULL  DEFAULT false, \"VCF\" BLOB)");

        //mDatabase.execSQL("INSERT INTO \"Resources\" VALUES (0,\"John Loizeau\",\"Mécanicien\",\"Collaborateur\",\"false\",null);");
        Cursor result = mDatabase.rawQuery("SELECT * FROM \"Resources\";", null);

        while(result.moveToNext())
        {
            Toast.makeText(getApplicationContext(), result.getString(2), Toast.LENGTH_SHORT).show();
        }*/
        //endregion

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.MainContent);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);

                switch (position)
                {
                    case 1:
                        break;
                }
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    public String ShowCalendar()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        final View modelDialogCalendar = getLayoutInflater().inflate(R.layout.model_dialogcalendar, null);

        final DatePicker mDialogCalendarPicker = (DatePicker)modelDialogCalendar.findViewById(R.id.model_dialogcalendar_picker);

        alertDialog.setView(modelDialogCalendar);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        alertDialog.create();
        alertDialog.show();

        return null;
    }

    public void GotoAbout(MenuItem item)
    {
        Intent mAbout = new Intent(this, About.class);
        startActivityForResult(mAbout, 1);
    }

    public void GotoNewProject(MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelNewProject = getLayoutInflater().inflate(R.layout.model_newproject, null);

        if(item == null)
        {
            ((EditText)modelNewProject.findViewById(R.id.model_newproject_name)).setError(getString(R.string.fillfield));
        }

        alertDialogBuilder.setView(modelNewProject);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(((EditText)modelNewProject.findViewById(R.id.model_newproject_name)).getText().toString().trim().equals(""))
                {
                    GotoNewProject(null);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), Project.class);
                    intent.putExtra("id", -1);
                    intent.putExtra("name", ((TextView) modelNewProject.findViewById(R.id.model_newproject_name)).getText().toString());
                    startActivityForResult(intent, 1);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), null);
        //region old OnShowListener
        /*alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialog)
            {
                Toast.makeText(getApplicationContext(), "salut", Toast.LENGTH_SHORT).show();
                Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ((TextView) modelNewProject.findViewById(R.id.model_resourcesList_name)).setError("salut");
                        ListView mResourceList = (ListView) findViewById(R.id.ResourcesList);
                        /*ResourcesListAdapter mResourceListAdapter = (ResourcesListAdapter) mResourceList.getAdapter();
                        mResourceListAdapter.add(new EnumResource(getApplicationContext(), "salut", "salut", false, 1));
                        mResourceList.setAdapter(mResourceListAdapter);*/
/*                        alertDialog.cancel();
                    }
                });
            }
        });*/
        //endregion
        alertDialogBuilder.show();
    }

    public void GotoNewPerson(final MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelNewPerson = getLayoutInflater().inflate(R.layout.model_newresource, null);

        if(item == null)
        {
            ((EditText)modelNewPerson.findViewById(R.id.model_newresource_name)).setError(getString(R.string.fillfield));
        }

        alertDialogBuilder.setView(modelNewPerson);
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(((EditText)modelNewPerson.findViewById(R.id.model_newresource_name)).getText().toString().trim().equals(""))
                {
                    GotoNewPerson(null);
                }
                else
                {
                    String type = "false";
                    if(((Spinner)modelNewPerson.findViewById(R.id.model_newresource_job)).getSelectedItem().toString().equals(getString(R.string.client)))
                    {
                        type = "true";
                    }
                    mDBSmartPro.NewResource(((EditText) modelNewPerson.findViewById(R.id.model_newresource_name)).getText().toString(), ((EditText) modelNewPerson.findViewById(R.id.model_newresource_description)).getText().toString(), ((Spinner)modelNewPerson.findViewById(R.id.model_newresource_job)).getSelectedItem().toString(), type, "none");
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mViewPager.setCurrentItem(1);
                }
            }
        });
        alertDialogBuilder.setNeutralButton(getString(R.string.importcontact), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, 0);
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void GotoNewTask(MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelNewTask = getLayoutInflater().inflate(R.layout.model_newtask, null);

        if(item == null)
        {
            ((EditText)modelNewTask.findViewById(R.id.model_newtask_name)).setError(getString(R.string.fillfield));
        }

        alertDialogBuilder.setView(modelNewTask);
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(((EditText)modelNewTask.findViewById(R.id.model_newtask_name)).getText().toString().trim().equals(""))
                {
                    GotoNewTask(null);
                }
                else
                {
                    DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                    mDBSmartPro.NewTask(false, ((EditText) modelNewTask.findViewById(R.id.model_newtask_name)).getText().toString(), ((EditText) modelNewTask.findViewById(R.id.model_newtask_description)).getText().toString(), dateFormat.format(new Date()));
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mViewPager.setCurrentItem(2);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void GotoCalendar(MenuItem item)
    {
        ShowCalendar();
        /*Intent intent = new Intent(Main.this, Calendar.class);
        startActivityForResult(intent, 1);*/
    }

    public void UpgradeDatabase(MenuItem item)
    {
        mDBSmartPro.onUpgrade(mDBSmartPro.getWritableDatabase(), 1, 2);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
            else
            {
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance)
    {
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    public boolean onSearchRequested()
    {
        return super.onSearchRequested();
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    public static void InflateResourceItem(final Context context, final TextView mResourcesListState, final int id)
    {
        AsyncTask<Void, Void, Object[]> mLoadResourceStateTask = new AsyncTask<Void, Void, Object[]>()
        {
            @Override
            protected void onPreExecute()
            {
            }

            @Override
            protected Object[] doInBackground(Void... params)
            {
                DBSmartPro mDBSmartPro = new DBSmartPro(context, "SmartPro.db", null, 1, null);
                return mDBSmartPro.GetResourceByID(id);
            }

            @Override
            protected void onPostExecute(Object[] input)
            {
                mResourcesListState.setText(R.string.active);
                mResourcesListState.setBackgroundResource(R.drawable.balloon_blue);
                mResourcesListState.getBackground().setAlpha(128);
            }
        };
        mLoadResourceStateTask.execute();
    }
}