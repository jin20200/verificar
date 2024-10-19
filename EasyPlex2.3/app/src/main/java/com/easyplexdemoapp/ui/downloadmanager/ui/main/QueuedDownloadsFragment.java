/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.StatusCode;

public class QueuedDownloadsFragment extends DownloadsFragment implements DownloadListAdapter.QueueClickListener
{

    public static QueuedDownloadsFragment newInstance()
    {
        QueuedDownloadsFragment fragment = new QueuedDownloadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public QueuedDownloadsFragment()
    {
        super(item -> !StatusCode.isStatusCompleted(item.info.statusCode));
    }

    @Override
    public void onStart()
    {
        super.onStart();

        binding.fragmentTitleDownloadCompleted.setVisibility(View.GONE);
        binding.fragmentTitleDownloadQueue.setVisibility(View.VISIBLE);
        binding.messageCompletedDownloadsEmpty.setVisibility(View.GONE);
        binding.messageQueueDownloadsEmpty.setVisibility(View.VISIBLE);
        binding.messageCompletedDownloadsEmpty.setText(getString(R.string.queue_download_message_fragment));

        subscribeAdapter();
    }

    @Override
    public void onItemClicked(@NonNull DownloadItem item)
    {
        showDetailsDialog(item.info.id);
    }

    @Override
    public void onItemPauseClicked(@NonNull DownloadItem item)
    {
        viewModel.pauseResumeDownload(item.info);
    }

    @Override
    public void onItemCancelClicked(@NonNull DownloadItem item)
    {
        viewModel.deleteDownload(item.info, true);
    }
}
