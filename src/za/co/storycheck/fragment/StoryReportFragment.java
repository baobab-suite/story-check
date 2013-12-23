package za.co.storycheck.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ShareActionProvider;
import za.co.storycheck.R;
import za.co.storycheck.dto.ReportDto;
import za.co.storycheck.loaders.StoryReportLoader;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<ReportDto> {


    private WebView reportView;
    private ShareActionProvider shareActionProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.story_report_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reportView = (WebView) view.findViewById(R.id.wv_story_report);
        getActivity().getSupportLoaderManager().initLoader(0, savedInstanceState, this);
    }

    public Loader<ReportDto> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = getActivity().getIntent().getExtras();
        long storyId = extras.getLong("storyId");
        String headline = extras.getString("headline");
        return new StoryReportLoader(getActivity(), storyId, headline);
    }

    public void onLoadFinished(Loader<ReportDto> spannedLoader, ReportDto dto) {
        reportView.loadData(dto.getHtml(), "text/plain", null);
        Bundle extras = getActivity().getIntent().getExtras();
        String headline = extras.getString("headline");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE, "StoryCheck report for: " + headline);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "StoryCheck report for: "+headline);
        shareIntent.putExtra(Intent.EXTRA_TEXT, dto.getString());
        shareIntent.setType("text/html");
        if (shareActionProvider != null){
            shareActionProvider.setShareIntent(shareIntent);
        }else{
            getActivity().getSupportLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.story_report_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.mi_share);

        // Fetch and store ShareActionProvider
        shareActionProvider = (ShareActionProvider) item.getActionProvider();
    }

    public void onLoaderReset(Loader<ReportDto> cursorLoader) {

    }
}
