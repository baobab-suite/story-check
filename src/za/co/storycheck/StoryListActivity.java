package za.co.storycheck;

import com.google.analytics.tracking.android.EasyTracker;
import com.rampo.updatechecker.UpdateChecker;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import za.co.storycheck.fragment.StoryFragment;

public class StoryListActivity extends FragmentActivity {

    StoryFragment storyFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_list);
        storyFragment = (StoryFragment)getSupportFragmentManager().findFragmentById(R.id.frag_story_detail);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        setTitle(getString(R.string.story_list_title));
//        getActionBar().show();
        ListView listView = (ListView) findViewById(R.id.lv_story_type);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getAdapter().getItem(position);
                long storyId = cursor.getLong(cursor.getColumnIndex("_id"));
                String headline = cursor.getString(cursor.getColumnIndex("headline"));
                storySelected(storyId, headline);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
        UpdateChecker.checkForNotification(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_add:
            {
                addStory();
                return true;
            }
            case R.id.mi_about:
            {
                Intent intent = new Intent(this, AboutActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;
            }
            case R.id.mi_feedback:
            {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:storycheck@googlegroups.com"));
                intent.putExtra(Intent.EXTRA_EMAIL, "storycheck@googlegroups.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_message));
                startActivity(Intent.createChooser(intent, "Send Email"));
                return true;
            }
        }
        return false;
    }

    protected void addStory() {
        Intent intent = new Intent(this, AddStoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    protected void storySelected(long storyId, String headline) {
        if(storyFragment == null){
            Intent intent = new Intent(this, StoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("storyId", storyId);
            intent.putExtra("headline", headline);
            startActivity(intent);
        }else{
            storyFragment.onLoaderReset(null);
            storyFragment.loadStory(storyId);
            TextView tv_headline = (TextView) findViewById(R.id.tv_headline);
            tv_headline.setText(headline);
        }
    }
}
