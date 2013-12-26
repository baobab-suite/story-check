package za.co.storycheck;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import za.co.storycheck.fragment.StoryFragment;
import za.co.storycheck.loaders.RawQueryLoader;

public class StoryActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private String headline;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);
        headline = getIntent().getExtras().getString("headline");
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(headline);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportLoaderManager().initLoader(R.id.story_activity, savedInstanceState, this);
        StoryFragment storyFragment = (StoryFragment)getSupportFragmentManager().findFragmentById(R.id.frag_story_detail);
        long storyId = getIntent().getExtras().getLong("storyId");
        storyFragment.setStory(storyId, headline);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            finish();
            return true;
        case R.id.mi_delete:
            deleteStory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reportStory() {
        Intent intent = new Intent(this, StoryReportActivity.class);
        startStoryActivity(intent);
    }

    private void deleteStory() {
        Intent intent = new Intent(this, DeleteStoryActivity.class);
        startStoryActivity(intent);
        finish();
    }

    private void editStory() {
        Intent intent = new Intent(this, EditStoryActivity.class);
        startStoryActivity(intent);
    }

    private void startStoryActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Bundle extras = getIntent().getExtras();
        intent.putExtra("storyId", extras.getLong("storyId"));
        intent.putExtra("headline", headline);
        startActivity(intent);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = getIntent().getExtras();
        long storyId = extras.getLong("storyId");
        return new RawQueryLoader(this, R.string.sql_query_Story_by_id, new String[]{String.valueOf(storyId)});
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        try {
            if (cursor.moveToFirst()) {
                headline = cursor.getString(cursor.getColumnIndex("headline"));
                getActionBar().setTitle(headline);
            }
        } finally {
//            cursor.close();
        }
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Do nothing
    }
}
