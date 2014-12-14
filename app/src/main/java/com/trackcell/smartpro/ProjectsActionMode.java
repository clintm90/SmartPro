package com.trackcell.smartpro;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

public class ProjectsActionMode implements ActionMode.Callback
{
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        mode.setTitle("SmartPro");
        mode.getMenuInflater().inflate(R.menu.action_mode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        // retrieve selected items and print them out
        /*SelectableAdapter adapter = (SelectableAdapter) ListViewActivity.this.getListAdapter();
        SparseBooleanArray selected = adapter.getSelectedIds();
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < selected.size(); i++)
        {
            if (selected.valueAt(i))
            {
                String selectedItem = adapter.getItem(selected.keyAt(i));
                message.append(selectedItem + "\n");
            }
        }
        Toast.makeText(ListViewActivity.this, message.toString(), Toast.LENGTH_LONG).show();*/

        // close action mode
        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        // remove selection
        /*SelectableAdapter adapter = (SelectableAdapter) getListAdapter();
        adapter.removeSelection();
        mActionMode = null;*/
    }
}
