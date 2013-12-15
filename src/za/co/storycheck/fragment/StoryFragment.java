package za.co.storycheck.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import za.co.storycheck.R;
import za.co.storycheck.data.RawQueryLoader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private EditText et_headline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        story.headline = et_headline.getText().toString();
//        try {
//            DataLoader.getDataLoader(getActivity()).storeStories(getActivity());
//        } catch (IOException e) {
//            Log.e("StoryFragment", "Error storing story.", e);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.story_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(0, savedInstanceState, this);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.item_row, null, new String[]{"label", "note"}, new int[] {R.id.tv_label, R.id.tv_note}, 0);
        ListView listView = (ListView) view.findViewById(R.id.lv_items);
        listView.setAdapter(adapter);
        et_headline = (EditText) view.findViewById(R.id.et_headline);
        String headline = getActivity().getIntent().getExtras().getString("headline");
        et_headline.setText(headline);
        getLoaderManager().initLoader(0, savedInstanceState, this);
    }
//
//    public void onLoadFinished(Loader<LoaderResult<Story>> loader, LoaderResult<Story> result) {
//        story = result.getData();
//        adapter.setData(story);
//        et_headline.setText(story.headline);
//        et_headline.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View view, boolean hasFocus) {
//                if(!hasFocus){
//                    story.headline = et_headline.getText().toString();
//                    try {
//                        DataLoader.getDataLoader(getActivity()).storeStories(getActivity());
//                    } catch (IOException e) {
//                        Log.e("StoryFragment", "Error storing story.", e);
//                    }
//                }
//            }
//        });
//    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = getActivity().getIntent().getExtras();
        long storyId = extras.getLong("storyId");
        return new RawQueryLoader(getActivity(), R.string.sql_query_all_StoryItem, new String[]{String.valueOf(storyId)});
    }

    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        adapter.swapCursor(null);
    }
}
