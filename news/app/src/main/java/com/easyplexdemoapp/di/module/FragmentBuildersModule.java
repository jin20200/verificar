package com.easyplexdemoapp.di.module;

import com.easyplexdemoapp.ui.animes.EpisodesFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.main.DownloadsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.main.FinishedDownloadsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.main.QueuedDownloadsFragment;
import com.easyplexdemoapp.ui.home.HomeFragment;
import com.easyplexdemoapp.ui.library.AnimesFragment;
import com.easyplexdemoapp.ui.library.LanguagesFragment;
import com.easyplexdemoapp.ui.library.LibraryFragment;
import com.easyplexdemoapp.ui.library.LibraryStyleFragment;
import com.easyplexdemoapp.ui.library.MoviesFragment;
import com.easyplexdemoapp.ui.library.NetworksFragment;
import com.easyplexdemoapp.ui.library.NetworksFragment2;
import com.easyplexdemoapp.ui.library.SeriesFragment;
import com.easyplexdemoapp.ui.mylist.AnimesListFragment;
import com.easyplexdemoapp.ui.mylist.ListFragment;
import com.easyplexdemoapp.ui.mylist.MoviesListFragment;
import com.easyplexdemoapp.ui.mylist.SeriesListFragment;
import com.easyplexdemoapp.ui.mylist.StreamingListFragment;
import com.easyplexdemoapp.ui.search.DiscoverFragment;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.iptv.IptvPlaylistFragment;
import com.easyplexdemoapp.ui.streaming.StreamingFragment;
import com.easyplexdemoapp.ui.upcoming.UpComingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/*
 * @author Yobex.
 * */
@Module
public abstract class FragmentBuildersModule {



    @ContributesAndroidInjector
    abstract IptvPlaylistFragment contributeIptvPlaylistFragment();

    @ContributesAndroidInjector
    abstract LanguagesFragment contributeLanguagesFragment();


    @ContributesAndroidInjector
    abstract EpisodesFragment contributeEpisodesFragment();

    @ContributesAndroidInjector
    abstract FinishedDownloadsFragment contributeFinishedDownloadsFragment();


    @ContributesAndroidInjector
    abstract QueuedDownloadsFragment contributeQueuedDownloadsFragment();

    @ContributesAndroidInjector
    abstract DownloadsFragment contributeDownloadsFragment();

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract UpComingFragment contributeUpcomingFragment();

    @ContributesAndroidInjector
    abstract DiscoverFragment contributeDiscoverFragment();

    @ContributesAndroidInjector
    abstract MoviesFragment contributeMoviesFragment();


    @ContributesAndroidInjector
    abstract LibraryStyleFragment contributeLibraryStyleFragment();

    @ContributesAndroidInjector
    abstract SeriesFragment contributeSeriesFragment();

    @ContributesAndroidInjector
    abstract LibraryFragment contributeLibraryFragment();

    @ContributesAndroidInjector
    abstract MoviesListFragment contributeMyListMoviesFragment();

    @ContributesAndroidInjector
    abstract AnimesFragment contributeAnimesFragment();

    @ContributesAndroidInjector
    abstract StreamingFragment contributeLiveFragment();

    @ContributesAndroidInjector
    abstract SettingsActivity contributeSettingsFragment();

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract SeriesListFragment contributeSeriesListFragment();

    @ContributesAndroidInjector
    abstract AnimesListFragment contributeAnimesListFragment();


    @ContributesAndroidInjector
    abstract NetworksFragment contributeNetworksFragment();

    @ContributesAndroidInjector
    abstract NetworksFragment2 contributeNetworksFragment2();

    @ContributesAndroidInjector
    abstract StreamingListFragment contributeStreamingListFragment();

}
