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

package com.easyplexdemoapp.ui.downloadmanager.service;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;

import java.util.UUID;

/*
 * Used only by DownloadScheduler.
 */

public class RunDownloadWorker extends Worker
{
    public static final String TAG_ID = "id";

    public RunDownloadWorker(@NonNull Context context, @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Data data = getInputData();
        String uuid = data.getString(TAG_ID);
        if (uuid == null)
            return Result.failure();

        UUID id;
        try {
            id = UUID.fromString(uuid);

        } catch (IllegalArgumentException e) {
            return Result.failure();
        }

        runDownloadAction(id);

        return Result.success();
    }

    private void runDownloadAction(UUID id)
    {
        /*
         * Use a foreground service, because WorkManager has a 10 minute work limit,
         * which may be less than the download time
         */
        Intent i = new Intent(getApplicationContext(), DownloadService.class);
        i.setAction(DownloadService.ACTION_RUN_DOWNLOAD);
        i.putExtra(DownloadService.TAG_DOWNLOAD_ID, id);
        Utils.startServiceBackground(getApplicationContext(), i);
    }
}
