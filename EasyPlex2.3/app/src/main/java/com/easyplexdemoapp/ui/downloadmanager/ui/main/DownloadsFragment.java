/*
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

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.MutableSelection;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.FragmentDownloadListBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.downloadmanager.core.filter.DownloadFilter;
import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadEngine;
import com.easyplexdemoapp.ui.downloadmanager.service.DownloadService;
import com.easyplexdemoapp.ui.downloadmanager.ui.BaseAlertDialog;
import com.easyplexdemoapp.ui.downloadmanager.ui.details.DownloadDetailsDialog;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.receiver.NetworkChangeReceiver;

import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/*
 * A base fragment for individual fragment with sorted content (queued and completed downloads)
 */

public abstract class DownloadsFragment extends Fragment
    implements DownloadListAdapter.ClickListener , Injectable
{


    @Inject
    MediaRepository mediaRepository;

    @Inject
    NetworkChangeReceiver networkChangeReceiver;

    @Inject
    SettingsManager settingsManager;

    private static final String TAG_DOWNLOAD_LIST_STATE = "download_list_state";
    private static final String SELECTION_TRACKER_ID = "selection_tracker_0";
    private static final String TAG_DELETE_DOWNLOADS_DIALOG = "delete_downloads_dialog";
    private static final String TAG_DOWNLOAD_DETAILS = "download_details";
    protected DownloadListAdapter adapter;
    private LinearLayoutManager layoutManager;
    /* Save state scrolling */
    private Parcelable downloadListState;
    private SelectionTracker<DownloadItem> selectionTracker;
    private ActionMode actionMode;
    FragmentDownloadListBinding binding;
    DownloadsViewModel viewModel;
    CompositeDisposable disposables = new CompositeDisposable();
    private DownloadEngine engine;
    private BaseAlertDialog deleteDownloadsDialog;
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private final DownloadFilter fragmentDownloadsFilter;

    protected DownloadsFragment(DownloadFilter fragmentDownloadsFilter)
    {
        this.fragmentDownloadsFilter = fragmentDownloadsFilter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_download_list, container, false);

        IntentFilter intentFilter = new IntentFilter(CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeReceiver, intentFilter);
        requireActivity().unregisterReceiver(networkChangeReceiver);

        setHasOptionsMenu(true);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(DownloadsViewModel.class);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);

        FragmentManager fm = getChildFragmentManager();
        deleteDownloadsDialog = (BaseAlertDialog)fm.findFragmentByTag(TAG_DELETE_DOWNLOADS_DIALOG);

        adapter = new DownloadListAdapter(this,mediaRepository,settingsManager);

        /*
         * A RecyclerView by default creates another copy of the ViewHolder in order to
         * fade the views into each other. This causes the problem because the old ViewHolder gets
         * the payload but then the new one doesn't. So needs to explicitly tell it to reuse the old one.
         */
        DefaultItemAnimator animator = new DefaultItemAnimator()
        {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder)
            {
                return true;
            }
        };
        layoutManager = new LinearLayoutManager((requireActivity()));
        binding.downloadList.setLayoutManager(layoutManager);
        binding.downloadList.setItemAnimator(animator);
        binding.downloadList.setEmptyView(binding.emptyViewDownloadList);
        binding.downloadList.setAdapter(adapter);

        selectionTracker = new SelectionTracker.Builder<>(
                SELECTION_TRACKER_ID,
                binding.downloadList,
                new DownloadListAdapter.KeyProvider(adapter),
                new DownloadListAdapter.ItemLookup(binding.downloadList),
                StorageStrategy.createParcelableStorage(DownloadItem.class))
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build();

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<>() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();

                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
                    setActionModeTitle(selectionTracker.getSelection().size());

                } else if (!selectionTracker.hasSelection()) {
                    if (actionMode != null)
                        actionMode.finish();
                    actionMode = null;

                } else {
                    setActionModeTitle(selectionTracker.getSelection().size());
                }
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();

                actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
                setActionModeTitle(selectionTracker.getSelection().size());
            }
        });

        if (savedInstanceState != null)
            selectionTracker.onRestoreInstanceState(savedInstanceState);
        adapter.setSelectionTracker(selectionTracker);


        engine = DownloadEngine.getInstance(requireActivity());
        engine.restoreDownloads();

        return binding.getRoot();
    }



    @Override
    public void onStop()
    {
        super.onStop();

        disposables.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        subscribeAlertDialog();
        subscribeForceSortAndFilter();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe(event -> {
                    if (event.dialogTag == null || !event.dialogTag.equals(TAG_DELETE_DOWNLOADS_DIALOG) || deleteDownloadsDialog == null)
                        return;
                    if (event.type == BaseAlertDialog.EventType.POSITIVE_BUTTON_CLICKED) {
                        Dialog dialog = deleteDownloadsDialog.getDialog();
                        if (dialog != null) {
                            CheckBox withFile = dialog.findViewById(R.id.delete_with_file);
                            deleteDownloads(withFile.isChecked());
                        }
                        if (actionMode != null)
                            actionMode.finish();

                        deleteDownloadsDialog.dismiss();
                    } else if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                        deleteDownloadsDialog.dismiss();
                    }
                });
        disposables.add(d);
    }

    private void subscribeForceSortAndFilter()
    {
        disposables.add(viewModel.onForceSortAndFilter()
                .filter(force -> force)
                .observeOn(Schedulers.io())
                .subscribe(force -> disposables.add(getDownloadSingle())));
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (downloadListState != null)
            layoutManager.onRestoreInstanceState(downloadListState);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.pause_all_menu) {
            pauseAll();
        } else if (itemId == R.id.resume_all_menu) {
            resumeAll();
        }
        return true;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null)
            downloadListState = savedInstanceState.getParcelable(TAG_DOWNLOAD_LIST_STATE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        downloadListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(TAG_DOWNLOAD_LIST_STATE, downloadListState);
        selectionTracker.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }

    protected void subscribeAdapter()
    {
        disposables.add(observeDownloads());
    }

    public Disposable observeDownloads()
    {
        return viewModel.observerAllInfoAndPieces()
                .subscribeOn(Schedulers.io())
                .flatMapSingle(infoAndPiecesList ->
                        Flowable.fromIterable(infoAndPiecesList)
                                .filter(fragmentDownloadsFilter)
                                .filter(viewModel.getDownloadFilter())
                                .map(DownloadItem::new)
                                .sorted(viewModel.getSorting())
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList,
                        (Throwable t) -> Timber.e("Getting info and pieces error: %s", Log.getStackTraceString(t)));
    }

    public Disposable getDownloadSingle()
    {
        return viewModel.getAllInfoAndPiecesSingle()
                .subscribeOn(Schedulers.io())
                .flatMap((infoAndPiecesList) ->
                        Observable.fromIterable(infoAndPiecesList)
                                .filter(fragmentDownloadsFilter)
                                .filter(viewModel.getDownloadFilter())
                                .map(DownloadItem::new)
                                .sorted(viewModel.getSorting())
                                .toList()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::submitList,
                        (Throwable t) -> Timber.e("Getting info and pieces error: %s", Log.getStackTraceString(t)));
    }

    @Override
    public abstract void onItemClicked(@NonNull DownloadItem item);

    private void setActionModeTitle(int itemCount)
    {
        actionMode.setTitle(String.valueOf(itemCount));
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback()
    {
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            mode.getMenuInflater().inflate(R.menu.download_list_action_mode, menu);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            int itemId = item.getItemId();
            if (itemId == R.id.delete_menu) {
                deleteDownloadsDialog();
            } else if (itemId == R.id.select_all_menu) {
                selectAllDownloads();
                mode.finish();
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            selectionTracker.clearSelection();
        }
    };

    private void deleteDownloadsDialog()
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_DELETE_DOWNLOADS_DIALOG) == null) {
            deleteDownloadsDialog = BaseAlertDialog.newInstance(
                    getString(R.string.deleting),
                    (selectionTracker.getSelection().size() > 1 ?
                            getString(R.string.delete_selected_downloads) :
                            getString(R.string.delete_selected_download)),
                    R.layout.dialog_delete_downloads,
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    null,
                    false);

            deleteDownloadsDialog.show(fm, TAG_DELETE_DOWNLOADS_DIALOG);
        }
    }

    private void deleteDownloads(boolean withFile)
    {
        MutableSelection<DownloadItem> selections = new MutableSelection<>();
        selectionTracker.copySelection(selections);

        disposables.add(Observable.fromIterable(selections)
                .map((selection -> selection.info))
                .toList()
                .subscribe(infoList -> viewModel.deleteDownloads(infoList, withFile)));
    }

    @SuppressLint("RestrictedApi")
    private void selectAllDownloads()
    {
        int n = adapter.getItemCount();
        if (n > 0) {
            selectionTracker.startRange(0);
            selectionTracker.extendRange(adapter.getItemCount() - 1);
        }
    }

    protected void showDetailsDialog(UUID id)
    {
        if (!isAdded())
            return;

        FragmentManager fm = getChildFragmentManager();
        if (fm.findFragmentByTag(TAG_DOWNLOAD_DETAILS) == null) {
            DownloadDetailsDialog details = DownloadDetailsDialog.newInstance(id);
            details.show(fm, TAG_DOWNLOAD_DETAILS);
        }
    }


    private void pauseAll()
    {
        engine.pauseAllDownloads();
    }

    private void resumeAll()
    {
        engine.resumeDownloads(false);
    }

    public void shutdown()
    {
        Intent i = new Intent(requireActivity(), DownloadService.class);
        i.setAction(DownloadService.ACTION_SHUTDOWN);
        requireActivity().startService(i);
        requireActivity().finish();
    }


}
