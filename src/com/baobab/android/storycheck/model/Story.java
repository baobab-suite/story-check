package com.baobab.android.storycheck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dirk
 * Date: 2013/09/06
 * Time: 6:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Story {

    public int id;
    public String headline = "";
    public String type;
    public String createDate = "";
    public List<Item> items = new ArrayList<Item>();
}
