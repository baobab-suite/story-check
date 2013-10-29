package com.baobab.android.storycheck.data;

import com.baobab.android.storycheck.model.Item;
import com.baobab.android.storycheck.model.Story;
import com.baobab.android.storycheck.model.StoryList;
import com.baobab.android.storycheck.model.StoryType;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/07
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataLoader {

    private static DataLoader dataLoader = new DataLoader();
    private static ObjectMapper jacksonMapper  = new ObjectMapper();
    private static String TAG = "DataLoader";
    static{
        jacksonMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false).configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        jacksonMapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, false);
    }
    private List<StoryType> storyTypes;
    private List<Story> stories;

    private DataLoader() {
    }

    public static DataLoader getDataLoader(Context context){
        return dataLoader;
    }

    public List<StoryType> getStoryTypes() {
        return storyTypes;
    }

    public List<StoryType> loadStoryTypes(Context context) throws IOException {
        InputStream stream = null;
        try {
            long start = System.currentTimeMillis();
            stream = context.getAssets().open("model/checklists2.json");
            storyTypes = jacksonMapper.readValue(stream, new TypeReference<List<StoryType>>() {});
            long end = System.currentTimeMillis();
            Log.d(TAG, "loadStoryTypes=" + (end - start));
            return storyTypes;
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                // Nothing
            }
        }
    }

    public Story getStory(int id, Context context) throws IOException {
        List<Story> stories = getStories();
        if(stories == null){
            stories = loadStories(context);
        }
        if(stories == null){
            stories = new ArrayList<Story>();
        }
        for (Story story : stories) {
            if(story.id == id){
                return story;
            }
        }
        return null;
    }

    public Story createStory(Context context, StoryType storyType) throws IOException {
        List<Story> stories = getStories();
        if(stories == null){
            try {
                stories = loadStories(context);
            } catch (EOFException e) {
                stories = null;
            }
        }
        if(stories == null){
            stories = new ArrayList<Story>();
            this.stories = stories;
        }
        int newId = 0;
        for (Story story : stories) {
            newId = Math.max(newId, story.id);
        }
        newId++;
        Story story = new Story();
        story.headline = "";
        story.id = newId;
        story.createDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
        story.type = storyType.getName();
        for (Item item : storyType.getItems()) {
            story.items.add(item.copy());
        }
        stories.add(story);

        storeStories(context);
        return story;
    }

    public synchronized void storeStories(Context context) throws IOException {
        InputStream stream = null;
        try {
            long start = System.currentTimeMillis();
            jacksonMapper.writeValue(getStoriesFile(context), new StoryList(stories));
            long end = System.currentTimeMillis();
            Log.d(TAG, "storeStories=" + (end - start));
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                // Nothing
            }
        }
     }

    public synchronized List<Story> loadStories(Context context) throws IOException {
        InputStream stream = null;
        try {
            long start = System.currentTimeMillis();
            stories = jacksonMapper.readValue(getStoriesFile(context), StoryList.class).stories;
            long end = System.currentTimeMillis();
            Log.d(TAG, "loadStories=" + (end - start));
            return stories;
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                // Nothing
            }
        }
    }

    public static boolean isSdCardPresent() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public List<Story> getStories() {
        return stories;
    }

    public File getStoriesFile(Context context) throws IOException {
        File file = new File(context.getFilesDir(), "stories.json");
        file.createNewFile();
        return file;
    }
}
