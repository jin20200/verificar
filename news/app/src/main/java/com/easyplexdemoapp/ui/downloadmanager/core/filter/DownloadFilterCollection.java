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

package com.easyplexdemoapp.ui.downloadmanager.core.filter;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.StatusCode;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.DateUtils;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.MimeTypeUtils;

public class DownloadFilterCollection
{
    public static DownloadFilter all()
    {
        return (infoAndPieces) -> true;
    }

    public static DownloadFilter category(@NonNull MimeTypeUtils.Category category)
    {
        return (infoAndPieces) -> MimeTypeUtils.getCategory(infoAndPieces.info.mimeType).equals(category);
    }

    public static DownloadFilter statusStopped()
    {
        return (infoAndPieces) -> StatusCode.isStatusStoppedOrPaused(infoAndPieces.info.statusCode);
    }

    public static DownloadFilter statusRunning()
    {
        return (infoAndPieces) ->
                infoAndPieces.info.statusCode == StatusCode.STATUS_RUNNING ||
                        infoAndPieces.info.statusCode == StatusCode.STATUS_FETCH_METADATA;
    }

    public static DownloadFilter dateAddedToday()
    {
        return (infoAndPieces) -> {
            long dateAdded = infoAndPieces.info.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfToday(timeMillis) &&
                    dateAdded <= DateUtils.endOfToday(timeMillis);
        };
    }

    public static DownloadFilter dateAddedYesterday()
    {
        return (infoAndPieces) -> {
            long dateAdded = infoAndPieces.info.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfYesterday(timeMillis) &&
                    dateAdded <= DateUtils.endOfYesterday(timeMillis);
        };
    }

    public static DownloadFilter dateAddedWeek()
    {
        return (infoAndPieces) -> {
            long dateAdded = infoAndPieces.info.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfWeek(timeMillis) &&
                    dateAdded <= DateUtils.endOfWeek(timeMillis);
        };
    }

    public static DownloadFilter dateAddedMonth()
    {
        return (infoAndPieces) -> {
            long dateAdded = infoAndPieces.info.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfMonth(timeMillis) &&
                    dateAdded <= DateUtils.endOfMonth(timeMillis);
        };
    }

    public static DownloadFilter dateAddedYear()
    {
        return (infoAndPieces) -> {
            long dateAdded = infoAndPieces.info.dateAdded;
            long timeMillis = System.currentTimeMillis();

            return dateAdded >= DateUtils.startOfYear(timeMillis) &&
                    dateAdded <= DateUtils.endOfYear(timeMillis);
        };
    }
}
