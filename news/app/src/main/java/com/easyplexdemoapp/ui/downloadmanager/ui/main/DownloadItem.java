/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.InfoAndPieces;

/*
 * Wrapper of InfoAndPieces class for DownloadListAdapter, that override Object::equals method
 * Necessary for other behavior in case if item was selected (see SelectionTracker).
 */

public class DownloadItem extends InfoAndPieces
{
    public DownloadItem(@NonNull InfoAndPieces infoAndPieces)
    {
        this.info = infoAndPieces.info;
        this.pieces = infoAndPieces.pieces;
    }

    /*
     * Compare objects by their content (info, pieces)
     */

    public boolean equalsContent(DownloadItem item)
    {
        return super.equals(item);
    }

    /*
     * Compare objects by info id
     */

    @Override
    public boolean equals(@Nullable Object o)
    {
        if (!(o instanceof DownloadItem))
            return false;

        if (o == this)
            return true;

        return info.id.equals(((DownloadItem)o).info.id);
    }
}
