package com.baobab.android.storycheck.adapter;

import com.baobab.android.storycheck.R;
import com.baobab.android.storycheck.model.StoryType;

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
public class StoryTypeAdapter extends BaseAdapter {

    private List<StoryType> storyTypes = new ArrayList<StoryType>();

    public int getCount() {
        return storyTypes.size();
    }

    public Object getItem(int i) {
        return storyTypes.get(i);
    }

    public long getItemId(int i) {
        return storyTypes.get(i).getName().hashCode();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        StoryType storyType = storyTypes.get(i);
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.story_type_row, null);
            holder.tv_label = ((TextView)view.findViewById(R.id.tv_label));
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_label.setText(storyType.getName());
        return view;

    }

    public void setData(List<StoryType> data) {
        storyTypes.clear();
        if(data != null){
            storyTypes.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        setData(null);
    }

    @Override
    public boolean hasStableIds() {
        return true;
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
        return storyTypes.isEmpty();
    }
    private class ViewHolder {
        public TextView tv_label;
    }
}

