/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadScheduler;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.StatusCode;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.DataRepository;

import java.util.List;

/*
 * Used only by DownloadScheduler.
 */

public class RestoreDownloadsWorker extends Worker
{
    @SuppressWarnings("unused")
    private static final String TAG = RestoreDownloadsWorker.class.getSimpleName();

    public RestoreDownloadsWorker(@NonNull Context context, @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Context context = getApplicationContext();
        DataRepository repo = RepositoryHelper.getDataRepository(context);

        List<DownloadInfo> infoList = repo.getAllInfo();
        if (infoList.isEmpty())
            return Result.success();

        for (DownloadInfo info : infoList) {
            if (info == null)
                continue;
            /*
             * Also restore those downloads that are incorrectly completed and
             * have the wrong status (for example, after crashing)
             */
            if (info.statusCode == StatusCode.STATUS_PENDING ||
                info.statusCode == StatusCode.STATUS_RUNNING ||
                info.statusCode == StatusCode.STATUS_FETCH_METADATA)
                DownloadScheduler.run(context, info);
        }

        return Result.success();
    }
}
