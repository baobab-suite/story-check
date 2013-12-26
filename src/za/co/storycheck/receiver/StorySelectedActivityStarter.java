package za.co.storycheck.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import za.co.storycheck.StoryActivity;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/20
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class StorySelectedActivityStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        long storyId = extras.getLong("storyId");
        String headline = extras.getString("headline");
        Intent viewStoryIntent = new Intent(context, StoryActivity.class);
        viewStoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        viewStoryIntent.putExtra("storyId", storyId);
        viewStoryIntent.putExtra("headline", headline);
        context.startActivity(viewStoryIntent);
    }
}
