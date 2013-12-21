package za.co.storycheck.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import za.co.storycheck.R;
import za.co.storycheck.loaders.RawQueryLoader;
import za.co.storycheck.viewbinder.StoryItemRowViewBinder;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLoaderManager().restartLoader(R.id.story_fragment, null, StoryFragment.this);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("reload_story"));
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
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_row, null, new String[]{"label", "note", "state"}, new int[] {R.id.tv_label, R.id.tv_note, R.id.cb_state}, 0);
        adapter.setViewBinder(new StoryItemRowViewBinder());
        ListView listView = (ListView) view.findViewById(R.id.lv_items);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(R.id.story_fragment, savedInstanceState, this);
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = getActivity().getIntent().getExtras();
        long storyId = extras.getLong("storyId");
        return new RawQueryLoader(getActivity(), R.string.sql_query_StoryItem_by_story_id, new String[]{String.valueOf(storyId)});
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
