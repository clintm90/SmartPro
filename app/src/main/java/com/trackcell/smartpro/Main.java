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
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Main extends Activity implements ActionBar.TabListener
{
    private Context parent;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    int CurrentPage = 0;

    private ListView mResourcesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        parent = getApplicationContext();

        setContentView(R.layout.activity_main);

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
                CurrentPage = position;
                actionBar.setSelectedNavigationItem(position);

                switch(position)
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

    public void GotoNewProject(MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelNewProject = getLayoutInflater().inflate(R.layout.model_newproject, null);

        alertDialogBuilder.setView(modelNewProject);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), null);
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
                        ((TextView)modelNewProject.findViewById(R.id.model_taskList_name)).setError("salut");
                        ListView mResourceList = (ListView)findViewById(R.id.ResourcesList);
                        ResourcesListAdapter mResourceListAdapter = (ResourcesListAdapter) mResourceList.getAdapter();
                        mResourceListAdapter.add(new EnumResource(getApplicationContext(), "salut", "salut", false));
                        mResourceList.setAdapter(mResourceListAdapter);
                        alertDialog.cancel();
                    }
                });
            }
        });
        alertDialogBuilder.show();
    }

    public void GotoNewPerson(MenuItem item)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        final View modelRegister = getLayoutInflater().inflate(R.layout.model_newresource, null);
        alertDialogBuilder.setView(modelRegister);
        alertDialogBuilder.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            }
        });
        alertDialogBuilder.setNeutralButton(getString(R.string.importcontact), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(pickContactIntent, 1);
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                ListView mResourceList = (ListView)findViewById(R.id.ResourcesList);
                ResourcesListAdapter mResourceListAdapter = (ResourcesListAdapter) mResourceList.getAdapter();
                mResourceListAdapter.add(new EnumResource(getApplicationContext(), cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), "salut", false));
                mResourceList.setAdapter(mResourceListAdapter);
            }
        }
    }

    @Override
    public boolean onSearchRequested()
    {
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //getMenuInflater().inflate(R.menu.main, menu);
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
            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:
                    inflater.inflate(R.menu.main, menu);
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            final ArrayList<EnumProject> PROJECTLIST = new ArrayList<EnumProject>();
            ArrayList<EnumResource> RESOURCESLIST = new ArrayList<EnumResource>();
            ArrayList<EnumTask> TASKLIST = new ArrayList<EnumTask>();

            View rootView = null;

            switch(getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:

                    rootView = inflater.inflate(R.layout.fragment_project, container, false);
                    ListView mProjectList = ((ListView) rootView.findViewById(R.id.ProjectList));

                    ProjectListAdapter mProjectListAdapter = new ProjectListAdapter(getActivity().getApplicationContext(), PROJECTLIST);
                    mProjectListAdapter.clear();
                    mProjectListAdapter.add(new EnumProject(getActivity().getApplicationContext(), "Sample Project", "My first application", EnumProject.CURRENT));
                    mProjectListAdapter.add(new EnumProject(getActivity().getApplicationContext(), "Event System", "A Best application", EnumProject.WAIT));
                    mProjectListAdapter.add(new EnumProject(getActivity().getApplicationContext(), "Vertigo", "A free project managemenr", EnumProject.LATE));

                    mProjectList.setAdapter(mProjectListAdapter);
                    mProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            //goto Project
                        }
                    });
                    mProjectList.setOnLongClickListener(new View.OnLongClickListener()
                    {
                        @Override
                        public boolean onLongClick(View v)
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getParentFragment().getActivity().getApplicationContext());
                            alertDialog.setTitle("salut");
                            alertDialog.setItems(new String[] {"Afficher le Projet", "Modifier le Projet", "Supprimer le Projet"}, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    switch(which)
                                    {
                                        case 0:
                                            break;
                                    }
                                }
                            });
                            alertDialog.create();
                            alertDialog.show();
                            return false;
                        }
                    });
                    return rootView;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_resources, container, false);
                    ListView mResourcesList = (ListView) rootView.findViewById(R.id.ResourcesList);

                    ResourcesListAdapter mResoucesListAdapter = new ResourcesListAdapter(getActivity().getApplicationContext(), RESOURCESLIST);
                    mResoucesListAdapter.clear();
                    mResoucesListAdapter.add(new EnumResource(getActivity().getApplicationContext(), "Clint Mourlevat", "My first application", true));
                    mResoucesListAdapter.add(new EnumResource(getActivity().getApplicationContext(), "John Loizeau", "My first application", false));
                    mResoucesListAdapter.add(new EnumResource(getActivity().getApplicationContext(), "Sebastien Grosjean", "My first application", false));

                    mResourcesList.setAdapter(mResoucesListAdapter);
                    mResourcesList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                            Intent mResourceIntent = new Intent(getActivity().getApplicationContext(), Resource.class);
                            mResourceIntent.putExtra("name", ((EnumResource)view.getTag()).Name);
                            mResourceIntent.putExtra("id", "1");
                            startActivityForResult(mResourceIntent, 1);
                        }
                    });
                    return rootView;

                case 3:
                    rootView = inflater.inflate(R.layout.fragment_task, container, false);

                    TaskListAdapter mTaskListAdapter = new TaskListAdapter(getActivity().getApplicationContext(), TASKLIST);
                    mTaskListAdapter.clear();
                    mTaskListAdapter.add(new EnumTask(getActivity().getApplicationContext(), "Manger", "My first application"));

                    ((ListView) rootView.findViewById(R.id.TaskList)).setAdapter(mTaskListAdapter);
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