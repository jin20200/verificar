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

package com.easyplexdemoapp.ui.downloadmanager.ui.adddownload;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.FragmentCallback;

public class AddDownloadActivity extends AppCompatActivity
    implements FragmentCallback
{
    public static final String TAG_INIT_PARAMS = "init_params";

    private static final String TAG_DOWNLOAD_DIALOG = "add_download_dialog";

    private AddDownloadDialog addDownloadDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        setTheme(Utils.getTranslucentAppTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        addDownloadDialog = (AddDownloadDialog)fm.findFragmentByTag(TAG_DOWNLOAD_DIALOG);
        if (addDownloadDialog == null) {
            AddInitParams initParams = null;
            Intent i = getIntent();
            if (i != null)
                initParams = i.getParcelableExtra(TAG_INIT_PARAMS);
            if (initParams == null) {
                initParams = new AddInitParams();
            }
            fillInitParams(initParams);
            addDownloadDialog = AddDownloadDialog.newInstance(initParams);
            addDownloadDialog.show(fm, TAG_DOWNLOAD_DIALOG);
        }
    }

    private void fillInitParams(AddInitParams params)
    {
        SettingsRepository pref = RepositoryHelper.getSettingsRepository(getApplicationContext());
        SharedPreferences localPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (params.url == null) {
            params.url = getUrlFromIntent();
        }
        if (params.dirPath == null) {
            params.dirPath = Uri.parse(pref.saveDownloadsIn());
        }
        if (params.retry == null) {
            params.retry = localPref.getBoolean(
                    getString(R.string.add_download_retry_flag),
                    true
            );
        }
        if (params.replaceFile == null) {
            params.replaceFile = localPref.getBoolean(
                    getString(R.string.add_download_replace_file_flag),
                    false
            );
        }
        if (params.unmeteredConnectionsOnly == null) {
            params.unmeteredConnectionsOnly = localPref.getBoolean(
                    getString(R.string.add_download_unmetered_only_flag),
                    false
            );
        }
        if (params.numPieces == null) {
            params.numPieces = localPref.getInt(
                    getString(R.string.add_download_num_pieces),
                    DownloadInfo.MIN_PIECES
            );
        }
    }



    private String getUrlFromIntent()
    {
        Intent i = getIntent();
        if (i != null) {
            if (i.getData() != null)
                return i.getData().toString();
            else
                return i.getStringExtra(Intent.EXTRA_TEXT);
        }

        return null;
    }

    @Override
    public void fragmentFinished(Intent intent, ResultCode code)
    {
        finish();
    }

    @Override
    public void onBackPressed()
    {
        addDownloadDialog.onBackPressed();
    }
}
