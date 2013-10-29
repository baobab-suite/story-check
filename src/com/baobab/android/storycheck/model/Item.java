package com.baobab.android.storycheck.model;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Item {
    public String label;
    public int state = 0;
    public String note;

    public Item copy() {
        Item item = new Item();
        item.label = this.label;
        item.state = this.state;
        return item;
    }

    public void toggleState(){
        state++;
        if (state > 2) state = 0;
    }
}
