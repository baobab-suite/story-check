package za.co.storycheck.loaders;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import za.co.storycheck.R;
import za.co.storycheck.data.DbHelper;
import za.co.storycheck.dto.ReportDto;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/10
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class StoryReportLoader extends AsyncTaskLoader<ReportDto> {


    private final long storyId;
    private final String headline;

    public StoryReportLoader(Context context, long storyId, String headline) {
        super(context);
        this.storyId = storyId;
        this.headline = headline;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ReportDto loadInBackground() {
        DbHelper dbHelper = DbHelper.getHelper(getContext());
        String itemSql = getContext().getString(R.string.sql_query_StoryItem_for_report);
        SQLiteDatabase readableDatabase = dbHelper.getWritableDatabase();
        Cursor c = readableDatabase.rawQuery(itemSql, new String[]{String.valueOf(storyId)});

        StringBuffer stringSB = new StringBuffer();
        stringSB.append("Status report for story: ");
        stringSB.append(headline);
        stringSB.append("\n\nItems not completed:");
        StringBuffer htmlSB = new StringBuffer();
        htmlSB.append("<html>Status report for story: <strong>");
        htmlSB.append(headline);
        htmlSB.append("</strong></br></br>Items not completed:<ul>");
        int state = 0;
        while (c.moveToNext()){
            stringSB.append("\n   - ");
            stringSB.append(c.getString(c.getColumnIndex("label")));
            htmlSB.append("<li>");
            htmlSB.append(c.getString(c.getColumnIndex("label")));
            htmlSB.append("</li>");
            int newState = c.getInt(c.getColumnIndex("state"));
            if(state != newState && newState == 1){
                stringSB.append("\n\nItems completed:");
                htmlSB.append("</ul></br>Items completed:<ul>");
            }
            if(state != newState && newState == 2){
                stringSB.append("\n\nItems marked and not applicable:");
                htmlSB.append("</ul></br>Items marked and not applicable:<ul>");
            }
            state = newState;
        }
        htmlSB.append("</ul></html>");
        c.close();
        ReportDto dto = new ReportDto();
        dto.setHtml(htmlSB.toString());
        dto.setString(stringSB.toString());
        return dto;
    }


}
