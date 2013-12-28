package za.co.storycheck.fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import za.co.storycheck.DeleteStoryActivity;
import za.co.storycheck.EditStoryActivity;
import za.co.storycheck.R;
import za.co.storycheck.StoryReportActivity;
import za.co.storycheck.loaders.RawQueryLoader;
import za.co.storycheck.viewbinder.StoryItemRowViewBinder;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryFragment extends SherlockFragment implements StoryDetailFragment, LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private long storyId = -1;
    private String headline;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long storyId = intent.getExtras().getLong("storyId");
            setStory(storyId, headline);
        }
    };

    public void setStory(long storyId, String headline) {
        this.storyId = storyId;
        this.headline = headline;
        Bundle bundle = new Bundle();
        bundle.putLong("storyId", storyId);
        bundle.putString("headline", headline);
        getLoaderManager().restartLoader(R.id.story_fragment, bundle, this);
    }

    public void clearSelection() {
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("reload_story"));
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] cols = {"label", "note", "state"};
        int[] views = {R.id.tv_label, R.id.tv_note, R.id.cb_state};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_row, null, cols, views, 0);
        adapter.setViewBinder(new StoryItemRowViewBinder());
        ListView listView = (ListView) view.findViewById(R.id.lv_items);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(R.id.story_fragment, getArguments(), this);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        long storyId;
        if (bundle == null) {
            return new Loader<Cursor>(getActivity());
        }
        storyId = bundle.getLong("storyId");
        return new RawQueryLoader(getActivity(), R.string.sql_query_StoryItem_by_story_id, new String[]{String.valueOf(storyId)});
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.story_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (storyId >= 0) {
            switch (item.getItemId()) {
                case R.id.mi_edit:
                    editStory();
                    return true;
                case R.id.mi_delete:
                    deleteStory();
                    return true;
                case R.id.mi_report:
                    reportStory();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void reportStory() {
        Intent intent = new Intent(getActivity(), StoryReportActivity.class);
        startStoryActivity(intent);
    }

    private void deleteStory() {
        Intent intent = new Intent(getActivity(), DeleteStoryActivity.class);
        startStoryActivity(intent);
    }

    private void editStory() {
        Intent intent = new Intent(getActivity(), EditStoryActivity.class);
        startStoryActivity(intent);
    }

    private void startStoryActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("storyId", storyId);
        intent.putExtra("headline", headline);
        startActivity(intent);
    }
}
