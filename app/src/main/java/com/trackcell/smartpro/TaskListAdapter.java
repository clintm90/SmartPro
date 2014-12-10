package com.trackcell.smartpro;

import android.content.Context;
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

        TextView taskListName = (TextView)rowView.findViewById(R.id.model_taskList_name);

        taskListName.setText(values.get(position).Name);

        rowView.setTag(values.get(position));

        return rowView;
    }
}