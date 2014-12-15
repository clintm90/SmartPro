package com.trackcell.smartpro;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceholderFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ActionMode mActionMode;

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

                List<EnumProject> users2 = new ArrayList<EnumProject>();
                users2.add(new EnumProject(getActivity().getApplicationContext(), -1, "Event System", "A Best application", EnumProject.WAIT));
                users2.add(new EnumProject(getActivity().getApplicationContext(), -1, "Vertigo", "A free project managemenr", EnumProject.LATE));

                listDataChild2.put(listDataHeader2.get(0), mDBSmartPro.GetProjects(true));
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
                        intent.putExtra("id", ((EnumProject) v.getTag()).ID);
                        intent.putExtra("name", ((EnumProject) v.getTag()).Name);
                        getActivity().startActivityForResult(intent, 1);

                        //Todo: complete action mode
                            /*if(mActionMode == null)
                            {
                            }
                            else
                            {
                                onListItemCheck(childPosition);
                            }*/
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
                    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id)
                    {
                        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD)
                        {
                            //onListItemCheck(position);
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
                                            mDBSmartPro.RemoveProject(((EnumProject)view.getTag()).ID);
                                            ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.MainContent);
                                            mViewPager.setAdapter(mViewPager.getAdapter());
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

                List<EnumResource> mCustomers = mDBSmartPro.GetResources(true);
                List<EnumResource> mUsers = mDBSmartPro.GetResources(false);

                if(mCustomers.size() == 0 && mUsers.size() == 0)
                {
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

                        final ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.MainContent);

                        final View modelResource = inflater.inflate(R.layout.model_resource, null);
                        final EditText mModelResourceName = (EditText)modelResource.findViewById(R.id.model_resource_name);
                        final EditText mModelResourceDescription = (EditText)modelResource.findViewById(R.id.model_resource_description);
                        final Spinner mMOdelResourceProjects = (Spinner)modelResource.findViewById(R.id.model_resource_projects);

                        mModelResourceName.setText(((EnumResource) v.getTag()).Name);
                        mModelResourceDescription.setText(((EnumResource) v.getTag()).Description);

                        final ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mDBSmartPro.GetProjectsNamesByID(((EnumResource) v.getTag()).ID));
                        mMOdelResourceProjects.setAdapter(adapter_state);

                        alertDialog.setView(modelResource);
                        alertDialog.setNegativeButton(getString(R.string.action_delete), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mDBSmartPro.RemoveResource(((EnumResource) v.getTag()).ID);
                                mViewPager.setAdapter(mViewPager.getAdapter());
                                mViewPager.setCurrentItem(1);
                            }
                        });
                        alertDialog.setPositiveButton(getString(R.string.valid), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(!mModelResourceName.getText().toString().trim().equals(""))
                                {
                                    mDBSmartPro.AlterResource(((EnumResource) v.getTag()).ID, mModelResourceName.getText().toString(),mModelResourceDescription.getText().toString());
                                    mViewPager.setAdapter(mViewPager.getAdapter());
                                    mViewPager.setCurrentItem(1);
                                }
                            }
                        });
                        alertDialog.create();
                        alertDialog.show();

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

                        final ViewPager mViewPager = (ViewPager) getActivity().findViewById(R.id.MainContent);

                        final View modelTask = inflater.inflate(R.layout.model_task, null);

                        final EditText mModelTaskName = (EditText)modelTask.findViewById(R.id.model_task_name);
                        final EditText mModelTaskDescription = (EditText)modelTask.findViewById(R.id.model_task_description);
                        final CheckBox mModelTaskEnded = (CheckBox)modelTask.findViewById(R.id.model_task_isEnded);

                        mModelTaskName.setText(((EnumTask) view.getTag()).Name);
                        mModelTaskDescription.setText(((EnumTask) view.getTag()).Description);
                        mModelTaskEnded.setChecked(((EnumTask)view.getTag()).isEnd);

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
                                mDBSmartPro.AlterTask(((EnumTask) view.getTag()).ID, mModelTaskName.getText().toString(), mModelTaskDescription.getText().toString(), mModelTaskEnded.isChecked());
                                mViewPager.setAdapter(mViewPager.getAdapter());
                                mViewPager.setCurrentItem(2);
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

    private void onListItemCheck(int position)
    {
        mActionMode = getActivity().startActionMode(new ProjectsActionMode());
    }
}