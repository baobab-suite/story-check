package com.baobab.android.storycheck.fragment;

import com.baobab.android.storycheck.R;
import com.baobab.android.storycheck.StoryActivity;
import com.baobab.android.storycheck.adapter.StoryTypeAdapter;
import com.baobab.android.storycheck.data.DataLoader;
import com.baobab.android.storycheck.loader.LoaderResult;
import com.baobab.android.storycheck.loader.StoryTypeLoader;
import com.baobab.android.storycheck.model.Story;
import com.baobab.android.storycheck.model.StoryType;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryTypeFragment extends Fragment implements LoaderManager.LoaderCallbacks<LoaderResult<List<StoryType>>> {

    private StoryTypeAdapter adapter = new StoryTypeAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_type_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(0, savedInstanceState, this);
        ListView listView = (ListView) view.findViewById(R.id.lv_story_type);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoryType storyType = (StoryType) parent.getAdapter().getItem(position);
                try {
                    FragmentActivity activity = getActivity();
                    Story story = DataLoader.getDataLoader(activity).createStory(activity, storyType);
                    Intent intent = new Intent(activity, StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("storyId", story.id);
                    activity.startActivity(intent);
                } catch (IOException e) {
                    Log.e("StoryTypeFragment", "error creating story", e);
                }
            }
        });
    }

    public Loader<LoaderResult<List<StoryType>>> onCreateLoader(int i, Bundle bundle) {
        return new StoryTypeLoader(getActivity());
    }

    public void onLoadFinished(Loader<LoaderResult<List<StoryType>>> loader, LoaderResult<List<StoryType>> result) {
        adapter.setData(result.getData());
    }

    public void onLoaderReset(Loader<LoaderResult<List<StoryType>>> listLoader) {
        adapter.clear();
    }
}
