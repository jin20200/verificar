package com.easyplexdemoapp.data.model.iptvplaylist;

import java.util.ArrayList;
import java.util.List;

// Group.java
public class Group {
    private String title;
    private List<PlaylistItem> items;

    public Group(String title) {
        this.title = title;
        this.items = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public List<PlaylistItem> getItems() { return items; }
    public void addItem(PlaylistItem item) { items.add(item); }
}