package za.co.storycheck;

import android.app.Application;
import za.co.storycheck.data.DbHelper;

/**
 * User: dirk
 * Date: 2013/12/20
 * Time: 10:29 AM
 */
public class StoryCheckApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
//        LocalBroadcastManager.getInstance(this).registerReceiver(new StoryStateUpdater(), new IntentFilter("update_story_state"));
//        GoogleAnalytics.getInstance(this).setDryRun(true);
    }


    @Override
    public void onTerminate() {
        DbHelper.getHelper(this).close();
    }
}
