/*
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.BrowserBookmark;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface BrowserRepository
{
    Single<Long> addBookmark(BrowserBookmark bookmark);

    Single<Integer> deleteBookmarks(List<BrowserBookmark> bookmarks);

    Single<Integer> updateBookmark(BrowserBookmark bookmark);

    Single<BrowserBookmark> getBookmarkByUrlSingle(String url);

    Flowable<List<BrowserBookmark>> observeAllBookmarks();
}
