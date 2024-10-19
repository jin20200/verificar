/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.core.storage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadPiece;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.Header;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.InfoAndPieces;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.UserAgent;
import com.easyplexdemoapp.ui.downloadmanager.core.system.FileSystemFacade;
import com.easyplexdemoapp.ui.downloadmanager.core.system.SystemFacadeHelper;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.Single;
import timber.log.Timber;

public class DataRepositoryImpl implements DataRepository
{
    @SuppressWarnings("unused")
    private static final String TAG = DataRepositoryImpl.class.getSimpleName();

    private final Context appContext;
    private final AppDatabase db;
    private final MediatorLiveData<List<UserAgent>> userAgents;
    private final FileSystemFacade fs;

    public DataRepositoryImpl(@NonNull Context appContext, @NonNull AppDatabase db)
    {
        this.appContext = appContext;
        this.db = db;
        fs = SystemFacadeHelper.getFileSystemFacade(appContext);
        userAgents = new MediatorLiveData<>();

        userAgents.addSource(db.userAgentDao().observeAll(),
                productEntities -> {
                    if (db.getDatabaseCreated().getValue() != null)
                        userAgents.postValue(productEntities);
                });
    }

    @Override
    public void addInfo(DownloadInfo info, List<Header> headers)
    {
        db.downloadDao().addInfo(info, headers);
    }

    @Override
    public void replaceInfoByUrl(DownloadInfo info, List<Header> headers)
    {
        db.downloadDao().replaceInfoByUrl(info, headers);
    }

    @Override
    public void updateInfo(DownloadInfo info,
                           boolean filePathChanged,
                           boolean rebuildPieces)
    {
        if (filePathChanged) {
            DownloadInfo oldInfo = db.downloadDao().getInfoById(info.id);
            if (oldInfo == null)
                return;
        }
        if (rebuildPieces)
            db.downloadDao().updateInfoWithPieces(info);
        else
            db.downloadDao().updateInfo(info);
    }

    @Override
    public void deleteInfo(DownloadInfo info, boolean withFile)
    {
        db.downloadDao().deleteInfo(info);

        if (withFile) {
            try {
                Uri filePath = fs.getFileUri(info.dirPath, info.fileName);
                if (filePath == null)
                    return;
                fs.deleteFile(filePath);

            } catch (FileNotFoundException | SecurityException e) {
                Timber.tag(TAG).w(Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public Flowable<List<InfoAndPieces>> observeAllInfoAndPieces()
    {
        return db.downloadDao().observeAllInfoAndPieces();
    }

    @Override
    public Flowable<InfoAndPieces> observeInfoAndPiecesById(UUID id)
    {
        return db.downloadDao().observeInfoAndPiecesById(id);
    }

    @Override
    public Single<List<InfoAndPieces>> getAllInfoAndPiecesSingle()
    {
        return db.downloadDao().getAllInfoAndPiecesSingle();
    }

    @Override
    public List<DownloadInfo> getAllInfo()
    {
        return db.downloadDao().getAllInfo();
    }

    @Override
    public DownloadInfo getInfoById(UUID id)
    {
        return db.downloadDao().getInfoById(id);
    }

    @Override
    public Single<DownloadInfo> getInfoByIdSingle(UUID id)
    {
        return db.downloadDao().getInfoByIdSingle(id);
    }

    @Override
    public int updatePiece(DownloadPiece piece)
    {
        return db.downloadDao().updatePiece(piece);
    }

    @Override
    public List<DownloadPiece> getPiecesById(UUID infoId)
    {
        return db.downloadDao().getPiecesById(infoId);
    }

    /*
     * Sorted by status code
     */

    @Override
    public List<DownloadPiece> getPiecesByIdSorted(UUID infoId)
    {
        return db.downloadDao().getPiecesByIdSorted(infoId);
    }

    @Override
    public DownloadPiece getPiece(int index, UUID infoId)
    {
        return db.downloadDao().getPiece(index, infoId);
    }

    @Override
    public List<Header> getHeadersById(UUID infoId)
    {
        return db.downloadDao().getHeadersById(infoId);
    }

    @Override
    public void addHeader(Header header)
    {
        db.downloadDao().addHeader(header);
    }

    @Override
    public void addUserAgent(UserAgent agent)
    {
        db.userAgentDao().add(agent);
    }

    @Override
    public void deleteUserAgent(UserAgent agent)
    {
        db.userAgentDao().delete(agent);
    }

    @Override
    public LiveData<List<UserAgent>> observeUserAgents()
    {
        return db.userAgentDao().observeAll();
    }
}