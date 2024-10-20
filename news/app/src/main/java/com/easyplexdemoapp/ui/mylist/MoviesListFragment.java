package com.easyplexdemoapp.ui.mylist;

import static com.easyplexdemoapp.util.ItemAnimation.FADE_IN;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.FragmentFavouriteMoviesBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.MoviesListViewModel;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MoviesListFragment extends Fragment implements Injectable , DeleteFavoriteDetectListner {


    @Inject
    ViewModelProvider.Factory viewModelFactory;


    FragmentFavouriteMoviesBinding binding;


    @Inject
    AuthRepository authRepository;

    @Inject
    MediaRepository mediaRepository;


    @Inject
    SettingsManager settingsManager;

    @Inject
    TokenManager tokenManager;

    private LoginViewModel loginViewModel;

    @Inject
    MoviesListViewModel moviesListViewModel;

    private  MoviesMyListAdapter moviesMyListAdapter;

    DefaultListAdapter defaultListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_movies, container, false);

        defaultListAdapter = new DefaultListAdapter(requireContext(), FADE_IN, settingsManager,authRepository,this);

        // ViewModel to cache, retrieve data for MyListFragment
        moviesListViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MoviesListViewModel.class);

        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        moviesMyListAdapter = new MoviesMyListAdapter(mediaRepository,settingsManager,authRepository,tokenManager);

        onLoadListData();

        binding.rvMylist.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.rvMylist.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        binding.rvMylist.setHasFixedSize(true);

        return  binding.getRoot();

    }

    private void onLoadListData() {

        if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null ) {

            onLoadMoviesListOnline();

        } else {

            onLoadMoviesListOffline();
        }
    }

    private void onLoadMoviesListOnline() {


        authRepository.getAuth()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NonNull UserAuthInfo userAuthInfo) {


                        binding.rvMylist.setAdapter(defaultListAdapter);
                        authRepository.searchQuery.setValue(String.valueOf(userAuthInfo.getId()));
                        authRepository.getUserFavorite("movies").observe(getViewLifecycleOwner(), genresList -> defaultListAdapter.submitList(genresList));
                        binding.rvMylist.setEmptyView(binding.noResults);

                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        //

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });


    }


    private void onLoadMoviesListOffline() {

        moviesListViewModel.getMoviesFavorites().observe(getViewLifecycleOwner(), favoriteMovies -> {

            moviesMyListAdapter.addToContent(favoriteMovies,requireActivity(),this);
            binding.rvMylist.setAdapter(moviesMyListAdapter);
            binding.rvMylist.setEmptyView(binding.noResults);

        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvMylist.setAdapter(null);
        binding =null;
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoadListData();
    }

    @Override
    public void onMediaDeletedSuccess(boolean clicked) {

        if (clicked) {
            onLoadListData();

        }
    }
}
