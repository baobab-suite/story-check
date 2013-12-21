package za.co.storycheck;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import za.co.storycheck.data.DbHelper;
import za.co.storycheck.loaders.RawQueryLoader;

public class AddStoryActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private EditText et_headline;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_add_activity);
        adapter = new SimpleCursorAdapter(this, R.layout.story_type_spinner_row, null, new String[]{"name"}, new int[]{R.id.tv_label}, 0);
        spinner = (Spinner) findViewById(R.id.sp_story_type);
        spinner.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, savedInstanceState, this);
        et_headline = (EditText) findViewById(R.id.et_headline);
        setTitle(R.string.add_story);
        getActionBar().setDisplayHomeAsUpEnabled(true);
     }

    private void add() {
        int position = spinner.getSelectedItemPosition();
        Cursor cursor = (Cursor) adapter.getItem(position);
        String type = cursor.getString(cursor.getColumnIndex("name"));
        long typeId = cursor.getLong(cursor.getColumnIndex("_id"));
        ContentValues values = new ContentValues();
        String headline = et_headline.getText().toString();
        values.put("headline", headline);
        values.put("type", type);
        long today = System.currentTimeMillis();
        values.put("create_date", today);
        values.put("last_edit_date", today);
        values.put("create_date_str", new SimpleDateFormat("dd.MM.yyyy").format(new Date(today)));
        values.put("deleted", false);
        DbHelper dbHelper = DbHelper.getHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        writableDatabase.beginTransaction();
        long storyId;
        try {
            storyId = writableDatabase.insert("Story", null, values);
            String insert = getResources().getString(R.string.sql_query_copy_story_item);
            writableDatabase.execSQL(insert, new Object[]{storyId, typeId});
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
//            writableDatabase.close();
        }
        Intent updateStoryStateIntent = new Intent("update_story_state");
        updateStoryStateIntent.putExtra("storyId", storyId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateStoryStateIntent);
        Intent intent = new Intent(this, StoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("storyId", storyId);
        intent.putExtra("headline", headline);
        startActivity(intent);
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
                add();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new RawQueryLoader(this, R.string.sql_query_all_StoryType, null);
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
