package com.trackcell.smartpro;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ProjectExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<EnumProject>> _listDataChild;

    public ProjectExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<EnumProject>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.model_projectlist, null);
        }

        TextView mProjectListName = (TextView)convertView.findViewById(R.id.model_resourcesList_name);
        TextView mProjectListDescription = (TextView)convertView.findViewById(R.id.model_projectList_description);
        TextView mProjectListState = (TextView)convertView.findViewById(R.id.model_projectList_state);
        TextView mProjectListDay = (TextView)convertView.findViewById(R.id.model_projectList_day);

        mProjectListName.setText(((EnumProject)getChild(groupPosition, childPosition)).Name);
        mProjectListDescription.setText(((EnumProject)getChild(groupPosition, childPosition)).Description);
        mProjectListName.setTypeface(null, Typeface.NORMAL);

        Main.InflateProjectItem(this._context, mProjectListState, mProjectListDay, ((EnumProject)getChild(groupPosition, childPosition)).ID);

        switch(((EnumProject)getChild(groupPosition, childPosition)).State)
        {
            case 0:
                mProjectListState.setText("Prévu");
                mProjectListState.setBackgroundResource(R.drawable.balloon_blue);
                break;
            case 1:
                mProjectListState.setText("En attente");
                mProjectListState.setBackgroundResource(R.drawable.balloon_orange);
                break;
            case 2:
                mProjectListState.setText("En retard");
                mProjectListState.setBackgroundResource(R.drawable.balloon_red);
        }

        convertView.setTag(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.model_groupheader, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textView);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}