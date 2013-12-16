package com.baobab.android.storycheck.adapter;

import com.baobab.android.storycheck.model.Item;
import com.baobab.android.storycheck.model.Story;
import com.baobab.android.storycheck.view.TriStateButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import za.co.storycheck.R;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChecklistAdapter extends BaseAdapter {

    private Story story = new Story();

    public int getCount() {
        return story.items.size();
    }

    public Object getItem(int i) {
        return story.items.get(i);
    }

    public long getItemId(int i) {
        return story.items.get(i).label.hashCode();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        final Item item = story.items.get(i);
        final ViewHolder holder;
        if (view == null || view.getTag() == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, null);
            holder.bt_state = ((TriStateButton)view.findViewById(R.id.cb_state));
            holder.tv_label = ((TextView)view.findViewById(R.id.tv_label));
            holder.tv_note = ((TextView)view.findViewById(R.id.tv_note));
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
//        holder.bt_state.setText(String.valueOf(item.state));
        holder.bt_state.setState(item.state);
        holder.tv_label.setText(item.label);
        String note = item.note;
        if (note == null || note.trim().length() == 0) {
            holder.tv_note.setText("Tap to add note");
            holder.tv_note.setTextColor(R.color.note_no_text);
        }else{
            holder.tv_note.setText(note);
            holder.tv_note.setTextColor(R.color.note_text);

        }
        holder.bt_state.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                item.toggleState();
//                holder.bt_state.setText(String.valueOf(item.state));
                holder.bt_state.setState(item.state);
            }
        });
        return view;

    }

    public void setData(Story story) {
        if(story != null){
            this.story = story;
        }else{
            this.story = new Story();
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
        return story.items.isEmpty();
    }

    private class ViewHolder {
        public TriStateButton bt_state;
        public TextView tv_label;
        public TextView tv_note;
    }
}


