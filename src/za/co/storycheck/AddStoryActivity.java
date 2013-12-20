package za.co.storycheck;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import za.co.storycheck.data.DbHelper;
import za.co.storycheck.data.RawQueryLoader;

public class AddStoryActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;
    private Spinner spinner;
    private EditText et_headline;
    private Menu menu;

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
        et_headline.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String headline = editable.toString();
                boolean enabled = headline != null && headline.trim().length() > 0;
                menu.findItem(R.id.mi_ok).setEnabled(enabled);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.findItem(R.id.mi_ok).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void add() {
        int position = spinner.getSelectedItemPosition();
        Cursor cursor = (Cursor) adapter.getItem(position);
        String type = cursor.getString(cursor.getColumnIndex("name"));
        long typeId = cursor.getLong(cursor.getColumnIndex("_id"));
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("headline", et_headline.getText().toString());
        values.put("type", type);
        long today = System.currentTimeMillis();
        values.put("create_date", today);
        values.put("last_edit_date", today);
        values.put("deleted", false);
        writableDatabase.beginTransaction();
        try {
            long storyId = writableDatabase.insert("Story", null, values);
            String insert = getResources().getString(R.string.sql_query_copy_story_item);
            writableDatabase.execSQL(insert, new Object[]{storyId, typeId});
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
