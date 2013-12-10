package com.baobab.android.storycheck.loader;

import com.baobab.android.storycheck.model.Story;

import java.io.IOException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import za.co.storycheck.R;
import za.co.storycheck.data.DataLoader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryLoader extends AsyncTaskLoader<LoaderResult<Story>> {

    private final int storyId;

    public StoryLoader(Context context, int storyId) {
        super(context);
        this.storyId = storyId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public LoaderResult<Story> loadInBackground() {
        try {
            Story story = DataLoader.getDataLoader(getContext()).getStory(storyId, getContext());
            return new LoaderResult<Story>(story);
        } catch (IOException e) {
            Log.e("StoryLoader","Error loading story", e);
            return new LoaderResult<Story>(e, R.string.error_loading_story);
        }
    }


}
