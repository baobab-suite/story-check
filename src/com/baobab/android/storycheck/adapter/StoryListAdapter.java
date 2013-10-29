package com.baobab.android.storycheck.adapter;

import com.baobab.android.storycheck.R;
import com.baobab.android.storycheck.model.Story;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryListAdapter extends BaseAdapter {

    private List<Story> stories = new ArrayList<Story>();

    public int getCount() {
        return stories.size();
    }

    public Object getItem(int i) {
        return stories.get(i);
    }

    public long getItemId(int i) {
        return 1;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Story story = stories.get(i);
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_row, null);
            holder.tv_label = ((TextView)view.findViewById(R.id.tv_label));
            holder.tv_type = ((TextView)view.findViewById(R.id.tv_type));
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_label.setText(story.headline);
        holder.tv_type.setText(story.type);
        return view;

    }

    public void setData(List<Story> data) {
        stories.clear();
        if(data != null){
            stories.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        setData(null);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return stories.isEmpty();
    }

    private class ViewHolder {
        public TextView tv_label;
        public TextView tv_type;
    }
}

