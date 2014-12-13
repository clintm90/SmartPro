package com.trackcell.smartpro;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

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

    //region ShowCalendar
    public String ShowCalendar()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        final View modelDialogCalendar = getLayoutInflater().inflate(R.layout.model_dialogcalendar, null);

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
    //endregion

    public void GotoAbout(MenuItem item)
    {
        Intent mAbout = new Intent(this, About.class);
        startActivityForResult(mAbout, 1);
    }

    public void GotoNewProject(MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelNewProject = getLayoutInflater().inflate(R.layout.model_newproject, null);

        alertDialogBuilder.setView(modelNewProject);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(getApplicationContext(), Project.class);
                intent.putExtra("name", ((TextView) modelNewProject.findViewById(R.id.model_newproject_name)).getText().toString());
                startActivityForResult(intent, 1);
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), null);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
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
                        alertDialog.cancel();
                    }
                });
            }
        });
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
                    mDBSmartPro.NewTask(false, ((EditText) modelNewTask.findViewById(R.id.model_newtask_name)).getText().toString(), ((EditText) modelNewTask.findViewById(R.id.model_newtask_description)).getText().toString(), new Date(1418474890));
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
        Intent intent = new Intent(Main.this, Calendar.class);
        startActivityForResult(intent, 1);
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

    public static class PlaceholderFragment extends Fragment
    {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment()
        {
        }

        public static PlaceholderFragment newInstance(int sectionNumber)
        {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setHasOptionsMenu(true);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:
                    inflater.inflate(R.menu.projects, menu);
                    break;

                case 2:
                    inflater.inflate(R.menu.resources, menu);
                    break;

                case 3:
                    inflater.inflate(R.menu.task, menu);
                    break;
            }
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
        {
            final DBSmartPro mDBSmartPro = new DBSmartPro(getActivity().getApplicationContext(), "SmartPro.db", null, 1, null);
            SQLiteDatabase db = mDBSmartPro.getWritableDatabase();
            mDBSmartPro.onCreate(db);

            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:

                    rootView = inflater.inflate(R.layout.fragment_project, container, false);
                    ExpandableListView mProjectList = (ExpandableListView) rootView.findViewById(R.id.ProjectList);
                    TextView mProjectListEmptyLabel = (TextView) rootView.findViewById(R.id.projectListEmptyLabel);

                    List<String> listDataHeader2 = new ArrayList<String>();
                    HashMap<String, List<EnumProject>> listDataChild2 = new HashMap<String, List<EnumProject>>();

                    ProjectExpandableListAdapter mProjectExpandableListAdapter = new ProjectExpandableListAdapter(getActivity().getApplicationContext(), listDataHeader2, listDataChild2);

                    listDataHeader2.add(getString(R.string.currentproject));
                    listDataHeader2.add(getString(R.string.lateproject));

                    List<EnumProject> current = new ArrayList<EnumProject>();
                    current.add(new EnumProject(getActivity().getApplicationContext(), "First Sample Project", "My first application", EnumProject.CURRENT));

                    List<EnumProject> users2 = new ArrayList<EnumProject>();
                    users2.add(new EnumProject(getActivity().getApplicationContext(), "Event System", "A Best application", EnumProject.WAIT));
                    users2.add(new EnumProject(getActivity().getApplicationContext(), "Vertigo", "A free project managemenr", EnumProject.LATE));

                    listDataChild2.put(listDataHeader2.get(0), current);
                    listDataChild2.put(listDataHeader2.get(1), users2);

                    mProjectList.setAdapter(mProjectExpandableListAdapter);
                    mProjectList.expandGroup(0);
                    mProjectList.expandGroup(1);

                    mProjectList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                    {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                        {
                            Intent intent = new Intent(getActivity(), Project.class);
                            intent.putExtra("name", ((EnumProject) v.getTag()).Name);
                            getActivity().startActivityForResult(intent, 1);
                            return false;
                        }
                    });
                    mProjectList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
                    {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                        {
                            return true;
                        }
                    });
                    mProjectList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                    {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
                            {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                                alertDialog.setItems(new String[]{getString(R.string.action_shareproject), getString(R.string.action_deleteproject)}, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        switch (which)
                                        {
                                            case 0:
                                                break;

                                            case 1:
                                                AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(getActivity());
                                                alertDialog.setTitle(getString(R.string.action_deleteproject));
                                                alertDialog.setMessage(getString(R.string.action_woulddelete));
                                                alertDialog.setCancelable(false);
                                                alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                    }
                                                });
                                                alertDialog.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which)
                                                    {
                                                    }
                                                });
                                                alertDialogDelete.create();
                                                alertDialogDelete.show();
                                                break;
                                        }
                                    }
                                });
                                alertDialog.create();
                                alertDialog.show();
                            }
                            return true;
                        }
                    });
                    return rootView;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_resources, container, false);
                    ExpandableListView mResourcesList = (ExpandableListView) rootView.findViewById(R.id.ResourcesList);
                    TextView mResourcesListEmptyLabel = (TextView) rootView.findViewById(R.id.resourcesListEmptyLabel);

                    List<String> listDataHeader = new ArrayList<String>();
                    HashMap<String, List<EnumResource>> listDataChild = new HashMap<String, List<EnumResource>>();

                    ResourcesExpandableListAdapter mResourcesExpandableListAdapter = new ResourcesExpandableListAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild);

                    List<EnumResource> mCustomers = mDBSmartPro.GetCustomers();
                    List<EnumResource> mUsers = mDBSmartPro.GetUsers();

                    if(mCustomers.size() == 0 && mUsers.size() == 0)
                    {
                        //mResourcesList.setAdapter(mResourcesExpandableListAdapter);
                        mResourcesListEmptyLabel.setVisibility(View.VISIBLE);
                        mResourcesList.setVisibility(View.INVISIBLE);
                    }

                    listDataHeader.add(getString(R.string.clients));
                    listDataHeader.add(getString(R.string.users));

                    listDataChild.put(listDataHeader.get(0), mCustomers);
                    listDataChild.put(listDataHeader.get(1), mUsers);

                    mResourcesList.setAdapter(mResourcesExpandableListAdapter);
                    mResourcesList.expandGroup(0);
                    mResourcesList.expandGroup(1);

                    mResourcesList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
                    {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
                        {
                            return true;
                        }
                    });
                    mResourcesList.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                    {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, final View v, int groupPosition, int childPosition, long id)
                        {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            final View modelResource = inflater.inflate(R.layout.model_resource, null);
                            alertDialog.setView(modelResource);
                            alertDialog.setNegativeButton(getString(R.string.action_delete), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.MainContent);
                                    mDBSmartPro.RemoveResource(((EnumTask) v.getTag()).ID);
                                    mViewPager.setAdapter(mViewPager.getAdapter());
                                    mViewPager.setCurrentItem(1);
                                }
                            });
                            alertDialog.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                            /*Intent mResourceIntent = new Intent(getActivity(), Resource.class);
                            mResourceIntent.putExtra("name", ((EnumResource) v.getTag()).Name);
                            mResourceIntent.putExtra("id", "1");
                            getActivity().startActivityForResult(mResourceIntent, 1);*/
                            return false;
                        }
                    });
                    mResourcesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                    {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
                            {
                            }
                            return true;
                        }
                    });
                    return rootView;

                case 3:
                    rootView = inflater.inflate(R.layout.fragment_task, container, false);
                    final ListView mTaskList = (ListView) rootView.findViewById(R.id.TaskList);
                    TextView mTaskListEmptyLabel = (TextView) rootView.findViewById(R.id.taskListEmptyLabel);

                    List<EnumTask> mTask = mDBSmartPro.GetTasks();

                    if(mTask.size() == 0)
                    {
                        mTaskListEmptyLabel.setVisibility(View.VISIBLE);
                    }

                    final TaskListAdapter mTaskListAdapter = new TaskListAdapter(getActivity().getApplicationContext(), mTask);

                    mTaskList.setAdapter(mTaskListAdapter);
                    mTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                        {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            final View modelTask = inflater.inflate(R.layout.model_task, null);
                            alertDialog.setView(modelTask);
                            alertDialog.setNegativeButton(getString(R.string.action_delete), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.MainContent);
                                    mDBSmartPro.RemoveTask(((EnumTask) view.getTag()).ID);
                                    mViewPager.setAdapter(mViewPager.getAdapter());
                                    mViewPager.setCurrentItem(2);
                                }
                            });
                            alertDialog.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                        }
                    });
                    return rootView;

                default:
                    return rootView;
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale l = Locale.getDefault();
            switch (position)
            {
                case 0:
                    return getString(R.string.title_section1);
                case 1:
                    return getString(R.string.title_section2);
                case 2:
                    return getString(R.string.title_section3);
            }
            return null;
        }
    }
}