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

/*
 * Provides information about the download thread status after stopping.
 */

public class DownloadResult
{
    public enum Status
    {
        FINISHED,
        PAUSED,
        STOPPED
    }

    public UUID infoId;
    public Status status;

    public DownloadResult(UUID infoId, Status status)
    {
        this.infoId = infoId;
        this.status = status;
    }
}
