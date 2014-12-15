package com.trackcell.smartpro;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
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

        TextView mTaskListName = (TextView)rowView.findViewById(R.id.model_resourcesList_name);
        TextView mTaskListDescription = (TextView)rowView.findViewById(R.id.model_taskList_description);
        TextView mTaskListDate = (TextView)rowView.findViewById(R.id.model_taskList_date);
        TextView mTaskListProject = (TextView)rowView.findViewById(R.id.title);

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext.getApplicationContext());
        mTaskListName.setText(values.get(position).Name);
        mTaskListDescription.setText(values.get(position).Description);
        mTaskListDate.setText(values.get(position).Date);

        Main.InflateTaskItem(this.mContext, mTaskListProject, values.get(position).ID);

        if(values.get(position).isEnd)
        {
            mTaskListName.setPaintFlags(mTaskListName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mTaskListDescription.setVisibility(View.GONE);
            mTaskListProject.setVisibility(View.GONE);
        }

        rowView.setTag(values.get(position));

        return rowView;
    }
}