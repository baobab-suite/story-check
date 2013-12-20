package za.co.storycheck;

import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import za.co.storycheck.data.DbHelper;
import za.co.storycheck.receiver.StoryStateUpdater;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/20
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class StoryCheckApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(this).registerReceiver(new StoryStateUpdater(), new IntentFilter("update_story_state"));
    }

    @Override
    public void onTerminate() {
        DbHelper.getHelper(this).close();
    }
}
