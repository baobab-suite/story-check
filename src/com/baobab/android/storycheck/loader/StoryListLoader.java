package com.baobab.android.storycheck.loader;

import com.baobab.android.storycheck.R;
import com.baobab.android.storycheck.data.DataLoader;
import com.baobab.android.storycheck.model.Story;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryListLoader extends AsyncTaskLoader<LoaderResult<List<Story>>> {

    public StoryListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        List<Story> stories = DataLoader.getDataLoader(getContext()).getStories();
        if(stories != null){
            deliverResult(new LoaderResult<List<Story>>(stories));
        }else{
            forceLoad();
        }
    }

    @Override
    public LoaderResult<List<Story>> loadInBackground() {
        try {
            List<Story> stories = DataLoader.getDataLoader(getContext()).loadStories(getContext());
            return new LoaderResult<List<Story>>(stories);
        } catch (IOException e) {
            Log.e("StoryTypeLoader", "Error loading story types", e);
            return new LoaderResult<List<Story>>(e, R.string.error_loading_storytypes);
        }
     }
}
