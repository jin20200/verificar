/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.browser.bookmarks;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.BrowserBookmark;

public class BrowserBookmarkItem extends BrowserBookmark implements Comparable<BrowserBookmarkItem>
{
    public BrowserBookmarkItem(@NonNull BrowserBookmark bookmark)
    {
        super(bookmark.url, bookmark.name, bookmark.dateAdded);
    }

    @Override
    public int compareTo(BrowserBookmarkItem o)
    {
        return Long.compare(o.dateAdded, dateAdded);
    }

    public boolean equalsContent(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrowserBookmark that = (BrowserBookmark) o;

        if (dateAdded != that.dateAdded) return false;
        if (!url.equals(that.url)) return false;
        return name.equals(that.name);
    }
}
