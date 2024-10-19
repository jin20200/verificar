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

package com.easyplexdemoapp.ui.downloadmanager.core.model.data;

import java.util.UUID;

public class PieceResult
{
    public UUID infoId;
    public int pieceIndex;
    public long retryAfter;

    public PieceResult(UUID infoId, int pieceIndex)
    {
        this.infoId = infoId;
        this.pieceIndex = pieceIndex;
    }
}
