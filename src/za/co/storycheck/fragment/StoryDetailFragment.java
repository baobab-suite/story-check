package za.co.storycheck.fragment;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/12/26
 * Time: 4:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoryDetailFragment {

    void setStory(long storyId, String headline);

    void clearSelection();
}
