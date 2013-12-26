package za.co.storycheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/26
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Splash extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getResources().getBoolean(R.bool.isTablet)){
        }
        String action = getResources().getString(R.string.startActivity);
        startActivity(new Intent(action));
    }
}