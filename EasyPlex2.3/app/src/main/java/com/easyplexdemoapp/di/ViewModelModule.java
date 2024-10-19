package com.easyplexdemoapp.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.easyplexdemoapp.ui.viewmodels.AnimeViewModel;
import com.easyplexdemoapp.ui.viewmodels.CastersViewModel;
import com.easyplexdemoapp.ui.viewmodels.HomeViewModel;
import com.easyplexdemoapp.ui.viewmodels.GenresViewModel;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.MovieDetailViewModel;
import com.easyplexdemoapp.ui.viewmodels.MoviesListViewModel;
import com.easyplexdemoapp.ui.viewmodels.NetworksViewModel;
import com.easyplexdemoapp.ui.viewmodels.PlayerViewModel;
import com.easyplexdemoapp.ui.viewmodels.RegisterViewModel;
import com.easyplexdemoapp.ui.viewmodels.SearchViewModel;
import com.easyplexdemoapp.ui.viewmodels.SerieDetailViewModel;
import com.easyplexdemoapp.ui.viewmodels.SettingsViewModel;
import com.easyplexdemoapp.ui.viewmodels.StreamingDetailViewModel;
import com.easyplexdemoapp.ui.viewmodels.StreamingGenresViewModel;
import com.easyplexdemoapp.ui.viewmodels.UpcomingViewModel;
import com.easyplexdemoapp.ui.viewmodels.UserViewModel;
import com.easyplexdemoapp.viewmodel.MoviesViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/*
 * @author Yobex.
 * */
@Module
public abstract class ViewModelModule {



    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingViewModel.class)
    abstract ViewModel bindUpcomingViewModel(UpcomingViewModel upcomingViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel.class)
    abstract ViewModel bindMovieDetailViewModel(MovieDetailViewModel movieDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SerieDetailViewModel.class)
    abstract ViewModel bindSerieDetailViewModel(SerieDetailViewModel serieDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel.class)
    abstract ViewModel bindGenresViewModel(GenresViewModel genresViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel settingsViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(MoviesListViewModel.class)
    abstract ViewModel bindMoviesListViewModel(MoviesListViewModel moviesListViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel.class)
    abstract ViewModel bindAnimeViewModel(AnimeViewModel animeViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(StreamingDetailViewModel.class)
    abstract ViewModel bindStreamingDetailViewModel(StreamingDetailViewModel streamingDetailViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(StreamingGenresViewModel.class)
    abstract ViewModel bindStreamingGenresViewModel(StreamingGenresViewModel streamingGenresViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    abstract ViewModel bindStreamingPlayerViewModel(PlayerViewModel playerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CastersViewModel.class)
    abstract ViewModel bindCastersViewModel(CastersViewModel castersViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(NetworksViewModel.class)
    abstract ViewModel bindNetworksViewModel(NetworksViewModel networksViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(MoviesViewModelFactory factory);


}
