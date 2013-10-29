package com.baobab.android.storycheck.data;

import com.baobab.android.storycheck.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "baobab.db";
    private Context context;

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DbHelper.class.getSimpleName(), "Creating database tables...");
        Resources res = context.getResources();
        for (String createScript : res.getStringArray(R.array.scripts_v2)) {
            Log.i(DbHelper.class.getSimpleName(), "Executing '" + createScript + "' on " + db.getPath() + " (v" + db.getVersion() + ")");
            db.execSQL(createScript);
        }
        Log.i(DbHelper.class.getSimpleName(), "...Done creating database tables.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(DbHelper.class.getSimpleName(), "Checking upgrade from v" + oldVersion + " to v" + newVersion + "...");
        for (int version = oldVersion; version < newVersion; version++) {
            checkForUpgrade(db, version);
        }
    }

    private void checkForUpgrade(SQLiteDatabase db, int version) {
        Log.i(DbHelper.class.getSimpleName(), "Checking for upgrade from v" + version);
    }

    private void upgradeFromVersion(SQLiteDatabase db, int version, int scriptResource) {
        Log.i(DbHelper.class.getSimpleName(), "Upgrading from v" + version + "...");
        Resources res = context.getResources();
        for (String script : res.getStringArray(scriptResource)) {
            Log.i(DbHelper.class.getSimpleName(), "Executing '" + script + "' on " + db.getPath() + " (v" + db.getVersion() + ")");
            db.execSQL(script);
        }
        Log.i(DbHelper.class.getSimpleName(), "...Done upgrading from v" + version + ".");
    }
}
