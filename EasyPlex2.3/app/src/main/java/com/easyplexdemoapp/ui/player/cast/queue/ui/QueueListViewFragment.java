/*
 * Copyright 2019 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easyplexdemoapp.ui.player.cast.queue.ui;

import static com.google.android.gms.cast.MediaStatus.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.player.cast.ExpandedControlsActivity;
import com.easyplexdemoapp.ui.player.cast.queue.QueueDataProvider;
import com.easyplexdemoapp.ui.player.cast.utils.Utils;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;

import timber.log.Timber;

/**
 * A fragment to show the list of queue items.
 */
public class QueueListViewFragment extends Fragment
        implements QueueListAdapter.OnStartDragListener {

    private QueueDataProvider mProvider;
    private ItemTouchHelper mItemTouchHelper;

    public QueueListViewFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mProvider = QueueDataProvider.getInstance(getContext());

        QueueListAdapter adapter = new QueueListAdapter(getActivity(), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new QueueItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.setEventListener(view1 -> {
            int id = view1.getId();
            if (id == R.id.container) {
                Timber.d("onItemViewClicked() container %s", view1.getTag(R.string.queue_tag_item));
                onContainerClicked(view1);
            } else if (id == R.id.play_pause) {
                Timber.d("onItemViewClicked() play-pause %s", view1.getTag(R.string.queue_tag_item));
                onPlayPauseClicked();
            } else if (id == R.id.play_upcoming) {
                mProvider.onUpcomingPlayClicked(
                        (MediaQueueItem) view1.getTag(R.string.queue_tag_item));
            } else if (id == R.id.stop_upcoming) {
                mProvider.onUpcomingStopClicked(
                        (MediaQueueItem) view1.getTag(R.string.queue_tag_item));
            }
        });
    }

    private void onPlayPauseClicked() {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient != null) {
            remoteMediaClient.togglePlayback();
        }
    }

    private void onContainerClicked(View view) {
        RemoteMediaClient remoteMediaClient = getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        MediaQueueItem item = (MediaQueueItem) view.getTag(R.string.queue_tag_item);
        if (mProvider.isQueueDetached()) {
            Timber.d("Is detached: itemId = %s", item.getItemId());

            int currentPosition = mProvider.getPositionByItemId(item.getItemId());
            MediaQueueItem[] items = Utils.rebuildQueue(mProvider.getItems());
            remoteMediaClient.queueLoad(items, currentPosition,
                    REPEAT_MODE_REPEAT_OFF, null);
        } else {
            int currentItemId = mProvider.getCurrentItemId();
            if (currentItemId == item.getItemId()) {
                // We selected the one that is currently playing so we take the user to the
                // full screen controller
                CastSession castSession = CastContext.getSharedInstance(
                        getContext().getApplicationContext())
                        .getSessionManager().getCurrentCastSession();
                if (castSession != null) {
                    Intent intent = new Intent(getActivity(), ExpandedControlsActivity.class);
                    startActivity(intent);
                }
            } else {
                // a different item in the queue was selected so we jump there
                remoteMediaClient.queueJumpToItem(item.getItemId(), null);
            }
        }
    }


    private RemoteMediaClient getRemoteMediaClient() {
        CastSession castSession =
                CastContext.getSharedInstance(getContext()).getSessionManager()
                        .getCurrentCastSession();
        if (castSession != null && castSession.isConnected()) {
            return castSession.getRemoteMediaClient();
        }
        return null;
    }
}
