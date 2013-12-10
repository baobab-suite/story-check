package com.baobab.android.storycheck.fragment;

import com.baobab.android.storycheck.adapter.ChecklistAdapter;
import com.baobab.android.storycheck.loader.LoaderResult;
import com.baobab.android.storycheck.loader.StoryLoader;
import com.baobab.android.storycheck.model.Story;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import za.co.storycheck.R;
import za.co.storycheck.data.DataLoader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<LoaderResult<Story>> {

    private ChecklistAdapter adapter = new ChecklistAdapter();
    private EditText et_headline;
    private Story story;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        story.headline = et_headline.getText().toString();
        try {
            DataLoader.getDataLoader(getActivity()).storeStories(getActivity());
        } catch (IOException e) {
            Log.e("StoryFragment", "Error storing story.", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(0, savedInstanceState, this);
        ListView listView = (ListView) view.findViewById(R.id.lv_items);
        listView.setAdapter(adapter);
        et_headline = (EditText) view.findViewById(R.id.et_headline);
    }

    public Loader<LoaderResult<Story>> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = getActivity().getIntent().getExtras();
        return new StoryLoader(getActivity(), extras.getInt("storyId"));
    }

    public void onLoadFinished(Loader<LoaderResult<Story>> loader, LoaderResult<Story> result) {
        story = result.getData();
        adapter.setData(story);
        et_headline.setText(story.headline);
        et_headline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    story.headline = et_headline.getText().toString();
                    try {
                        DataLoader.getDataLoader(getActivity()).storeStories(getActivity());
                    } catch (IOException e) {
                        Log.e("StoryFragment", "Error storing story.", e);
                    }
                }
            }
        });
    }

    public void onLoaderReset(Loader<LoaderResult<Story>> listLoader) {
        adapter.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.story_menu, menu);
    }
}
