package za.co.storycheck;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import za.co.storycheck.data.DbHelper;

public class DeleteStoryActivity extends Activity {

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
    }

    private void delete() {
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("storyId");
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            String delete = getResources().getString(R.string.sql_delete_StoryItem_by_story_id);
            writableDatabase.execSQL(delete, new Object[]{id});
            writableDatabase.delete("Story", "_id = ?", new String[]{String.valueOf(id)});
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_cancel_menu, menu);
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
