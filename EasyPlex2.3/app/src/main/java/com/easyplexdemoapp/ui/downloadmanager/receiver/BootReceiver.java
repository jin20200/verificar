/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadEngine;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;

/*
 * The receiver for autostart stopped downloads.
 */

public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction() == null)
            return;

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SettingsRepository pref = RepositoryHelper.getSettingsRepository(context.getApplicationContext());
            if (pref.autostart()) {
                DownloadEngine engine = DownloadEngine.getInstance(context.getApplicationContext());
                engine.restoreDownloads();
            }
        }
    }
}
