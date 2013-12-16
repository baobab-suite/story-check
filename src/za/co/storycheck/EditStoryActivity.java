package za.co.storycheck;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import za.co.storycheck.data.DbHelper;

public class EditStoryActivity extends FragmentActivity {

    private EditText et_headline;
    private long id;
    private Menu menu;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_edit_activity);
        et_headline = (EditText) findViewById(R.id.et_headline);
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("Edit a headline");
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("storyId");
        final String headline = extras.getString("headline");
        et_headline.setText(headline);
        et_headline.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String headline = editable.toString();
                menu.findItem(R.id.mi_ok).setEnabled(headline != null && headline.trim().length() > 0);
            }
        });
    }

    private void save() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("headline", et_headline.getText().toString());
        values.put("last_edit_date", System.currentTimeMillis());
        writableDatabase.beginTransaction();
        try {
            writableDatabase.update("Story", values, "_id = ?", new String[]{String.valueOf(id)});
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.findItem(R.id.mi_ok).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
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
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
