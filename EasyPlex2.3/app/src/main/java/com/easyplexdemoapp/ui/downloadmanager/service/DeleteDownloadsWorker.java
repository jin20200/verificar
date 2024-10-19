

package com.easyplexdemoapp.ui.downloadmanager.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadEngine;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.storage.DataRepository;

import java.util.UUID;

import timber.log.Timber;

/*
 * Used only by DownloadEngine.
 */

public class DeleteDownloadsWorker extends Worker
{
    @SuppressWarnings("unused")
    private static final String TAG = DeleteDownloadsWorker.class.getSimpleName();

    public static final String TAG_ID_LIST = "id_list";
    public static final String TAG_WITH_FILE = "with_file";

    public DeleteDownloadsWorker(@NonNull Context context, @NonNull WorkerParameters params)
    {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork()
    {
        Context context = getApplicationContext();
        DownloadEngine engine = DownloadEngine.getInstance(context);
        DataRepository repo = RepositoryHelper.getDataRepository(context);

        Data data = getInputData();
        String[] idList = data.getStringArray(TAG_ID_LIST);
        boolean withFile = data.getBoolean(TAG_WITH_FILE, false);
        if (idList == null)
            return Result.failure();

        for (String id : idList) {
            if (id == null)
                continue;
            UUID uuid;
            try {
                uuid = UUID.fromString(id);

            } catch (IllegalArgumentException e) {
                continue;
            }

            DownloadInfo info = repo.getInfoById(uuid);
            if (info == null)
                continue;
            try {
                engine.doDeleteDownload(info, withFile);

            } catch (Exception e) {
                Timber.tag(TAG).e(Log.getStackTraceString(e));
            }
        }

        return Result.success();
    }
}
