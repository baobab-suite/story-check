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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import za.co.storycheck.data.DbHelper;
import za.co.storycheck.data.RawQueryLoader;

public class AddStoryActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_add_activity);
        adapter = new SimpleCursorAdapter(this, R.layout.story_type_spinner_row, null, new String[]{"name"}, new int[]{R.id.tv_label}, 0);
        final Spinner spinner = (Spinner) findViewById(R.id.sp_story_type);
        spinner.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, savedInstanceState, this);
        final Button bt_ok = (Button) findViewById(R.id.bt_ok);
        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
        final EditText et_headline = (EditText) findViewById(R.id.et_headline);
        et_headline.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String headline = editable.toString();
                bt_ok.setEnabled(headline != null && headline.trim().length() > 0);
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int position = spinner.getSelectedItemPosition();
                Cursor cursor = (Cursor) adapter.getItem(position);
                String type = cursor.getString(cursor.getColumnIndex("name"));
                long typeId = cursor.getLong(cursor.getColumnIndex("_id"));
                DbHelper dbHelper = new DbHelper(AddStoryActivity.this);
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
                    writableDatabase.execSQL(insert, new String[] {String.valueOf(storyId), String.valueOf(typeId)});
                    writableDatabase.setTransactionSuccessful();
                } finally {
                    writableDatabase.endTransaction();
                    writableDatabase.close();
                }
                finish();
            }
        });
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
