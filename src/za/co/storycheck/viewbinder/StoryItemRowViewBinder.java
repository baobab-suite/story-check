package za.co.storycheck.viewbinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import za.co.storycheck.data.DbHelper;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/15
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryItemRowViewBinder implements SimpleCursorAdapter.ViewBinder {

    public boolean setViewValue(final View view, Cursor cursor, int columnIndex) {
        if (columnIndex == cursor.getColumnIndex("state")) {
            int state = cursor.getInt(columnIndex);
            CheckBox checkBox = (CheckBox) view;
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(state == 1);
            final long id = cursor.getLong(cursor.getColumnIndex("_id"));
            final Context context = view.getContext();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                    AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            DbHelper dbHelper = new DbHelper(context);
                            SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
//                            values.put("_id", cursor.getLong(cursor.getColumnIndex("_id")));
                            values.put("state", b ? 1 : 0);
                            writableDatabase.beginTransaction();
                            try {
                                int count = writableDatabase.update("StoryItem", values, " _id = ?", new String[]{String.valueOf(id)});
                                writableDatabase.setTransactionSuccessful();
                                Intent intent = new Intent("reload_story");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            } finally {
                                writableDatabase.endTransaction();
                                writableDatabase.close();
                            }
                            return null;
                        }
                    };
                    task.execute((Void)null);

                }
            });
            return true;
        }
        // For others, we simply return false so that the default binding
        // happens.
        return false;
    }
}
