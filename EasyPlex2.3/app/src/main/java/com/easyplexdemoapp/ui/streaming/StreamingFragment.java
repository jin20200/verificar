package com.easyplexdemoapp.ui.streaming;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.FragmentStreamingBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.viewmodels.StreamingGenresViewModel;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import java.util.Locale;
import javax.inject.Inject;


public class StreamingFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    SettingsManager settingsManager;

    @Inject
    SharedPreferences preferences;

    @Inject
    SettingsRepository authRepository;



    @Inject
    MediaRepository mediaRepository;

    private StreamingGenresViewModel genresViewModel;


    @Inject
    AuthManager authManager;


    @Inject
    TokenManager tokenManager;

    FragmentStreamingBinding binding;

    StreamingGenresAdapter adapter;
    private static final  int LOADING_DURATION = 1500;
    private boolean mStream;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_streaming, container, false);


        genresViewModel = new ViewModelProvider(this, viewModelFactory).get(StreamingGenresViewModel.class);
        genresViewModel.getStreamingGenresList();
        onLoadGenres();


        if (Tools.isRTL(Locale.getDefault())){

            binding.filterBtn.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.filterBtn.setBackgroundResource(R.drawable.bg_episodes_rtl);
        }

        binding.filterBtn.setOnClickListener(v -> {

            onLoadGenres();
            binding.planetsSpinner.performClick();
        });


        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerView.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 1), true));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        mStream = false;
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar.setAlpha(1.0f);
        binding.scrollView.setVisibility(View.GONE);
        adapter = new StreamingGenresAdapter(getContext());

        return binding.getRoot();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        onAppConnected();

        binding.swipeContainer.setOnRefreshListener(() -> {

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.setAlpha(1.0f);
            binding.scrollView.setVisibility(View.GONE);
            onAppConnected();

            new Handler(Looper.getMainLooper()).postDelayed(() -> binding.swipeContainer.setRefreshing(false),2000);


        });

        // Scheme colors for animation

        binding.swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_red_light)
        );


    }

    private void onAppConnected() {

        mStream = true;
        checkAllDataLoaded();

    }



    private void onLoadGenres() {

        genresViewModel.getStreamingGenresList();
        genresViewModel.streamingDetailMutableLiveData.observe(getViewLifecycleOwner(), streaming -> {


            if (!streaming.getGenres().isEmpty()) {

                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.planetsSpinner.setItem(streaming.getGenres());
                binding.planetsSpinner.setSelection(0);
                binding.planetsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                        binding.planetsSpinner.setVisibility(View.GONE);
                        binding.filterBtn.setVisibility(View.VISIBLE);
                        Genre genre = (Genre) adapterView.getItemAtPosition(position);
                        int genreId = genre.getId();
                        String genreName = genre.getName();

                        binding.selectedGenre.setText(genreName);
                        genresViewModel.searchQuery.setValue(String.valueOf(genreId));
                        genresViewModel.getStreamGenresitemPagedList().observe(getViewLifecycleOwner(), streamingGenres -> {

                            if (streamingGenres !=null) {

                                adapter.submitList(streamingGenres);
                                binding.recyclerView.setAdapter(adapter);
                                binding.textViewSelectAnotherGenre.setVisibility(View.GONE);
                                binding.noMoviesFound.setVisibility(View.GONE);
                                binding.noResults.setVisibility(View.GONE);
                                binding.noMoviesFound.setVisibility(View.GONE);
                                binding.noResults.setVisibility(View.GONE);

                            }else {
                                binding.noMoviesFound.setVisibility(View.VISIBLE);
                                binding.noResults.setVisibility(View.VISIBLE);
                                binding.noResults.setText(String.format("No Results found for %s", genreName));
                                binding.textViewSelectAnotherGenre.setVisibility(View.VISIBLE);

                            }


                        });



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                        // Invoked when a network exception occurred talking to the server or when an unexpected exception occurred creating the request or processing the response.

                    }
                });


            }else {

                binding.noMoviesFound.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);

            }


        });




    }


    private void checkAllDataLoaded() {
        if (mStream) {

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Tools.fadeOut(binding.progressBar);
                binding.progressBar.setVisibility(View.GONE);
                binding.scrollView.setVisibility(View.VISIBLE);
            }, LOADING_DURATION);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

}
