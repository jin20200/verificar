/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.sorting;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.ui.downloadmanager.ui.main.DownloadItem;

import java.util.Comparator;

public class DownloadSortingComparator implements Comparator<DownloadItem>
{
    private final DownloadSorting sorting;

    public DownloadSortingComparator(@NonNull DownloadSorting sorting)
    {
        this.sorting = sorting;
    }

    public DownloadSorting getSorting()
    {
        return sorting;
    }

    @Override
    public int compare(DownloadItem o1, DownloadItem o2)
    {
        return DownloadSorting.SortingColumns.fromValue(sorting.getColumnName())
                .compare(o1, o2, sorting.getDirection());
    }
}
