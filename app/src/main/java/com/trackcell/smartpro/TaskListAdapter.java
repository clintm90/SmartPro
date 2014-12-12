package com.trackcell.smartpro;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<EnumTask>
{
    private final Context mContext;
    private final List<EnumTask> values;

    public TaskListAdapter(Context context, List<EnumTask> values)
    {
        super(context, R.layout.model_tasklist, values);
        this.mContext = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_tasklist, parent, false);

        TextView taskListName = (TextView)rowView.findViewById(R.id.model_resourcesList_name);
        TextView taskListDescription = (TextView)rowView.findViewById(R.id.model_taskList_description);
        TextView taskListDate = (TextView)rowView.findViewById(R.id.model_taskList_date);
        TextView taskListProject = (TextView)rowView.findViewById(R.id.model_taskList_project);

        taskListName.setText(values.get(position).Name);
        taskListDate.setText(values.get(position).Date.toString());

        if(values.get(position).isEnd)
        {
            taskListName.setPaintFlags(taskListName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            taskListDescription.setVisibility(View.GONE);
            taskListProject.setVisibility(View.GONE);
        }

        rowView.setTag(values.get(position));

        return rowView;
    }
}