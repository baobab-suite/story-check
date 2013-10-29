package com.baobab.android.storycheck.model;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryType {

    private String name;
    private String key;
    private Item[] items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
