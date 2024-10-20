/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.model;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.UUID;

/*
 * The priority queue if we want to defer download for an indefinite period of time,
 * for example, simultaneous downloads.
 */

class DownloadQueue
{
    @SuppressWarnings("unused")
    private static final String TAG = DownloadQueue.class.getSimpleName();

    private final ArrayDeque<UUID> queue = new ArrayDeque<>();

    public void push(@NonNull UUID downloadId)
    {
        if (queue.contains(downloadId))
            return;
        queue.push(downloadId);
    }

    public UUID pop()
    {
        UUID downloadId = null;
        while (downloadId == null) {
            try {
                downloadId = queue.pop();

            } catch (NoSuchElementException e) {
                /* Queue is empty, return */
                return null;
            }
        }

        return downloadId;
    }
}