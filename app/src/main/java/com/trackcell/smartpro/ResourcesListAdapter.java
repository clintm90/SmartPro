package com.trackcell.smartpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResourcesListAdapter extends ArrayAdapter<EnumResource>
{
    private final Context mContext;
    private final List<EnumResource> values;

    public ResourcesListAdapter(Context context, List<EnumResource> values)
    {
        super(context, R.layout.model_resourceslist, values);
        this.mContext = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.model_resourceslist, parent, false);

        TextView resourcesListName = (TextView)rowView.findViewById(R.id.model_resourcesList_name);

        resourcesListName.setText(values.get(position).Name);

        rowView.setTag(values.get(position));

        return rowView;
    }
}