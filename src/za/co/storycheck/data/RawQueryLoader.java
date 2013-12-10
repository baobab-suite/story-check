package za.co.storycheck.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/10
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class RawQueryLoader extends AsyncTaskLoader<Cursor> {

    private String sql;
    private String[] args;

    public RawQueryLoader(Context context, String sql, String[] args) {
        super(context);
        this.sql = sql;
        this.args = args;
    }

    public RawQueryLoader(Context context, int sql, String[] args) {
        super(context);
        this.sql =context.getResources().getString(sql);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        DbHelper dbHelper = new DbHelper(getContext());
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        try {
            return readableDatabase.rawQuery(sql, args);
        } finally {
//            dbHelper.close();
        }
    }


}
