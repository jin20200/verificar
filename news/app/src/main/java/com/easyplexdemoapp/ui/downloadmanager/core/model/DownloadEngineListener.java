/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

public abstract class DownloadEngineListener
{
    public void onDownloadsCompleted() {}

    public void onApplyingParams(@NonNull UUID id) {}

    public void onParamsApplied(@NonNull UUID id, @Nullable String name, @Nullable Throwable e) {}
}
