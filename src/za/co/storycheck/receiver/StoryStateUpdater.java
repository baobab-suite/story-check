package za.co.storycheck.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import za.co.storycheck.R;
import za.co.storycheck.data.DbHelper;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/20
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class StoryStateUpdater extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        updateStoryState(context, intent);
    }

    private void updateStoryState(Context context, Intent intent) {
        long storyId = intent.getExtras().getLong("storyId");
        DbHelper dbHelper = DbHelper.getHelper(context);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        int count0 = getStateCount(context, storyId, 0, writableDatabase);
        int count1 = getStateCount(context, storyId, 1, writableDatabase);
        ContentValues values = new ContentValues();
        values.put("check_count", count1);
        values.put("item_count", count1+count0);
        values.put("last_edit_date", System.currentTimeMillis());
        writableDatabase.beginTransaction();
        try {
            try {
                int rowCount = writableDatabase.update("Story", values, "_id = ?", new String[]{String.valueOf(storyId)});
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
//                writableDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private int getStateCount(Context context, long storyId, int state, SQLiteDatabase writableDatabase) {
        String sql = context.getString(R.string.sql_query_count_StoryItem_by_story_id_and_state);
        Cursor c = writableDatabase.rawQuery(sql, new String[]{String.valueOf(storyId), String.valueOf(state)});
        try {
            if (c.moveToFirst()){
                return c.getInt(0);
            }
        } finally {
            c.close();
        }
        return 0;
    }
}
