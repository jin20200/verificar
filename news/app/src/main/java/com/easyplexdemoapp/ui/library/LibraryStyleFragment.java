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
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.LayoutGenresBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.viewmodels.GenresViewModel;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class LibraryStyleFragment extends Fragment implements Injectable {

    LayoutGenresBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    MediaRepository mediaRepository;
    private GenresViewModel genresViewModel;


    @Inject
    ItemAdapter adapter;
    private List<String> provinceList;
    private static final int ANIMATION_TYPE = ItemAnimation.FADE_IN;

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



    // Load Genres
    private void onLoadGenres() {

        genresViewModel.getMoviesGenresList();
        genresViewModel.movieDetailMutableLiveData.observe(getViewLifecycleOwner(), movieDetail -> {

            binding.filterBtn.setOnClickListener(v -> binding.planetsSpinner.performClick());

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
                        genresViewModel.getGenresitemPagedList("movie").observe(getViewLifecycleOwner(), genresList -> {

                            if (genresList !=null) {

                                adapter.submitList(genresList);

                            }

                        });



                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                        // Nothting to refresh when no Item Selected

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
                        String all = provinceList.get(0);
                        binding.selectedGenreLeft.setText(all);
                        genresViewModel.searchQuery.setValue("allgenres");
                        genresViewModel.getMediaLibraryPagedList().observe(getViewLifecycleOwner(), genresList -> adapter.submitList(genresList));

                        break;
                    case 1:

                        String latest = provinceList.get(1);
                        binding.selectedGenreLeft.setText(latest);
                        genresViewModel.searchQuery.setValue("latestadded");
                        genresViewModel.getMediaLibraryPagedList().observe(getViewLifecycleOwner(), genresList -> adapter.submitList(genresList));



                        break;
                    case 2:

                        String rating = provinceList.get(2);
                        binding.selectedGenreLeft.setText(rating);
                        genresViewModel.searchQuery.setValue("byrating");
                        genresViewModel.getMediaLibraryPagedList().observe(getViewLifecycleOwner(), genresList -> adapter.submitList(genresList));

                        break;
                    case 3:

                        String year = provinceList.get(3);
                        binding.selectedGenreLeft.setText(year);
                        genresViewModel.searchQuery.setValue("byyear");
                        genresViewModel.getMediaLibraryPagedList().observe(getViewLifecycleOwner(), genresList -> adapter.submitList(genresList));


                        break;
                    case 4:

                        String views = provinceList.get(4);
                        binding.selectedGenreLeft.setText(views);
                        genresViewModel.searchQuery.setValue("byviews");
                        genresViewModel.getMediaLibraryPagedList().observe(getViewLifecycleOwner(), genresList -> adapter.submitList(genresList));

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
