package com.easyplexdemoapp.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.databinding.LayoutGenresBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.viewmodels.GenresViewModel;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;


public class SeriesFragment extends Fragment implements Injectable {

    LayoutGenresBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private GenresViewModel genresViewModel;
    @Inject
    ItemAdapter adapter;
    private List<String> provinceList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_genres, container, false);

        genresViewModel = new ViewModelProvider(this, viewModelFactory).get(GenresViewModel.class);


        onLoadAllGenres();
        onLoadGenres();

        if (Tools.isRTL(Locale.getDefault())){

            binding.filterBtn.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.filterBtn.setBackgroundResource(R.drawable.bg_episodes_rtl);
            binding.filterBtnAllgenres.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.filterBtnAllgenres.setBackgroundResource(R.drawable.bg_episodes);
        }


        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
        binding.recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);


        return binding.getRoot();

    }



    private void onLoadGenres() {

        binding.filterBtn.setOnClickListener(v -> binding.planetsSpinner.performClick());

        genresViewModel.getMoviesGenresList();
        genresViewModel.movieDetailMutableLiveData.observe(getViewLifecycleOwner(), movieDetail -> {

            if (!movieDetail.getGenresPlayer().isEmpty()) {

                binding.noMoviesFound.setVisibility(View.GONE);

                binding.planetsSpinner.setItem(movieDetail.getGenresPlayer());
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
                        genresViewModel.getGenresitemPagedList("serie").observe(getViewLifecycleOwner(), genresList -> {

                            if (genresList !=null) {

                                adapter.submitList(genresList);


                                if (Objects.requireNonNull(adapter.getCurrentList()).isEmpty()) {

                                    binding.noMoviesFound.setVisibility(View.VISIBLE);
                                    binding.noResults.setText(String.format("No Results found for %s", genreName));

                                }else {

                                    binding.noMoviesFound.setVisibility(View.GONE);


                                }
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

            }


        });


    }

    private void onLoadAllGenres() {

        provinceList = new ArrayList<>();
        provinceList.add(getString(R.string.all_genres));
        provinceList.add(getString(R.string.latest_added));
        provinceList.add(getString(R.string.by_rating));
        provinceList.add(getString(R.string.by_year));
        provinceList.add(getString(R.string.by_views));


        binding.filterBtnAllgenres.setOnClickListener(v -> binding.planetsSpinnerSort.performClick());

        binding.noMoviesFound.setVisibility(View.GONE);

        binding.planetsSpinnerSort.setItem(provinceList);
        binding.planetsSpinnerSort.setSelection(0);
        binding.planetsSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                switch (position) {

                    case 0:

                        String genreName = provinceList.get(0);
                        binding.selectedGenreLeft.setText(genreName);

                        genresViewModel.seriePagedList.observe(getViewLifecycleOwner(), favoriteMovies -> adapter.submitList(favoriteMovies));


                        break;
                    case 1:

                        String serieLatest = provinceList.get(1);
                        binding.selectedGenreLeft.setText(serieLatest);

                        genresViewModel.serieLatestPagedList.observe(getViewLifecycleOwner(), favoriteMovies -> adapter.submitList(favoriteMovies));

                        break;
                    case 2:

                        String rating = provinceList.get(2);
                        binding.selectedGenreLeft.setText(rating);
                        genresViewModel.serieRatingPagedList.observe(getViewLifecycleOwner(), favoriteMovies -> adapter.submitList(favoriteMovies));

                        break;
                    case 3:


                        String year = provinceList.get(3);
                        binding.selectedGenreLeft.setText(year);
                        genresViewModel.serieYearPagedList.observe(getViewLifecycleOwner(), favoriteMovies -> adapter.submitList(favoriteMovies));

                        break;
                    case 4:

                        String serieview = provinceList.get(4);
                        binding.selectedGenreLeft.setText(serieview);
                        genresViewModel.serieViewsgPagedList.observe(getViewLifecycleOwner(), favoriteMovies -> adapter.submitList(favoriteMovies));

                        break;
                    default:
                        break;
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                // Nothting to refresh when no Item Selected

            }
        });

    }


}
