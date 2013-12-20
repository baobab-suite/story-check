package za.co.storycheck;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import za.co.storycheck.data.RawQueryLoader;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            finish();
            return true;
        case R.id.mi_edit:
            editStory();
            return true;
        case R.id.mi_delete:
            deleteStory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteStory() {
        Intent intent = new Intent(this, DeleteStoryActivity.class);
        sartStoryActivity(intent);
        finish();
    }

    private void editStory() {
        Intent intent = new Intent(this, EditStoryActivity.class);
        sartStoryActivity(intent);
    }

    private void sartStoryActivity(Intent intent) {
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
            cursor.close();
        }
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Do nothing
    }
}
