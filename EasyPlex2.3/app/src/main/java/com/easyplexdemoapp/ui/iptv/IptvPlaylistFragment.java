package com.easyplexdemoapp.ui.iptv;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.iptvplaylist.Group;
import com.easyplexdemoapp.data.model.iptvplaylist.PlaylistItem;
import com.easyplexdemoapp.databinding.PlaylistLayoutBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.users.MenuHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IptvPlaylistFragment extends Fragment implements Injectable {

    PlaylistLayoutBinding binding;

    private GroupAdapter adapter;
    private List<Group> groups;
    private List<Group> allGroups;

    @Inject
    SettingsManager settingsManager;

    @Inject
    TokenManager tokenManager;

    @Inject
    AuthManager authManager;

    @Inject
    MenuHandler menuHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.playlist_layout, container, false);
        binding.setController(menuHandler);
        setupToolbar();
        setupRecyclerView();
        setupSearchListener();
        setupClearButtonListener();

        loadPlaylist();

        binding.retryPlaylist.setOnClickListener(v -> loadPlaylist());

        return binding.getRoot();
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        binding.toolbar.setTitle(null);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        groups = new ArrayList<>();
        allGroups = new ArrayList<>();
        adapter = new GroupAdapter(requireContext(), groups, settingsManager, authManager, tokenManager);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadPlaylist() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.playerError.setVisibility(View.GONE);
        new FetchPlaylistTask().execute(settingsManager.getSettings().getM3uplaylistpath());
    }

    private void setupClearButtonListener() {
        binding.btClear.setOnClickListener(v -> {
            binding.etSearch.setText("");
            resetToInitialState();
        });
    }

    private void updateClearButtonVisibility(String query) {
        binding.btClear.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void resetToInitialState() {
        groups.clear();
        groups.addAll(allGroups);
        adapter.notifyDataSetChanged();
        updateClearButtonVisibility("");
    }

    private void setupSearchListener() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateClearButtonVisibility(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterGroups(s.toString());
            }
        });
    }

    private void filterGroups(String query) {
        groups.clear();
        if (query.isEmpty()) {
            groups.addAll(allGroups);
        } else {
            for (Group group : allGroups) {
                Group filteredGroup = new Group(group.getTitle());
                for (PlaylistItem item : group.getItems()) {
                    if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        filteredGroup.addItem(item);
                    }
                }
                if (!filteredGroup.getItems().isEmpty()) {
                    groups.add(filteredGroup);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchPlaylistTask extends AsyncTask<String, Void, List<Group>> {
        private boolean isError = false;

        @Override
        protected List<Group> doInBackground(String... urls) {
            List<Group> fetchedGroups = new ArrayList<>();
            try {
                URL url = new URL(urls[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                String title = "";
                String streamUrl = "";
                String groupTitle = "";
                String tvgId = "";
                String logoUrl = "";
                Group currentGroup = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#EXTINF:")) {
                        String[] parts = line.split(",", 2);
                        if (parts.length > 1) {
                            title = parts[1].trim();
                            groupTitle = extractAttribute(parts[0], "group-title");
                            tvgId = extractAttribute(parts[0], "tvg-id");
                            logoUrl = extractAttribute(parts[0], "tvg-logo");
                        }
                    } else if (!line.startsWith("#")) {
                        streamUrl = line.trim();
                        if (currentGroup == null || !currentGroup.getTitle().equals(groupTitle)) {
                            currentGroup = new Group(groupTitle);
                            fetchedGroups.add(currentGroup);
                        }
                        currentGroup.addItem(new PlaylistItem(title, streamUrl, tvgId, logoUrl));
                        title = "";
                        streamUrl = "";
                        tvgId = "";
                        logoUrl = "";
                    }
                }
                reader.close();
            } catch (Exception e) {
                isError = true;
                e.printStackTrace();
            }
            return fetchedGroups;
        }

        private String extractAttribute(String input, String attributeName) {
            int start = input.indexOf(attributeName + "=\"");
            if (start != -1) {
                start += attributeName.length() + 2;
                int end = input.indexOf("\"", start);
                if (end != -1) {
                    return input.substring(start, end);
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(List<Group> result) {
            binding.progressBar.setVisibility(View.GONE);

            if (isError || result.isEmpty()) {
                binding.playerError.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                menuHandler.isErrorLoadingPlaylist.set(true);
            } else {
                binding.playerError.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                menuHandler.isErrorLoadingPlaylist.set(false);

                allGroups.clear();
                allGroups.addAll(result);
                groups.clear();
                groups.addAll(result);
                adapter.notifyDataSetChanged();

                updateClearButtonVisibility(binding.etSearch.getText().toString());
            }
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }


}