package com.baobab.android.storycheck.loader;

import com.baobab.android.storycheck.R;
import com.baobab.android.storycheck.data.DataLoader;
import com.baobab.android.storycheck.model.StoryType;

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
public class StoryTypeLoader extends AsyncTaskLoader<LoaderResult<List<StoryType>>> {

    public StoryTypeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        List<StoryType> storyTypes = DataLoader.getDataLoader(getContext()).getStoryTypes();
        if(storyTypes != null){
            deliverResult(new LoaderResult<List<StoryType>>(storyTypes));
        }else{
            forceLoad();
        }
    }

    @Override
    public LoaderResult<List<StoryType>> loadInBackground() {
        try {
            List<StoryType> storyTypes = DataLoader.getDataLoader(getContext()).loadStoryTypes(getContext());
            return new LoaderResult<List<StoryType>>(storyTypes);
        } catch (IOException e) {
            Log.e("StoryTypeLoader","Error loading story types", e);
            return new LoaderResult<List<StoryType>>(e, R.string.error_loading_storytypes);
        }
     }
}
