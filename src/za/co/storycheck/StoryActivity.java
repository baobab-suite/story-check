package za.co.storycheck;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class StoryActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_activity);
    }
}
