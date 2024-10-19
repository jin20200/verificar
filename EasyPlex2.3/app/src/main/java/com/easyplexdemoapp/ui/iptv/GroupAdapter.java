package com.easyplexdemoapp.ui.iptv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.iptvplaylist.Group;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private List<Group> groups;
    private Context context;
    private SettingsManager settingsManager;
    private AuthManager authManager;
    private TokenManager tokenManager;

    public GroupAdapter(Context context, List<Group> groups,SettingsManager settingsManager,AuthManager authManager,TokenManager tokenManager) {
        this.context = context;
        this.groups = groups;
        this.settingsManager = settingsManager;
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupTitleTextView.setText(group.getTitle());

        PlaylistAdapter playlistAdapter = new PlaylistAdapter(context, group.getItems(),settingsManager,authManager,tokenManager);
        holder.itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.itemsRecyclerView.setAdapter(playlistAdapter);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupTitleTextView;
        RecyclerView itemsRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            groupTitleTextView = itemView.findViewById(R.id.groupTitleTextView);
            itemsRecyclerView = itemView.findViewById(R.id.itemsRecyclerView);
        }
    }
}