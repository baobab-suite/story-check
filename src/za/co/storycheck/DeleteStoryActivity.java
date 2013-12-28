package za.co.storycheck;

import com.google.analytics.tracking.android.EasyTracker;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import za.co.storycheck.data.DbHelper;

public class DeleteStoryActivity extends SherlockFragmentActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_delete_activity);
        TextView tv_headline = (TextView) findViewById(R.id.tv_headline);
        Bundle extras = getIntent().getExtras();
        final String headline = extras.getString("headline");
        tv_headline.setText(headline);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.delete_checklist));
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

    private void delete() {
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("storyId");
        DbHelper dbHelper = DbHelper.getHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            String delete = getResources().getString(R.string.sql_delete_StoryItem_by_story_id);
            writableDatabase.execSQL(delete, new Object[]{id});
            writableDatabase.delete("Story", "_id = ?", new String[]{String.valueOf(id)});
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
//            writableDatabase.close();
        }
        finish();
        Intent reloadIntent = new Intent("reload_story");
        reloadIntent.putExtra("storyId", -1l);
        LocalBroadcastManager.getInstance(this).sendBroadcast(reloadIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.ok_cancel_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
            case R.id.mi_cancel:
                finish();
                return true;
            case R.id.mi_ok:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
