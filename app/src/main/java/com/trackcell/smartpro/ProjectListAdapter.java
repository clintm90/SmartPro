package com.trackcell.smartpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectListAdapter extends ArrayAdapter<EnumProject>
{
    private final Context mContext;
    private final List<EnumProject> values;

    public ProjectListAdapter(Context context, List<EnumProject> values)
    {
        super(context, R.layout.model_projectlist, values);
        this.mContext = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_projectlist, parent, false);

        TextView projectListName = (TextView)rowView.findViewById(R.id.model_taskList_name);
        TextView projectListDescription = (TextView)rowView.findViewById(R.id.model_projectList_description);
        TextView projectListState = (TextView)rowView.findViewById(R.id.model_projectList_state);
        TextView projectListDay = (TextView)rowView.findViewById(R.id.model_projectList_day);

        projectListName.setText(values.get(position).Name);
        projectListDescription.setText(values.get(position).Description);
        projectListDay.setText("3 Personnes");

        switch(values.get(position).State)
        {
            case 0:
                projectListState.setText("Prévu");
                projectListState.setBackgroundResource(R.drawable.balloon_blue);
                break;
            case 1:
                projectListState.setText("En attente");
                projectListState.setBackgroundResource(R.drawable.balloon_orange);
                break;
            case 2:
                projectListState.setText("En retard");
                projectListState.setBackgroundResource(R.drawable.balloon_red);
        }

        rowView.setTag(values.get(position));

        return rowView;
    }
}