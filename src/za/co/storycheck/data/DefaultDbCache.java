package za.co.storycheck.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DefaultDbCache {

    private static final String TAG = DefaultDbCache.class.getName();

    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public DefaultDbCache(Context ctx) throws Exception {
        context = ctx;
        dbHelper = DbHelper.getHelper(context);
    }

    /**
     * Opens the cache.
     */
    public DefaultDbCache open() throws Exception {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Closes the database.
     */
    public void close() {
        dbHelper.close();
    }

    private boolean isOpen() {
        return db != null && db.isOpen();
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy, String limit) {
        return db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }
}
