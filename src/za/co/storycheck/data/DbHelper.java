package za.co.storycheck.data;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import za.co.storycheck.R;

public class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "story-check.db";
    private Context context;

    public DbHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DbHelper.class.getSimpleName(), "Creating database tables...");
        Resources res = context.getResources();
        for (String createScript : res.getStringArray(R.array.create_db)) {
            Log.i(DbHelper.class.getSimpleName(), "Executing '" + createScript + "' on " + db.getPath() + " (v" + db.getVersion() + ")");
            db.execSQL(createScript);
        }
        Log.i(DbHelper.class.getSimpleName(), "...Done creating database tables.");
        Log.i(DbHelper.class.getSimpleName(), "...Inserting initial data.");
        for (String createScript : res.getStringArray(R.array.insert_init_data)) {
            Log.i(DbHelper.class.getSimpleName(), "Executing '" + createScript + "' on " + db.getPath() + " (v" + db.getVersion() + ")");
            db.execSQL(createScript);
        }
        Log.i(DbHelper.class.getSimpleName(), "...Done inserting initial data.");
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

    static DbHelper dbHelper;

    public synchronized static DbHelper getHelper(Context context) {
        if(dbHelper == null){
            dbHelper = new DbHelper(context);
        }
        return dbHelper;
    }
}
