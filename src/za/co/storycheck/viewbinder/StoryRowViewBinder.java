package za.co.storycheck.viewbinder;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.TextView;
import za.co.storycheck.view.DrawablePie;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/15
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryRowViewBinder implements SimpleCursorAdapter.ViewBinder {

    public boolean setViewValue(final View view, Cursor cursor, int columnIndex) {
        if (columnIndex == cursor.getColumnIndex("deleted")) {
            int item_count = cursor.getInt(cursor.getColumnIndex("item_count"));
            int check_count = cursor.getInt(cursor.getColumnIndex("check_count"));
            DrawablePie pie = (DrawablePie) view;
            pie.setPieVlaues(new int[]{check_count, item_count-check_count});
            return true;
//        if (columnIndex == cursor.getColumnIndex("deleted")) {
//            int item_count = cursor.getInt(cursor.getColumnIndex("item_count"));
//            int check_count = cursor.getInt(cursor.getColumnIndex("check_count"));
//            ProgressBar progressBar = (ProgressBar) view;
//            progressBar.setMax(item_count);
//            progressBar.setProgress(check_count);
//            return true;
        } else if (columnIndex == cursor.getColumnIndex("_id")) {
            int item_count = cursor.getInt(cursor.getColumnIndex("item_count"));
            int check_count = cursor.getInt(cursor.getColumnIndex("check_count"));
            int pers = check_count*100/item_count;
            ((TextView)view).setText(pers+"%");
            return true;
        }
        // For others, we simply return false so that the default binding
        // happens.
        return false;
    }
}
