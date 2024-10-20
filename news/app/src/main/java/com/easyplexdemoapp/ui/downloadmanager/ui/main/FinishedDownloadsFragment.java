/* * EasyPlex - Movies - Live Streaming - TV Series, Anime * * @author @Y0bEX * @package EasyPlex - Movies - Live Streaming - TV Series, Anime * @copyright Copyright (c) 2021 Y0bEX, * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/ * @profile https://codecanyon.net/user/yobex * @link yobexd@gmail.com * @skype yobexd@gmail.com **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.StatusCode;
import com.easyplexdemoapp.ui.downloadmanager.core.model.data.entity.DownloadInfo;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.BaseAlertDialog;

import io.reactivex.disposables.Disposable;

public class FinishedDownloadsFragment extends DownloadsFragment implements DownloadListAdapter.FinishClickListener, DownloadListAdapter.ErrorClickListener
{


    private static final String TAG_DELETE_DOWNLOAD_DIALOG = "delete_download_dialog";
    private static final String TAG_DOWNLOAD_FOR_DELETION = "download_for_deletion";
    private BaseAlertDialog deleteDownloadDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private DownloadInfo downloadForDeletion;

    public static FinishedDownloadsFragment newInstance()
    {
        FinishedDownloadsFragment fragment = new FinishedDownloadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public FinishedDownloadsFragment()
    {
        super(item -> StatusCode.isStatusCompleted(item.info.statusCode));
    }

    @Override
    public void onStart()
    {
        super.onStart();

        binding.fragmentTitleDownloadCompleted.setVisibility(View.VISIBLE);
        binding.fragmentTitleDownloadQueue.setVisibility(View.GONE);
        binding.messageCompletedDownloadsEmpty.setVisibility(View.VISIBLE);
        binding.messageQueueDownloadsEmpty.setVisibility(View.GONE);
        binding.messageCompletedDownloadsEmpty.setText(getString(R.string.completed_download_message_fragment));

        subscribeAdapter();
        subscribeAlertDialog();
    }


    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe(event -> {
                    if (event.dialogTag == null || !event.dialogTag.equals(TAG_DELETE_DOWNLOAD_DIALOG) || deleteDownloadDialog == null)
                        return;
                    if (event.type == BaseAlertDialog.EventType.POSITIVE_BUTTON_CLICKED) {
                        Dialog dialog = deleteDownloadDialog.getDialog();
                        if (dialog != null && downloadForDeletion != null) {
                            CheckBox withFile = dialog.findViewById(R.id.delete_with_file);
                            deleteDownload(downloadForDeletion, withFile.isChecked());
                        }

                        downloadForDeletion = null;
                        deleteDownloadDialog.dismiss();
                    } else if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                        downloadForDeletion = null;
                        deleteDownloadDialog.dismiss();
                    }
                });
        disposables.add(d);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null)
            downloadForDeletion = savedInstanceState.getParcelable(TAG_DOWNLOAD_FOR_DELETION);

        FragmentManager fm = getChildFragmentManager();
        deleteDownloadDialog = (BaseAlertDialog)fm.findFragmentByTag(TAG_DELETE_DOWNLOAD_DIALOG);
        dialogViewModel = new ViewModelProvider((requireActivity())).get(BaseAlertDialog.SharedViewModel.class);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putParcelable(TAG_DOWNLOAD_FOR_DELETION, downloadForDeletion);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(@NonNull DownloadItem item) {
        Intent file = Utils.createOpenFileIntent(requireActivity().getApplicationContext(), item.info);
        if (file != null) {
            startActivity(Intent.createChooser(file, getString(R.string.open_using)));
        } else {
            Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.file_not_available), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemMenuClicked(int menuId, @NonNull DownloadItem item)
    {
        if (menuId == R.id.delete_menu) {
            downloadForDeletion = item.info;
            showDeleteDownloadDialog();
        }
    }

    @Override
    public void onItemResumeClicked(@NonNull DownloadItem item)
    {
        viewModel.resumeIfError(item.info);
    }

    private void showDeleteDownloadDialog()
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_DELETE_DOWNLOAD_DIALOG) == null) {
            deleteDownloadDialog = BaseAlertDialog.newInstance(
                    getString(R.string.deleting),
                    getString(R.string.delete_selected_download),
                    R.layout.dialog_delete_downloads,
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    null,
                    false);

            deleteDownloadDialog.show(fm, TAG_DELETE_DOWNLOAD_DIALOG);
        }
    }

    private void deleteDownload(DownloadInfo info, boolean withFile)
    {
        viewModel.deleteDownload(info, withFile);
    }
}
