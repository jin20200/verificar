package com.easyplexdemoapp.ui.mylist;

import static com.easyplexdemoapp.util.ItemAnimation.FADE_IN;

import android.annotation.SuppressLint;
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
import com.easyplexdemoapp.ui.viewmodels.StreamingDetailViewModel;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class StreamingListFragment extends Fragment implements Injectable , DeleteFavoriteDetectListner {


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private StreamMyListdapter streamMyListdapter;


    private StreamingDetailViewModel streamingDetailViewModel;


    FragmentFavouriteMoviesBinding binding;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    SettingsManager settingsManager;



    @Inject
    AuthRepository authRepository;

    @Inject
    TokenManager tokenManager;


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
        streamingDetailViewModel = new ViewModelProvider(this, viewModelFactory).get(StreamingDetailViewModel.class);

        streamMyListdapter = new StreamMyListdapter(mediaRepository,settingsManager);

        onLoadListData();

        binding.rvMylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvMylist.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 1), true));
        binding.rvMylist.setHasFixedSize(true);

        return  binding.getRoot();

    }


    private void onLoadListData() {

        if (settingsManager.getSettings().getFavoriteonline() == 1 && tokenManager.getToken().getAccessToken() !=null) {

            onLoadSeriesListOnline();

        } else {

            onLoadSeriesListOffline();
        }
    }

    private void onLoadSeriesListOffline() {

        streamingDetailViewModel.getStreamFavorites().observe(getViewLifecycleOwner(), favoriteMovies -> {

            binding.rvMylist.setAdapter(streamMyListdapter);
            streamMyListdapter.addToContent(favoriteMovies,requireActivity());
            binding.rvMylist.setEmptyView(binding.noResults);

        });
    }

    private void onLoadSeriesListOnline() {

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
                        authRepository.getUserFavorite("streaming").observe(getViewLifecycleOwner(), genresList -> defaultListAdapter.submitList(genresList));
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.rvMylist.setAdapter(null);
        binding =null;

    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        onLoadListData();

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onMediaDeletedSuccess(boolean clicked) {
        if (clicked) {
            onLoadListData();
        }
    }

}
