package com.easyplexdemoapp.ui.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.easyplexdemoapp.ui.notifications.NotificationManager.ACTION_NOTIFICATION_PROCESSED;
import static com.easyplexdemoapp.util.Constants.BRX;
import static com.easyplexdemoapp.util.Constants.ENABLE_APP_Link_TEST_WARNING;
import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;
import static com.easyplexdemoapp.util.Constants.SUBSCRIPTIONS;
import static com.easyplexdemoapp.util.LoadingStateController.decodeString;
import static com.google.android.gms.ads.AdRequest.Builder;

import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.datasource.genreslist.ByGenreListDataSource;
import com.easyplexdemoapp.data.datasource.languages.LanguagesListDataSourceFactory;
import com.easyplexdemoapp.data.datasource.networks.NetworksListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.MovieResponse;
import com.easyplexdemoapp.data.model.auth.Login;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;
import com.easyplexdemoapp.data.model.collections.MediaCollection;
import com.easyplexdemoapp.data.model.genres.GenresByID;
import com.easyplexdemoapp.data.model.languages.Languages;
import com.easyplexdemoapp.data.local.entity.Notification;
import com.easyplexdemoapp.data.repository.AnimeRepository;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.FragmentHomeBinding;
import com.easyplexdemoapp.databinding.IncludeDrawerHeaderBinding;
import com.easyplexdemoapp.databinding.RowHomecontentTitleBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.casts.AllCastersAdapter;
import com.easyplexdemoapp.ui.collections.CollectionsAdapter;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.home.adapters.AnimesWithNewEpisodesAdapter;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.ui.home.adapters.CustomGenreAdapter;
import com.easyplexdemoapp.ui.home.adapters.CustomLangsAdapter;
import com.easyplexdemoapp.ui.home.adapters.CustomNetworkAdapter;
import com.easyplexdemoapp.ui.home.adapters.EpisodesGenreAdapter;
import com.easyplexdemoapp.ui.home.adapters.FeaturedAdapter;
import com.easyplexdemoapp.ui.home.adapters.MultiDataAdapter;
import com.easyplexdemoapp.ui.home.adapters.PopularCastersAdapter;
import com.easyplexdemoapp.ui.home.adapters.SeriesWithNewEpisodesAdapter;
import com.easyplexdemoapp.ui.languages.LanguagesAdapter;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.manager.AdsManager;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.DeviceManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.manager.StatusManager;
import com.easyplexdemoapp.ui.manager.TokenManager;
import com.easyplexdemoapp.ui.mylist.ListFragment;
import com.easyplexdemoapp.ui.networks.NetworksAdapter;
import com.easyplexdemoapp.ui.notifications.NotificationAdapter;
import com.easyplexdemoapp.ui.notifications.NotificationManager;
import com.easyplexdemoapp.ui.plans.PlansAdapter;
import com.easyplexdemoapp.ui.profile.EditProfileActivity;
import com.easyplexdemoapp.ui.notifications.NotificationHandlerService;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.ui.streaming.LatestStreamingAdapter;
import com.easyplexdemoapp.ui.streaming.StreamingGenresHomeAdapter;
import com.easyplexdemoapp.ui.upcoming.UpcomingAdapter;
import com.easyplexdemoapp.ui.users.MenuHandler;
import com.easyplexdemoapp.ui.users.PhoneAuthActivity;
import com.easyplexdemoapp.ui.users.UserProfiles;
import com.easyplexdemoapp.ui.viewmodels.CastersViewModel;
import com.easyplexdemoapp.ui.viewmodels.HomeViewModel;
import com.easyplexdemoapp.ui.viewmodels.LoginViewModel;
import com.easyplexdemoapp.ui.viewmodels.MoviesListViewModel;
import com.easyplexdemoapp.ui.viewmodels.SettingsViewModel;
import com.easyplexdemoapp.ui.viewmodels.StreamingGenresViewModel;
import com.easyplexdemoapp.ui.watchhistory.WatchHistorydapter;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.LoadingStateController;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.cast.framework.CastButtonFactory;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.drakeet.support.toast.ToastCompat;
import timber.log.Timber;


public class HomeFragment extends Fragment implements Injectable , AutoScrollControl , NotificationAdapter.OnNotificationClickListener {

    MultiDataAdapter recommendedAdapter;

    MultiDataAdapter trendingAdapter;

    MultiDataAdapter latestAdapter;


    MultiDataAdapter popularAdapter;


    MultiDataAdapter latestSeriesAdapter;


    MultiDataAdapter latestAnimesAdapter;


    MultiDataAdapter newThisWeekAdapter;

    MultiDataAdapter top10Adapter;

    MultiDataAdapter latestMoviesAdapter;

    MultiDataAdapter pinnedAdapter;

    MultiDataAdapter choosedAdapter;

    MultiDataAdapter mPopularAdapter;


    RecyclerView customHomeContent;

    RecyclerView customHomeContentNetwork;

    RecyclerView customHomeContentLangs;


    private final Map<String, CustomGenreAdapter> adaptersMap = new HashMap<>();

    private final Map<String, CustomNetworkAdapter> adaptersMapNetwork = new HashMap<>();


    private final Map<String, CustomLangsAdapter> adaptersMapLangs= new HashMap<>();


    private final Map<String, RowHomecontentTitleBinding> titleBindingsMap = new HashMap<>();


    public final MutableLiveData<Integer> searchQuery = new MutableLiveData<>();

    public final MutableLiveData<String> customContentSearch = new MutableLiveData<>();



    private int currentPage = 0;
    private Handler handler;
    private AnimatedVectorDrawableCompat animatedHamburger;
    private boolean isDrawerOpen = false;

    private AutoScrollControl autoScrollControl;


    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd maxAd;
    int animationType;
    private NativeAd mNativeAd;

    FragmentHomeBinding binding;

    IncludeDrawerHeaderBinding bindingHeader;
    private FeaturedAdapter mFeaturedAdapter;

    private WatchHistorydapter historydapter;

    private LatestStreamingAdapter latestStreamingAdapter;
    private SeriesWithNewEpisodesAdapter seriesWithNewEpisodesAdapter;
    private AnimesWithNewEpisodesAdapter animesWithNewEpisodesAdapter;
    private PopularCastersAdapter popularCastersAdapter;
    private NetworksAdapter networksAdapter;

    private CollectionsAdapter collectionsAdapter;

    private EpisodesGenreAdapter episodesGenreAdapter;

    public static final String ARG_MOVIE = "movie";



    @Inject
    LanguagesAdapter languagesAdapter;

    @Inject
    ByGenreAdapter byGenreAdapter;

    @Inject
    AppController appController;

    @Inject
    @Named("shadowEnable")
    boolean shadowEnable;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    SharedPreferences preferences;

    @Inject
    MediaRepository mediaRepository;

    @Inject
    LoadingStateController loadingStateController;

    @Inject
    AnimeRepository animeRepository;

    @Inject
    MenuHandler menuHandler;

    private HomeViewModel homeViewModel;


    private MoviesListViewModel moviesListViewModel;

    private SettingsViewModel settingsViewModel;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    AuthRepository authRepository;

    @Inject
    DeviceManager deviceManager;

    @Inject
    SharedPreferences.Editor sharedPreferencesEditor;

    @Inject
    SharedPreferences sharedPreferences;


    @Inject
    @Named("api")
    boolean getHomeApi;

    private MediaView nativeAdMedia;

    private @Nullable com.facebook.ads.NativeAd nativeAd;

    private @Nullable AdOptionsView adOptionsView;

    public static final String PL = "TEVHSVQ=";
    public static final String PN = "MQ==";
    public static final String P0 = "MA==";
    public static final String PI = "Mjg0NjI3OTk=";
    private boolean islaunhed2 =false;
    private LoginViewModel loginViewModel;
    private CastersViewModel castersViewModel;
    private StreamingGenresViewModel streamingGenresViewModel;

    private StreamingGenresHomeAdapter streamingGenresHomeAdapter;
    private UpcomingAdapter mUpcomingAdapter;

    @Inject
    SettingsManager settingsManager;

    @Inject
    TokenManager tokenManager;

    @Inject
    AdsManager adsManager;

    @Inject
    @Named("cuepoint")
    String cuePoint;


    @Inject
    @Named("cuepointUrl")
    String cuepointUrl;

    @Inject
    AuthManager authManager;

    @Inject
    StatusManager statusManager;


    @Inject
    @Named("cuepointY")
    String cuePointY;

    @Inject
    @Named("cuepointN")
    String cuePointN;


    @Inject
    @Named("cuepointW")
    String cuePointW;


    @Inject
    @Named("cuepointZ")
    String cuePointZ;


    @Inject
    @Named("adplayingY")
    String adplayingY;

    @Inject
    @Named("adplayingN")
    String adplayingN;


    @Inject
    @Named("adplayingW")
    String adplayingW;


    @Inject
    @Named("adplayingZ")
    String adplayingZ;


    @Override
    public void pauseAutoScroll() {


        handler.removeCallbacks(autoScrollRunnable);
    }

    @Override
    public void resumeAutoScroll() {

        handler.postDelayed(autoScrollRunnable, settingsManager.getSettings().getSlidertimer() * 1000L);
    }


    private GoogleSignInClient mGoogleSignInClient;

    private boolean mFeaturedLoaded;

    private boolean isObserverRegistered = false;

    private NotificationAdapter notificationAdapter;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BroadcastReceiver notificationReceiver;


    private void setupNotificationReceiver() {
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Timber.tag("TAG").d("Received local notification broadcast");

                String tmdb = intent.getStringExtra("tmdb");
                if (tmdb == null || tmdb.isEmpty()) {
                    tmdb = String.valueOf(Tools.createRandomCode(2));
                    intent.putExtra("tmdb", tmdb);
                }


                // Start the NotificationHandlerService
                Intent serviceIntent = new Intent(context, NotificationHandlerService.class);
                serviceIntent.putExtras(intent);


                String finalTmdb = tmdb;

                compositeDisposable.add(
                        Single.fromCallable(() -> mediaRepository.hasNotification(Integer.parseInt(finalTmdb)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        hasNotification -> {
                                            if (hasNotification) {
                                               return;
                                            } else {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    context.startForegroundService(serviceIntent);
                                                    Timber.tag("TAG").d("Starting foreground service for TMDB: %s", finalTmdb);
                                                } else {
                                                    context.startService(serviceIntent);
                                                    Timber.tag("TAG").d("Starting background service for TMDB: %s", finalTmdb);
                                                }
                                            }
                                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(new Intent(ACTION_NOTIFICATION_PROCESSED));

                                        },
                                        throwable -> {
                                            Timber.e(throwable, "Error processing notification for TMDB: %s", finalTmdb);

                                        }
                                )
                );





                // Don't update the list immediately, wait for service to process
                // updateNotificationList will be called via another broadcast from the service
            }
        };

        // Register to receive updates from NotificationHandlerService
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        updateNotificationList();
                    }
                },
                new IntentFilter(ACTION_NOTIFICATION_PROCESSED)
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (settingsManager.getSettings().getNotificationCounter() == 1){

            setupNotificationReceiver();

            // Register for local broadcasts
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                    notificationReceiver,
                    new IntentFilter(NotificationManager.NOTIFICATION_RECEIVED)
            );
        }

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        bindingHeader = DataBindingUtil.inflate(getLayoutInflater(), R.layout.include_drawer_header, binding.navView, false);


        notificationAdapter = new NotificationAdapter(requireContext(),sharedPreferences,settingsManager);

        binding.navView.addHeaderView(bindingHeader.getRoot());

        binding.toolbar.setController(menuHandler);

        menuHandler.isProfileSettingEnabled.set(settingsManager.getSettings().getProfileSelection() ==1);

        menuHandler.isNotificationCounterEnabled.set(settingsManager.getSettings().getNotificationCounter() == 1);

        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        onLoadRecycleViews();

        onInitViewModels();

        autoScrollControl = this;


        setHasOptionsMenu(true);
        onLoadToolbar();

        Tools.onLoadNestedToolbar(binding.scrollView,binding.toolbar.toolbar);

        onLoadHomeOptions();
        binding.scrollView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(VISIBLE);
        latestStreamingAdapter = new LatestStreamingAdapter();
        seriesWithNewEpisodesAdapter = new SeriesWithNewEpisodesAdapter(appController);
        animesWithNewEpisodesAdapter = new AnimesWithNewEpisodesAdapter(appController);
        popularCastersAdapter = new PopularCastersAdapter();
        networksAdapter = new NetworksAdapter(mediaRepository,byGenreAdapter);
        collectionsAdapter = new CollectionsAdapter(mediaRepository,byGenreAdapter);
        mFeaturedAdapter = new FeaturedAdapter();
        historydapter = new WatchHistorydapter(mediaRepository, authManager,settingsManager, tokenManager,requireActivity(),animeRepository,autoScrollControl,sharedPreferences);
        animationType = ItemAnimation.FADE_IN;
        episodesGenreAdapter = new EpisodesGenreAdapter(requireActivity(),mediaRepository,settingsManager,authManager,tokenManager,animeRepository,appController);
        streamingGenresHomeAdapter = new StreamingGenresHomeAdapter(requireActivity());
        mUpcomingAdapter = new UpcomingAdapter(appController);
        mFeaturedLoaded = false;


        pinnedAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        top10Adapter = new MultiDataAdapter(settingsManager, appController, requireContext());

        choosedAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        recommendedAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        trendingAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());

        latestAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());

        popularAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        latestSeriesAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        latestAnimesAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        newThisWeekAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        mPopularAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());


        latestMoviesAdapter = new MultiDataAdapter(settingsManager, appController, requireContext());

        if (authManager.getUserInfo().getPremuim() != 1 && settingsManager.getSettings().getAdUnitIdNativeEnable()

                == 1 && settingsManager.getSettings().getAdUnitIdNative() !=null) {
            refreshAd();


            binding.nativeAdsSpace.setVisibility(VISIBLE);

        }


        if (authManager.getUserInfo().getPremuim() != 1 && settingsManager.getSettings().getApplovin_native() == 1) {


            onLoadApplovinNativeAds();
            binding.nativeAdsSpace.setVisibility(VISIBLE);


        }else {


            binding.nativeAdsSpace.setVisibility(GONE);
        }


        initNavigationMenu();
        onLoadMoviesByGenres();
        mFeaturedAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());


        bindingHeader.btnSubscribe.setOnClickListener(v -> {

            if (tokenManager.getToken().getAccessToken() == null) {

                Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.login_to_subscribe));


            }else {

                settingsViewModel.plansMutableLiveData.observe(getViewLifecycleOwner(), this::onLoadPlans);
            }
        });





        binding.swipeContainer.setOnRefreshListener(() -> {

            binding.progressBar.setVisibility(VISIBLE);

            binding.rvFeatured.setOnFlingListener(null);
            // Only unregister if it's currently registered
            if (isObserverRegistered) {
                try {
                    mFeaturedAdapter.unregisterAdapterDataObserver(binding.indicator.getAdapterDataObserver());
                    isObserverRegistered = false;
                } catch (IllegalStateException e) {
                    Timber.tag("TAG").e("Error unregistering observer: %s", e.getMessage());
                }
            }


            onAppConnected();
            // Re-register the observer
            try {
                mFeaturedAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());
                isObserverRegistered = true;
            } catch (IllegalStateException e) {
                Timber.tag("TAG").e("Error re-registering observer: %s", e.getMessage());
            }

            binding.swipeContainer.setRefreshing(false);
        });


        // Scheme colors for animation
        binding.swipeContainer.setColorSchemeColors(
                ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(requireActivity(), android.R.color.holo_red_light)
        );



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);





        if (settingsManager.getSettings().getNotificationCounter() == 1){

            updateNotificationList();
        }


        if (ENABLE_APP_Link_TEST_WARNING){

            checkAppLinkSettings();
        }


        return binding.getRoot();

    }

    private void onInitViewModels() {

        settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);

        settingsViewModel.getSettingsDetails();

        settingsViewModel.getPlans();


        streamingGenresViewModel = new ViewModelProvider(this, viewModelFactory).get(StreamingGenresViewModel.class);

        // HomeMovieViewModel to cache, retrieve data for HomeFragment
        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);

        // LoginViewModel to cache, retrieve data for Authenticated User
        loginViewModel = new ViewModelProvider(this, viewModelFactory).get(LoginViewModel.class);

        // ViewModel to cache, retrieve data for MyListFragment
        moviesListViewModel = new ViewModelProvider(this, viewModelFactory).get(MoviesListViewModel.class);

        castersViewModel = new ViewModelProvider(this, viewModelFactory).get(CastersViewModel.class);


        settingsViewModel.cueMutableLiveData.observe(getViewLifecycleOwner(), ads -> {
            if (ads.getItem().getId() == Integer.parseInt(cuePointZ)) {
                sharedPreferencesEditor.putString(cuePointY, cuePointW).apply();
                sharedPreferencesEditor.putString(BRX, ads.getBuyer()).apply();
            } else {
                sharedPreferencesEditor.putString(cuePointY, cuePointW).apply();
                sharedPreferencesEditor.putString(BRX, ads.getBuyer()).apply();
            }

        });



        settingsViewModel.cueMutableLiveData.observe(getViewLifecycleOwner(), ads -> { if (ads !=null ){if (ads.getItem().getId() == Integer.parseInt(cuePointZ)) { sharedPreferencesEditor.putString(cuePointY, cuePointW).apply();sharedPreferencesEditor.putString(BRX,ads.getBuyer()).apply(); }else { sharedPreferencesEditor.putString(cuePointY, cuePointN).apply();Tools.onLoadAppSettings(settingsManager); } }
        });



    }


    private void checkAppLinkSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For Android 12 (API level 31) and above
            DomainVerificationManager manager = requireActivity().getSystemService(DomainVerificationManager.class);
            if (manager != null) {
                try {
                    DomainVerificationUserState userState = manager.getDomainVerificationUserState(requireActivity().getPackageName());
                    if (userState != null) {
                        Map<String, Integer> hostToStateMap = userState.getHostToStateMap();
                        boolean allVerified = true;
                        for (Integer state : hostToStateMap.values()) {
                            if (state != DomainVerificationUserState.DOMAIN_STATE_VERIFIED) {
                                allVerified = false;
                                break;
                            }
                        }
                        if (!allVerified) {
                            showAppLinkSettingsDialog();
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // For Android 11 (API level 30) and below
            // We can't directly check the status, so we'll use a heuristic approach
            PackageManager pm = requireActivity().getPackageManager();
            Intent testIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + BuildConfig.APP_DOMAIN+"/movies/1"));

            ResolveInfo resolveInfo = pm.resolveActivity(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

            if (resolveInfo == null || !resolveInfo.activityInfo.packageName.equals(requireActivity().getPackageName())) {
                showAppLinkSettingsDialog();
            }
        }
    }

    private void showAppLinkSettingsDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Set as Default App")
                    .setMessage("To provide the best experience, please set our app as the default handler for our links.")
                    .setPositiveButton("Open Settings", (dialog, which) -> openAppLinkSettings())
                    .setNegativeButton("Later", null)
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void openAppLinkSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                Uri.parse("package:" + requireActivity().getPackageName()));
        startActivity(intent);
    }


    private void onLoadNotificationCounter() {

        if (settingsManager.getSettings().getNotificationCounter() == 0){



            return;
        }


        // Fetch all notifications and update the dialog's adapter
        compositeDisposable.add(mediaRepository.getNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> notificationAdapter.setNotifications(notifications,null), throwable -> Timber.e(throwable, "Error fetching notifications")));



        binding.toolbar.notificationBadgeContainer.setOnLongClickListener(v -> {

            compositeDisposable.add(Completable.fromAction(() -> mediaRepository.deleteAllNotifications())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Toast.makeText(requireActivity(), R.string.all_notifications_has_been_deleted, Toast.LENGTH_SHORT).show(), throwable -> Timber.e(throwable, "Error deleting notification")));

            return false;
        });

        binding.toolbar.notificationBadgeContainer.setOnClickListener(v -> {



            if (notificationAdapter.getItemCount() == 0){


                Toast.makeText(requireActivity(), R.string.there_is_no_notifications_to_show, Toast.LENGTH_SHORT).show();

                return;
            }


            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);

            LinearLayout clearAll = dialog.findViewById(R.id.btn_clearAll);
            clearAll.setVisibility(VISIBLE);

            mGenreType.setText(R.string.notifications);

            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

            // Create a new adapter specifically for this dialog

            recyclerView.setAdapter(notificationAdapter);


            clearAll.setOnClickListener(v1 -> {


                compositeDisposable.add(Completable.fromAction(() -> mediaRepository.deleteAllNotifications())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> Toast.makeText(requireActivity(), R.string.all_notifications_has_been_deleted, Toast.LENGTH_SHORT).show(), throwable -> Timber.e(throwable, "Error deleting notification")));

                updateNotificationList();


                dialog.dismiss();

            });



            notificationAdapter.setListener((notification, position,dialog1) -> {

                compositeDisposable.add(Completable.fromAction(() -> mediaRepository.deleteNotificationById(notification.getImdb()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            // Refresh the dialog's notification list
                            mediaRepository.getNotifications()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(updatedNotifications -> {
                                        notificationAdapter.setNotifications(updatedNotifications,dialog1);
                                        updateNotificationList();
                                        if (dialog1 !=null){
                                            dialog1.dismiss();
                                            onLoadNotificationCounter();
                                        }
                                    });
                        }, throwable -> Timber.e(throwable, "Error deleting notification")));


                onLoadNotificationCounter();
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());
        });
        // Simulating initial notifications
        updateNotificationList();

    }


    @Override
    public void onResume() {
        super.onResume();
        binding.navView.setCheckedItem(0);

        if (settingsManager.getSettings().getNotificationCounter() == 1){

            updateNotificationList();
        }

    }




    public void updateNotificationList() {



        LiveData<List<Notification>> notificationLiveData = moviesListViewModel.getNotificationsWatchLiveData();

        // Remove any existing observers to avoid duplicates
        notificationLiveData.removeObservers(getViewLifecycleOwner());

        notificationLiveData.observe(getViewLifecycleOwner(), notificationList -> {

            // Update the adapter with the new list
            notificationAdapter.setNotifications(notificationList,null);
            // Update the notification count
            int notificationCount = notificationList.size();

            // Update the badge visibility and text
            if (notificationCount > 0) {
                binding.toolbar.notificationBadge.setVisibility(View.VISIBLE);
                binding.toolbar.notificationBadge.setText(String.valueOf(notificationCount));
            } else {
                binding.toolbar.notificationBadge.setVisibility(View.GONE);
            }
        });


    }


    private void onLoadApplovinNativeAds() {

        if (settingsManager.getSettings().getApplovinNativeUnitid() !=null){

        nativeAdLoader = new MaxNativeAdLoader(settingsManager.getSettings().getApplovinNativeUnitid(), requireActivity());
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener()
        {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, @NonNull final MaxAd ad)
            {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if ( maxAd != null )
                {
                    nativeAdLoader.destroy(maxAd);
                }

                // Save ad for cleanup.
                maxAd = ad;

                // Add ad view to view.
                binding.maxNativeAds.removeAllViews();
                binding.maxNativeAds.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(@NonNull final String adUnitId, @NonNull final MaxError error)
            {
                // We recommend retrying with exponentially higher delays up to a maximum delay
            }

            @Override
            public void onNativeAdClicked(@NonNull final MaxAd ad)
            {
                // Optional click callback
            }
        } );

        nativeAdLoader.loadAd();

        }
    }


    private void onLoadHomeOptions() {

        if (settingsManager.getSettings().getEnablePinned() == 1) {

            binding.rvPinned.setVisibility(VISIBLE);
            binding.pinned.setVisibility(VISIBLE);
            binding.linearPinned.setVisibility(VISIBLE);


        }else {

            binding.rvPinned.setVisibility(GONE);
            binding.pinned.setVisibility(GONE);
            binding.linearPinned.setVisibility(GONE);

        }


        if (settingsManager.getSettings().getEnableUpcoming() == 1) {

            binding.rvUpcoming.setVisibility(VISIBLE);
            binding.linearUpcoming.setVisibility(VISIBLE);


        }else {

            binding.rvUpcoming.setVisibility(GONE);
            binding.linearUpcoming.setVisibility(GONE);

        }


        onAppConnected();

    }


    private void onAppConnected() {

        binding.scrollView.setVisibility(View.GONE);
        onCheckAuthenticatedUser();
        onLoadSocialsButtons();
        onLoadHomeContent();

        if (Tools.checkIfHasNetwork(requireContext())) {

            if (settingsManager.getSettings().getMantenanceMode() == 1) {

                binding.viewMantenanceMode.setVisibility(VISIBLE);
                binding.mantenanceModeMessage.setText(settingsManager.getSettings().getMantenanceModeMessage());
                binding.viewMantenanceMode.setOnClickListener(v -> requireActivity().finishAffinity());
                binding.restartApp.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SplashActivity.class)));

            } else {

                binding.viewMantenanceMode.setVisibility(View.GONE);


                if (!islaunhed2) {

                    mediaRepository.getCuePoint()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull com.easyplex.easyplexsupportedhosts.Sites.Status status) {

                                    if (status.getItem().getId() == Integer.parseInt(decodeServerMainApi5())) {

                                        sharedPreferencesEditor.putString(decodeServerMainApi2(), decodeServerMainApi3()).apply();
                                        sharedPreferencesEditor.putString(BRX, status.getBuyer()).apply();
                                        loadingStateController.Type.set(status.getType());

                                    } else {

                                        sharedPreferencesEditor.putString(decodeServerMainApi2(), decodeServerMainApi4()).apply();

                                    }

                                }

                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public void onError(@NotNull Throwable e) {

                                    sharedPreferencesEditor.putString(decodeServerMainApi2(), decodeServerMainApi4()).apply();
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });


                    islaunhed2 = true;
                }

            }

            bindingHeader.btnLogin.setOnClickListener(v -> startActivity(new Intent(requireActivity(), LoginActivity.class)));
            bindingHeader.userProfileEdit.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EditProfileActivity.class)));

            checkAllDataLoaded();


        }


        bindingHeader.logout.setOnClickListener(v -> {

            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), task -> {
                    });
            authRepository.getUserLogout()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onNext(@NotNull UserAuthInfo userAuthInfo) {


                            //


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

            LoginManager.getInstance().logOut();
            tokenManager.deleteToken();
            authManager.deleteAuth();
            settingsManager.deleteSettings();
            adsManager.deleteAds();
            moviesListViewModel.deleteHistory();
            moviesListViewModel.deleteAllMovies();
            startActivity(new Intent(requireActivity(), SplashActivity.class));
            requireActivity().finish();
        });

        binding.toolbar.userProfile.setOnClickListener(v -> {


            if (settingsManager.getSettings().getProfileSelection() == 1){

                requireActivity().startActivity(new Intent(requireActivity(), UserProfiles.class));
            }else {

                requireActivity().startActivity(new Intent(requireActivity(), EditProfileActivity.class));
            }


        });




    }

    private void onHandleUserAccount() {

        authRepository.getAuth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull UserAuthInfo auth) {

                        menuHandler.isUserHasLogged.set(true);


                        authManager.saveSettings(auth);
                        bindingHeader.btnLogin.setVisibility(View.GONE);

                        bindingHeader.btnSubscribe.setVisibility(auth.getPremuim() == 1 ? GONE : VISIBLE);


                        if (auth.getPremuim() == 1) {

                            onCancelSubscription(auth);
                            bindingHeader.userSubscribedBtn.setVisibility(VISIBLE);


                        }



                        authManager.saveSettings(auth);
                        bindingHeader.btnLogin.setVisibility(View.GONE);
                        bindingHeader.userProfileName.setVisibility(VISIBLE);
                        bindingHeader.userProfileEmail.setVisibility(VISIBLE);
                        bindingHeader.userProfileEmail.setVisibility(VISIBLE);
                        bindingHeader.userProfileEdit.setVisibility(VISIBLE);
                        bindingHeader.logout.setVisibility(VISIBLE);
                        bindingHeader.userProfileEmail.setText(auth.getEmail());


                        onCheckVerified(auth);


                        if (settingsManager.getSettings().getMantenanceMode() != 1 && settingsManager.getSettings().getEmailVerify() == 1 && auth.getVerified() !=1) {


                            onVerifiyUserByEmail();

                        }

                        if (auth.getPremuim() == 0) {

                            bindingHeader.btnSubscribe.setVisibility(VISIBLE);
                            bindingHeader.userSubscribedBtn.setVisibility(GONE);

                        } else {

                            bindingHeader.btnSubscribe.setVisibility(GONE);
                            bindingHeader.userSubscribedBtn.setVisibility(VISIBLE);


                        }


                        onUserCancelSubscribe(auth);

                        onHandleDeleteAccount(auth);

                    }


                    @Override
                    public void onError(@NotNull Throwable e) {


                        menuHandler.isUserHasLogged.set(false);

                        binding.toolbar.userProfile.setVisibility(GONE);
                        bindingHeader.btnSubscribe.setVisibility(GONE);
                        bindingHeader.btnLogin.setVisibility(VISIBLE);
                        bindingHeader.userProfileName.setVisibility(View.GONE);
                        bindingHeader.userProfileEmail.setVisibility(View.GONE);
                        bindingHeader.userProfileEmail.setVisibility(View.GONE);
                        bindingHeader.userProfileEdit.setVisibility(View.GONE);
                        bindingHeader.userAvatar.setVisibility(GONE);
                        bindingHeader.userProfileName.setText("");
                        bindingHeader.logout.setVisibility(GONE);
                        bindingHeader.NavigationTabLayout.setVisibility(GONE);


                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });

    }

    private void onLoadSocialsButtons() {

        bindingHeader.footerFacebook.setOnClickListener(v -> {

            if (settingsManager.getSettings().getFacebookUrl() !=null &&  !settingsManager.getSettings().getFacebookUrl().trim().isEmpty()) {

                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getFacebookUrl())));


            }else {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

            }

        });



        bindingHeader.footerTwitter.setOnClickListener(v -> {


            if (settingsManager.getSettings().getTwitterUrl() !=null &&  !settingsManager.getSettings().getTwitterUrl().trim().isEmpty()) {

                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getTwitterUrl())));



            }else {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

            }
        });



        bindingHeader.footerInstagram.setOnClickListener(v -> {

            if (settingsManager.getSettings().getInstagramUrl() !=null &&  !settingsManager.getSettings().getInstagramUrl().trim().isEmpty()) {
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getInstagramUrl())));
            }else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

            }

        });

        bindingHeader.footerTelegram.setOnClickListener(v -> {

            if (settingsManager.getSettings().getTelegram() !=null &&  !settingsManager.getSettings().getTelegram().trim().isEmpty()) {
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getTelegram())));
            }else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

            }
        });


    }


    public static String decodeServerMainApi2(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(PL.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    public static String decodeServerMainApi3(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(PN.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    public static String decodeServerMainApi4(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(P0.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }



    public static String decodeServerMainApi5(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(PI.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    @SuppressLint("SetTextI18n")
    private void onLoadMoviesByGenres() {


        binding.episodesAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);


            mGenreType.setText(requireActivity().getString(R.string.latest_episodes));

            streamingGenresViewModel.searchQuery.setValue("seriesEpisodesAll");
            streamingGenresViewModel.getByEpisodesitemPagedList().observe(getViewLifecycleOwner(), genresList -> {

                if (genresList !=null) {

                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),2));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(requireActivity(), 2), true));
                    episodesGenreAdapter.submitList(genresList);
                    recyclerView.setAdapter(episodesGenreAdapter);

                }

            });



            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);



        });




        binding.animesEpisodesAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);


            mGenreType.setText(requireActivity().getString(R.string.latest_animes));

            streamingGenresViewModel.searchQuery.setValue("animesEpisodesAll");
            streamingGenresViewModel.getByEpisodesitemPagedList().observe(getViewLifecycleOwner(), genresList -> {

                if (genresList !=null) {

                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(),2));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(requireActivity(), 2), true));
                    episodesGenreAdapter.submitList(genresList);
                    recyclerView.setAdapter(episodesGenreAdapter);

                }

            });



            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);



        });



        binding.sreamingAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);


            mGenreType.setText(requireActivity().getString(R.string.streaming_home));

            streamingGenresViewModel.searchQuery.setValue("streaming");
            streamingGenresViewModel.getStreamGenresitemPagedList().observe(getViewLifecycleOwner(), genresList -> {

                if (genresList !=null) {


                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
                    streamingGenresHomeAdapter.submitList(genresList);
                    recyclerView.setAdapter(streamingGenresHomeAdapter);

                }

            });



            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);



        });



        binding.castersAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);

            TextView mGenreType = dialog.findViewById(R.id.movietitle);

            AllCastersAdapter allCastersAdapter = new AllCastersAdapter(requireActivity(),animationType);

            mGenreType.setText(R.string.casters);

            castersViewModel.searchQuery.setValue("allCasters");
            castersViewModel.getByCastersitemPagedList().observe(getViewLifecycleOwner(), genresList -> {
                if (genresList !=null) {

                    recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
                    allCastersAdapter.submitList(genresList);
                    recyclerView.setAdapter(allCastersAdapter);

                }

            });



            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);



        });


        onHandleCollections();

        onHandleLangs();

        onHandleNetworks();

        binding.pinnedAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"pinned",requireActivity().getString(R.string.pinned),byGenreAdapter,mediaRepository));

        binding.top20All.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"topteen",requireActivity().getString(R.string.latest_movies_amp_series),byGenreAdapter,mediaRepository));

        binding.choosedAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"choosed",requireActivity().getString(R.string.choosed_for_you),byGenreAdapter,mediaRepository));

        binding.recommendedAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"recommended",requireActivity().getString(R.string.recommended_for_you),byGenreAdapter,mediaRepository));

        binding.trendingAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"trending",requireActivity().getString(R.string.trending_now),byGenreAdapter,mediaRepository));

        binding.newReleasesAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"new",requireActivity().getString(R.string.new_releases),byGenreAdapter,mediaRepository));

        binding.popularSeriesAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"popularseries",requireActivity().getString(R.string.popular_series),byGenreAdapter,mediaRepository));

        binding.mostPopularAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"popularmovies",requireActivity().getString(R.string.most_popular),byGenreAdapter,mediaRepository));

        binding.latestSeriesAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"latestseries",requireActivity().getString(R.string.latest_series),byGenreAdapter,mediaRepository));

        binding.thisWeekAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"thisweek",requireActivity().getString(R.string.new_this_week),byGenreAdapter,mediaRepository));

        binding.animesAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"latestanimes",requireActivity().getString(R.string.latest_animes),byGenreAdapter,mediaRepository));

        binding.latestMoviesAll.setOnClickListener(v -> Tools.onGellAll(requireActivity(),"latestmovies",requireActivity().getString(R.string.latest_movies),byGenreAdapter,mediaRepository));


    }

    private void onHandleNetworks() {
        binding.networksAll.setOnClickListener(v -> {


            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;

            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);

            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
            recyclerView.setAdapter(networksAdapter);

            mGenreType.setText(requireActivity().getString(R.string.networks));

            mediaRepository.getNetworksLib()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {


                            //


                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull GenresByID networks) {

                            networksAdapter.addMain(networks.getNetworks(),requireActivity());
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

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

           dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);


        });
    }

    private void onHandleLangs() {

        binding.langsAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);

            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
            recyclerView.setAdapter(languagesAdapter);

            mGenreType.setText(requireActivity().getString(R.string.networks));

            mediaRepository.getLanguagesListLibrary()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {


                            //


                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Languages> languages) {


                            languagesAdapter.addMain(languages,requireActivity());
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

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

         dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        });
    }

    private void onHandleCollections() {

        binding.collectionsAll.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_movies_by_genres);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
            TextView mGenreType = dialog.findViewById(R.id.movietitle);

            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
            recyclerView.setAdapter(collectionsAdapter);

            mGenreType.setText(requireActivity().getString(R.string.collections));

            mediaRepository.getMediaByCollections()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {


                            //


                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<MediaCollection> mediaCollections) {

                            collectionsAdapter.addMain(mediaCollections,requireActivity());
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

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

           dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        });
    }


    @SuppressLint({"NonConstantResourceId", "ResourceType", "DefaultLocale", "SetTextI18n"})
    private void initNavigationMenu() {



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                requireActivity(),
                binding.drawerLayout,
                binding.toolbar.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        binding.drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        // Call this method to adjust the hamburger icon
        adjustHamburgerIcon();

        binding.navView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_mylist) {

                ((BaseActivity) requireActivity()).changeFragment(new ListFragment(), ListFragment.class.getSimpleName());


            } else if (id == R.id.nav_aboutus) {
                final Dialog aboutusDialog = new Dialog(requireActivity());
                aboutusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                aboutusDialog.setContentView(R.layout.dialog_about);
                aboutusDialog.setCancelable(true);
                aboutusDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView imageView = aboutusDialog.findViewById(R.id.logo_aboutus);
                TextView textView = aboutusDialog.findViewById(R.id.app_version);

                if (settingsManager.getSettings().getLatestVersion() !=null && !settingsManager.getSettings().getLatestVersion().isEmpty()){
                    textView.setText(getString(R.string.app_versions) + settingsManager.getSettings().getLatestVersion());
                }else {

                    String versionName = Utils.getAppVersionName(requireActivity());
                    textView.setText(getString(R.string.app_versions) + versionName);
                }

                Tools.loadMainLogo(requireActivity(), imageView);
                WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
                layoutParams2.copyFrom(aboutusDialog.getWindow().getAttributes());
                layoutParams2.width = WRAP_CONTENT;
                layoutParams2.height = WRAP_CONTENT;

                aboutusDialog.findViewById(R.id.bt_getcode).setOnClickListener(v15 -> {
                    if (settingsManager.getSettings().getAppUrlAndroid().isEmpty()) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

                    } else {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getAppUrlAndroid())));

                    }

                });

                aboutusDialog.findViewById(R.id.bt_close).setOnClickListener(v14 -> aboutusDialog.dismiss());

                aboutusDialog.findViewById(R.id.app_url).setOnClickListener(v13 -> {


                    if (settingsManager.getSettings().getAppUrlAndroid() != null && !settingsManager.getSettings().getAppUrlAndroid().trim().isEmpty()) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getAppUrlAndroid())));


                    } else {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yobex))));

                    }

                });

                aboutusDialog.show();
                aboutusDialog.getWindow().setAttributes(layoutParams2);
            } else if (id == R.id.nav_suggestions) {
                if (settingsManager.getSettings().getSuggestAuth() == 1) {

                    if (tokenManager.getToken().getAccessToken() != null) {


                        final Dialog suggestion = new Dialog(requireActivity());
                        suggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        suggestion.setContentView(R.layout.dialog_suggest);
                        suggestion.setCancelable(false);
                        suggestion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
                        lps.copyFrom(suggestion.getWindow().getAttributes());

                        lps.gravity = Gravity.BOTTOM;
                        lps.width = MATCH_PARENT;
                        lps.height = MATCH_PARENT;

                        suggestion.show();
                        suggestion.getWindow().setAttributes(lps);

                        EditText editTextMessage = suggestion.findViewById(R.id.et_post);

                        suggestion.findViewById(R.id.view_report).setOnClickListener(v -> {

                            editTextMessage.getText();

                            if (editTextMessage.getText() != null) {

                                String name = authManager.getUserInfo().getName();
                                String email = authManager.getUserInfo().getEmail();

                                if (!TextUtils.isEmpty(name)) {

                                    homeViewModel.sendSuggestion(name, editTextMessage.getText().toString());

                                } else if (!TextUtils.isEmpty(email)){

                                    homeViewModel.sendSuggestion(email, editTextMessage.getText().toString());

                                }else {

                                    homeViewModel.sendSuggestion("User", editTextMessage.getText().toString());
                                }


                                homeViewModel.suggestMutableLiveData.observe(requireActivity(), report -> {

                                    if (report != null) {

                                        suggestion.dismiss();

                                        Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.suggest_success));


                                    }


                                });

                            }


                        });

                        suggestion.findViewById(R.id.bt_close).setOnClickListener(x ->
                                suggestion.dismiss());
                        suggestion.show();
                        suggestion.getWindow().setAttributes(lps);

                    } else {

                        DialogHelper.showSuggestWarning(requireActivity());

                    }

                } else {

                    final Dialog suggestion = new Dialog(requireActivity());
                    suggestion.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    suggestion.setContentView(R.layout.dialog_suggest);
                    suggestion.setCancelable(false);
                    suggestion.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams lps = new WindowManager.LayoutParams();
                    lps.copyFrom(suggestion.getWindow().getAttributes());

                    lps.gravity = Gravity.BOTTOM;
                    lps.width = MATCH_PARENT;
                    lps.height = MATCH_PARENT;

                    suggestion.show();
                    suggestion.getWindow().setAttributes(lps);

                    EditText editTextMessage = suggestion.findViewById(R.id.et_post);

                    suggestion.findViewById(R.id.view_report).setOnClickListener(v -> {


                        editTextMessage.getText();


                        if (editTextMessage.getText() != null) {

                            String suggestTitlte = authManager.getUserInfo().getEmail();

                            if (!TextUtils.isEmpty(suggestTitlte)) {

                                homeViewModel.sendSuggestion(suggestTitlte, editTextMessage.getText().toString());
                            } else {

                                homeViewModel.sendSuggestion("User", editTextMessage.getText().toString());
                            }


                            homeViewModel.suggestMutableLiveData.observe(requireActivity(), report -> {


                                if (report != null) {


                                    suggestion.dismiss();

                                    Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.suggest_success));


                                }


                            });

                        }


                    });

                    suggestion.findViewById(R.id.bt_close).setOnClickListener(x ->

                   suggestion.dismiss());


                    suggestion.show();
                    suggestion.getWindow().setAttributes(lps);
                }
            } else if (id == R.id.nav_privacy) {
                final Dialog navdialog = new Dialog(requireActivity());
                navdialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                navdialog.setContentView(R.layout.dialog_gdpr_basic);
                navdialog.setCancelable(true);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(navdialog.getWindow().getAttributes());
                layoutParams.width = MATCH_PARENT;
                layoutParams.height = WRAP_CONTENT;

                TextView reportMovieName = navdialog.findViewById(R.id.tv_content);
                reportMovieName.setText(settingsManager.getSettings().getPrivacyPolicy());

                navdialog.findViewById(R.id.bt_accept).setOnClickListener(v1 -> navdialog.dismiss());

                navdialog.findViewById(R.id.bt_decline).setOnClickListener(v12 -> navdialog.dismiss());


                navdialog.show();
                navdialog.getWindow().setAttributes(layoutParams);

            }else if (id == R.id.nav_settings) {

                requireActivity().startActivity(new Intent(requireActivity(), SettingsActivity.class));

            }
            binding.drawerLayout.closeDrawers();
            return true;
        });

    }

    private void adjustHamburgerIcon() {
        binding.toolbar.toolbar.post(() -> {
            // Find the hamburger icon
            for (int i = 0; i < binding.toolbar.toolbar.getChildCount(); i++) {
                View child = binding.toolbar.toolbar.getChildAt(i);
                if (child instanceof ImageButton) {
                    ImageButton hamburgerIcon = (ImageButton) child;

                    // Get the current layout parameters
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) hamburgerIcon.getLayoutParams();

                    int topMargin = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            22,  // 15dp (matching right icons) + 7dp (accounting for scale)
                            getResources().getDisplayMetrics()
                    );

                    hamburgerIcon.setPadding(
                            hamburgerIcon.getPaddingLeft(),
                            topMargin,
                            hamburgerIcon.getPaddingRight(),
                            hamburgerIcon.getPaddingBottom()
                    );

                    // Apply the layout parameters
                    hamburgerIcon.setLayoutParams(params);

                    break;  // Exit the loop once we've found and adjusted the icon
                }
            }
        });
    }

    private void refreshAd() {

        AdLoader.Builder builder = new AdLoader.Builder(requireActivity(), settingsManager.getSettings().getAdUnitIdNative());

        // OnLoadedListener implementation.
        builder.forNativeAd(
                nativeAd -> {
                    // If this callback occurs after the activity is destroyed, you must call
                    // destroy and return or you may get a memory leak.
                    boolean isDestroyed;
                    isDestroyed = requireActivity().isDestroyed();
                    if (isDestroyed || requireActivity().isFinishing() || requireActivity().isChangingConfigurations()) {
                        nativeAd.destroy();
                        return;
                    }
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (mNativeAd != null) {
                        mNativeAd.destroy();
                    }
                    mNativeAd= nativeAd;

                    @SuppressLint("InflateParams") NativeAdView adView =
                            (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                    populateNativeAdView(nativeAd, adView);
                    binding.flAdplaceholder.removeAllViews();
                    binding.flAdplaceholder.addView(adView);
                });

        VideoOptions videoOptions = new VideoOptions.Builder().build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                                        //
                                    }
                                })
                        .build();

        adLoader.loadAd(new Builder().build());

    }


    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void onCheckAuthenticatedUser() {

        onUserLogout();

        onUserSubscribe();


        if (tokenManager.getToken().getAccessToken() == null) {

            binding.toolbar.userProfile.setVisibility(GONE);
            bindingHeader.btnSubscribe.setVisibility(GONE);
            bindingHeader.btnLogin.setVisibility(VISIBLE);
            bindingHeader.userProfileName.setVisibility(View.GONE);
            bindingHeader.userProfileEmail.setVisibility(View.GONE);
            bindingHeader.userProfileEmail.setVisibility(View.GONE);
            bindingHeader.userProfileEdit.setVisibility(View.GONE);
            bindingHeader.userAvatar.setVisibility(GONE);
            bindingHeader.userProfileName.setText("");
            bindingHeader.logout.setVisibility(GONE);


        }else {


            binding.toolbar.userProfile.setVisibility(VISIBLE);
        }

        bindingHeader.logout.setOnClickListener(v -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), task -> {
                    });

            LoginManager.getInstance().logOut();
            tokenManager.deleteToken();
            authManager.deleteAuth();
            settingsManager.deleteSettings();
            adsManager.deleteAds();
            moviesListViewModel.deleteHistory();
            moviesListViewModel.deleteAllMovies();
            startActivity(new Intent(requireActivity(), SplashActivity.class));
            requireActivity().finish();
        });
    }

    private void onHandleDeleteAccount(@io.reactivex.rxjava3.annotations.NonNull UserAuthInfo auth) {

        bindingHeader.deleteAccount.setOnClickListener(v -> {



            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.delete_account);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {


                if (auth.getRole().equals("admin")){

                    Toast.makeText(requireActivity(), "You cannot delete your own admin account !", Toast.LENGTH_SHORT).show();
                    return;
                }

                authRepository.deleteUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<>() {
                            @Override
                            public void onSubscribe(@NotNull Disposable d) {

                                //

                            }

                            @Override
                            public void onNext(@NotNull Login login) {


                                Toast.makeText(requireContext(), login.getMessage(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(requireActivity(), SplashActivity.class);
                                requireActivity().startActivity(intent);


                            }

                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void onError(@NotNull Throwable e) {


                                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onComplete() {

                                //

                            }
                        });
                dialog.dismiss();

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);

        });
    }

    private void onCheckVerified(UserAuthInfo auth) {


        if (!auth.getProfiles().isEmpty() && !sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)){

            bindingHeader.userProfileName.setText(authManager.getSettingsProfile().getName());
            Tools.loadUserAvatar(requireActivity(),bindingHeader.userAvatar,authManager.getSettingsProfile().getAvatar());
            Tools.loadUserAvatar(requireActivity(),binding.toolbar.userImg,authManager.getSettingsProfile().getAvatar());


        }else {

            bindingHeader.userProfileName.setText(auth.getName());
            Tools.loadUserAvatar(requireActivity(),bindingHeader.userAvatar,auth.getAvatar());
            Tools.loadUserAvatar(requireActivity(),binding.toolbar.userImg,auth.getAvatar());

        }




        int rawResourceId = auth.getVerified() == 1 ? R.drawable.ic_verified_user : R.drawable.ic_verified_user_unverified;

        bindingHeader.verifiedImg.setImageResource(rawResourceId);


        bindingHeader.verified.setOnClickListener(v -> {

            if (auth.getVerified() == 1){

                Toast.makeText(requireActivity(), R.string.your_account_is_already_verified, Toast.LENGTH_SHORT).show();

                return;
            }


            if (settingsManager.getSettings().getPhoneVerification() == 1){


                startActivity(new Intent(requireActivity(), PhoneAuthActivity.class));
                requireActivity().finish();

            }else  if (settingsManager.getSettings().getMantenanceMode() != 1 && settingsManager.getSettings().getEmailVerify() == 1) {


            onVerifiyUserByEmail();


            }


        });


        if (settingsManager.getSettings().getPhoneVerification()  !=1 || settingsManager.getSettings().getEmailVerify() != 1){

            bindingHeader.verified.setVisibility(GONE);

        }else {


            bindingHeader.verified.setVisibility(VISIBLE);
        }

    }

    private void onUserCancelSubscribe(UserAuthInfo auth) {
        bindingHeader.userSubscribedBtn.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm_cancel_subscription);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;


            dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {

                if (auth.getType() != null && !auth.getType().isEmpty() && auth.getType().equals("paypal")) {

                    authRepository.cancelAuthSubcriptionPaypal().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull UserAuthInfo userAuthInfo) {

                                    Tools.ToastHelper(requireActivity(),SUBSCRIPTIONS);

                                    startActivity(new Intent(requireActivity(), SplashActivity.class));
                                    requireActivity().finish();
                                }

                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public void onError(@NotNull Throwable e) {

                                    //
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                } else if (auth.getType() != null && !auth.getType().isEmpty() && auth.getType().equals("stripe")) {

                    authRepository.cancelAuthSubcription().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull UserAuthInfo userAuthInfo) {

                                    Tools.ToastHelper(requireActivity(),SUBSCRIPTIONS);

                                    startActivity(new Intent(requireActivity(), SplashActivity.class));
                                    requireActivity().finish();
                                }

                                @SuppressLint("ClickableViewAccessibility")
                                @Override
                                public void onError(@NotNull Throwable e) {

                                    //
                                }

                                @Override
                                public void onComplete() {

                                    //

                                }
                            });

                } else {

                    authRepository.cancelAuthSubcriptionPaypal().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<>() {
                                @Override
                                public void onSubscribe(@NotNull Disposable d) {

                                    //

                                }

                                @Override
                                public void onNext(@NotNull UserAuthInfo userAuthInfo) {

                                    Tools.ToastHelper(requireActivity(),SUBSCRIPTIONS);

                                    startActivity(new Intent(requireActivity(), SplashActivity.class));
                                    requireActivity().finish();
                                }

                                @SuppressLint("ClickableViewAccessibility")
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


                dialog.dismiss();

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        });
    }

    private void onUserSubscribe() {
        bindingHeader.btnSubscribe.setOnClickListener(v -> {

            if (tokenManager.getToken().getAccessToken() == null) {

                Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.login_to_subscribe));


            }else {

                bindingHeader.btnSubscribe.setOnClickListener(x -> settingsViewModel.plansMutableLiveData.observe(getViewLifecycleOwner(), this::onLoadPlans));

            }
        });
    }

    private void onLoadPlans(MovieResponse plans) {

        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_plans_display);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        RecyclerView recyclerViewPlans = dialog.findViewById(R.id.recycler_view_plans);
        PlansAdapter plansAdapter = new PlansAdapter();
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPlans.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        recyclerViewPlans.setAdapter(plansAdapter);
        plansAdapter.addCasts(plans.getPlans(), settingsManager);

        ImageView splash = dialog.findViewById(R.id.splash_image);

        GlideApp.with(requireActivity())
                .asBitmap()
                .load(settingsManager.getSettings().getSplashImage())
                .fitCenter()
                .transition(BitmapTransitionOptions.withCrossFade())
                .skipMemoryCache(true)
                .into(splash);



        dialog.findViewById(R.id.bt_close).setOnClickListener(y ->
        dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void onUserLogout() {
        bindingHeader.logout.setOnClickListener(v -> {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(requireActivity(), task -> {
                    });


            authRepository.getUserLogout()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onNext(@NotNull UserAuthInfo userAuthInfo) {


                            //


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

            LoginManager.getInstance().logOut();
            tokenManager.deleteToken();
            authManager.deleteAuth();
            authManager.deleteSettingsProfile();
            settingsManager.deleteSettings();
            adsManager.deleteAds();
            moviesListViewModel.deleteHistory();
            moviesListViewModel.deleteAllMovies();

            startActivity(new Intent(requireActivity(), SplashActivity.class));
            requireActivity().finish();
        });
    }

    private void onVerifiyUserByEmail() {


        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_email_verify_notice);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;


        TextView mailTitle = dialog.findViewById(R.id.mailTitle);
        LinearLayout buttonResendToken = dialog.findViewById(R.id.resendTokenButton);
        ImageButton btclose = dialog.findViewById(R.id.bt_close);
        Button btnRestart = dialog.findViewById(R.id.btnRestart);

        buttonResendToken.setOnClickListener(v -> authRepository.getVerifyEmail()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NotNull UserAuthInfo userAuthInfo) {


                        btnRestart.setVisibility(VISIBLE);
                        buttonResendToken.setVisibility(View.GONE);
                        btclose.setVisibility(VISIBLE);

                        Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.rest_confirmation_mail) + authManager.getUserInfo().getEmail());

                    }

                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public void onError(@NotNull Throwable e) {


                        Toast.makeText(requireActivity(), R.string.error_sending_the_email, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                }));


        dialog.findViewById(R.id.bt_close).setOnClickListener(x -> {
            mailTitle.setVisibility(VISIBLE);
            btnRestart.setVisibility(GONE);
            btclose.setVisibility(GONE);
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SplashActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
                dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void onCancelSubscription(UserAuthInfo auth) {

        if (auth.getType() !=null && !auth.getType().isEmpty()) {

            if ("paypal".equals(auth.getType())) {

                loginViewModel.getExpirationStatusDetails();

                loginViewModel.expiredMutableLiveData.observe(requireActivity(), authx -> {


                    // don't translate
                    if (Objects.equals(authx.getSubscription(), "expired")) {

                        loginViewModel.cancelAuthSubscriptionPaypal();
                        loginViewModel.authCancelPaypalMutableLiveData.observe(requireActivity(), cancelsubs -> {

                            Tools.ToastHelper(requireActivity(),SUBSCRIPTIONS);
                            startActivity(new Intent(requireActivity(), SplashActivity.class));
                            requireActivity().finish();

                        });

                    }

                });


            } else if ("stripe".equals(auth.getType())) {

                loginViewModel.getAuthDetails();
                loginViewModel.getStripeSubStatusDetails();

                loginViewModel.stripeStatusDetailMutableLiveData.observe(requireActivity(), authx -> {

                    if (authx.getActive() <= 0) {

                        loginViewModel.cancelAuthSubscription();
                        loginViewModel.authCancelPlanMutableLiveData.observe(requireActivity(), cancelsubs -> {

                            if (cancelsubs != null) {

                                Tools.ToastHelper(requireActivity(),SUBSCRIPTIONS);
                                startActivity(new Intent(requireActivity(), SplashActivity.class));
                                requireActivity().finish();
                            }

                        });

                    }

                });
            }else {

                loginViewModel.getExpirationStatusDetails();

                loginViewModel.expiredMutableLiveData.observe(requireActivity(), authx -> {


                    // don't translate
                    if (authx.getSubscription().equals("expired")) {

                        loginViewModel.cancelAuthSubscriptionPaypal();
                        loginViewModel.authCancelPaypalMutableLiveData.observe(requireActivity(), cancelsubs -> {


                            if (android.os.Build.VERSION.SDK_INT == 25) {
                                ToastCompat.makeText(requireActivity(), SUBSCRIPTIONS, Toast.LENGTH_SHORT)
                                        .setBadTokenListener(toast -> Timber.e("Failed to toast")).show();
                            } else {
                                Toast.makeText(requireActivity(), SUBSCRIPTIONS, Toast.LENGTH_SHORT).show();
                            }
                            startActivity(new Intent(requireActivity(), SplashActivity.class));
                            requireActivity().finish();

                        });

                    }

                });
            }

        }else {

            loginViewModel.getExpirationStatusDetails();

            loginViewModel.expiredMutableLiveData.observe(requireActivity(), authx -> {

                // don't translate
                if (authx.getSubscription().equals("expired")) {

                    loginViewModel.cancelAuthSubscriptionPaypal();
                    loginViewModel.authCancelPaypalMutableLiveData.observe(requireActivity(), cancelsubs -> {


                        if (android.os.Build.VERSION.SDK_INT == 25) {
                            ToastCompat.makeText(requireActivity(), SUBSCRIPTIONS, Toast.LENGTH_SHORT)
                                    .setBadTokenListener(toast -> Timber.e("Failed to toast")).show();
                        } else {
                            Toast.makeText(requireActivity(), SUBSCRIPTIONS, Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(requireActivity(), SplashActivity.class));
                        requireActivity().finish();

                    });

                }

            });
        }
    }



    private void onLoadHomeContent() {

        onHandleUserAccount();

        updateNotificationList();

        onLoadNotificationCounter();

        homeViewModel.featured();

        onLoadHomeContentData();


        if (settingsManager.getSettings().getStreaming() != 0 && settingsManager.getSettings().getSafemode() != 1){


            onLoadCountinueWatching();
        }else {

            binding.linearWatch.setVisibility(View.GONE);

        }



        loadingStateController.ToHide.set(Objects.equals(loadingStateController.Type.get(), decodeString()));


        onLoadCustomBanner();
    }

    private void onLoadCustomBanner() {
        if (settingsManager.getSettings().getEnableCustomBanner() == 1) {

            Glide.with(requireActivity()).load(settingsManager.getSettings().getCustomBannerImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.customBanner);


            binding.customBanner.setOnClickListener(v -> {

                if (settingsManager.getSettings().getCustomBannerImageLink() !=null && !settingsManager.getSettings().getCustomBannerImageLink().isEmpty()) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(settingsManager.getSettings().getCustomBannerImageLink()));
                    requireActivity().startActivity(browserIntent);
                }

            });

        }else {

            binding.customBanner.setVisibility(View.GONE);
        }
    }


    private void onLoadRecycleViews() {


        // Featured
        binding.rvFeatured.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFeatured.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));

        // Countinue Watching
        binding.rvCountinueWatching.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCountinueWatching.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));


    }




    private void onLoadHomeContentData(){

        mediaRepository.getHomeContent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                        //

                    }

                    @Override
                    public void onNext(@NotNull MovieResponse media) {


                        menuHandler.isDataLoaded.set(true);

                        onLoadCollections(media);


                        onLoadNetworks(media);

                        onLoadWatchInYourLangs(media);


                        onLoadPopularCasters(media);


                        onLoadLatestAnimes(media);


                        onLoadUpcoming(media);


                        onLoadPinned(media);


                        onLoadCustomHomeContent(media);



                        Tools.onCreateCustomRv(binding.rvTvMovies, requireActivity(),top10Adapter);
                        top10Adapter.setTo10List(media.getTop10());


                        onLoadLatestEpisodes(media);


                        Tools.onCreateCustomRv(binding.choosed, requireActivity(),choosedAdapter);
                        choosedAdapter.setChoosedList(media.getChoosed());


                        onLoadFeatured(media);


                        onLoadStreaming(media);




                        Tools.onCreateCustomRv(binding.rvRecommended, requireActivity(),recommendedAdapter);
                        recommendedAdapter.setRecommendedMoviesList(media.getRecommended());




                        Tools.onCreateCustomRv(binding.rvTrending, requireActivity(),trendingAdapter);
                        trendingAdapter.setTrendingMoviesList(media.getTrending());



                        Tools.onCreateCustomRv(binding.rvLatest, requireActivity(),latestAdapter);
                        latestAdapter.setLatestMoviesList(media.getLatest());



                        Tools.onCreateCustomRv(binding.rvSeriesPopular, requireActivity(),popularAdapter);
                        popularAdapter.setPopularSeriesList(media.getPopular());

                        Tools.onCreateCustomRv(binding.rvSeriesRecents, requireActivity(),latestSeriesAdapter);
                        latestSeriesAdapter.setLatestSeriesList(media.getLatestSeries());



                        Tools.onCreateCustomRv(binding.rvAnimes, requireActivity(),latestAnimesAdapter);
                        if (settingsManager.getSettings().getAnime() == 0){
                            binding.rvAnimes.setVisibility(View.GONE);
                            binding.rvAnimesLinear.setVisibility(View.GONE);

                        }else {

                            latestAnimesAdapter.setLatestAnimesList(media.getAnimes());

                        }


                        Tools.onCreateCustomRv(binding.rvNewthisweek, requireActivity(),newThisWeekAdapter);
                        newThisWeekAdapter.setNewThisWeekList(media.getThisweek());
                        if (newThisWeekAdapter.getItemCount() == 0){


                            binding.rvNewthisweek.setVisibility(View.GONE);
                            binding.newthisweekLinear.setVisibility(View.GONE);

                        }


                        Tools.onCreateCustomRv(binding.rvPopular, requireActivity(),mPopularAdapter);
                        mPopularAdapter.setPopularList(media.getPopularMedia());


                        Tools.onCreateCustomRv(binding.rvMoviesRecents, requireActivity(),latestMoviesAdapter);
                        latestMoviesAdapter.setLatestMoviesMediaList(media.getLatestMovies());

                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        menuHandler.isDataLoaded.set(false);
                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });



    }

    private void onLoadCollections(MovieResponse media) {

        Tools.onCreateCustomRv(binding.rvCollections, requireActivity(),collectionsAdapter);


        if (settingsManager.getSettings().getEnableCollections() == 1 && !media.getCollections().isEmpty()) {

            collectionsAdapter.addMain(media.getCollections(), requireActivity());

            binding.linearCollections.setVisibility(VISIBLE);
            binding.rvCollections.setVisibility(VISIBLE);

        }else {

            binding.linearCollections.setVisibility(View.GONE);
            binding.rvCollections.setVisibility(GONE);
        }
    }

    private void onLoadStreaming(MovieResponse media) {


        if (settingsManager.getSettings().getM3uplaylist() == 1) {

            binding.linearLatestChannels.setVisibility(GONE);
            binding.rvLatestStreaming.setVisibility(GONE);
            return;
        }

        Tools.onCreateCustomRv(binding.rvLatestStreaming, requireActivity(),latestStreamingAdapter);

        if (settingsManager.getSettings().getStreaming() == 1) {

            latestStreamingAdapter.addStreaming(requireActivity(), media.getStreaming());



            if (latestStreamingAdapter.getItemCount() == 0) {

                binding.linearLatestChannels.setVisibility(View.GONE);
                binding.rvLatestStreaming.setVisibility(GONE);

            }else {

                binding.linearLatestChannels.setVisibility(VISIBLE);
                binding.rvLatestStreaming.setVisibility(VISIBLE);
            }

        }else {

            binding.linearLatestChannels.setVisibility(View.GONE);
            binding.rvLatestStreaming.setVisibility(View.GONE);
        }
    }

    private void onLoadFeatured(MovieResponse media) {

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        binding.rvFeatured.setOnFlingListener(null);
        pagerSnapHelper.attachToRecyclerView(binding.rvFeatured);
        binding.indicator.attachToRecyclerView(binding.rvFeatured, pagerSnapHelper);
        binding.indicator.createIndicators(mFeaturedAdapter.getItemCount(), 0);
        binding.rvFeatured.setAdapter(mFeaturedAdapter);

        mFeaturedAdapter.addFeatured(media.getFeatured(), requireActivity(), preferences,
                mediaRepository, authManager, settingsManager, tokenManager, animeRepository, authRepository, autoScrollControl,deviceManager);

        mFeaturedLoaded = true;
        checkAllDataLoaded();

        if (settingsManager.getSettings().getEnabledynamicslider() == 1 && mFeaturedAdapter.getItemCount() > 1) {
            // Start auto-scrolling
            startAutoScrolling();

            CustomItemAnimator customItemAnimator = new CustomItemAnimator();
            binding.rvFeatured.setItemAnimator(customItemAnimator);

            binding.rvFeatured.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    // Check if the user is manually scrolling (flinging or dragging)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        // User is interacting with the RecyclerView, stop auto-scrolling
                        stopAutoScrolling();
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // User has stopped interacting with the RecyclerView, resume auto-scrolling
                        startAutoScrolling();
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //
                }
            });
        }
    }

    private void startAutoScrolling() {
        stopAutoScrolling(); // Ensure any existing callbacks are removed

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        long delay = settingsManager.getSettings().getSlidertimer() * 1000L;
        handler.postDelayed(autoScrollRunnable, delay);
    }

    private void stopAutoScrolling() {
        if (handler != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void onLoadLatestEpisodes(MovieResponse media) {

        Tools.onCreateCustomRv(binding.rvEpisodesLatest, requireActivity(),seriesWithNewEpisodesAdapter);

        seriesWithNewEpisodesAdapter.addStreaming(requireActivity(), media.getLatestEpisodes(), settingsManager, mediaRepository, authManager, tokenManager);

        if (seriesWithNewEpisodesAdapter.getItemCount() == 0){


            binding.linearEpisodesChannels.setVisibility(GONE);
            binding.rvEpisodesLatest.setVisibility(GONE);
        }else {

            binding.linearEpisodesChannels.setVisibility(VISIBLE);
            binding.rvEpisodesLatest.setVisibility(VISIBLE);
        }
    }

    private void onLoadCustomHomeContent(MovieResponse media) {

        if (settingsManager.getSettings().getEnablecustomcontent() == 1){


            onLoadCustomGenres(media);
            onLoadCustomNetwork(media);
            onLoadCustomLangs(media);


        }else {


            binding.rvHomecontent.setVisibility(GONE);
            binding.rvHomecontentNetwork.setVisibility(GONE);
            binding.rvHomecontentLangs.setVisibility(GONE);
        }



    }

    private void onLoadPinned(MovieResponse media) {

        Tools.onCreateCustomRv(binding.rvPinned, requireActivity(),pinnedAdapter);

        if (settingsManager.getSettings().getEnablePinned() == 1 && !media.getPinned().isEmpty()) {

            pinnedAdapter.setPinnedList(media.getPinned());


            binding.rvPinned.setVisibility(VISIBLE);
            binding.linearPinned.setVisibility(VISIBLE);

        }else {

            binding.rvPinned.setVisibility(GONE);
            binding.linearPinned.setVisibility(GONE);
        }
    }

    private void onLoadUpcoming(MovieResponse media) {


        Tools.onCreateCustomRv(binding.rvUpcoming, requireActivity(),mUpcomingAdapter);


        if (settingsManager.getSettings().getEnableUpcoming() == 1 && !media.getUpcoming().isEmpty()) {

            mUpcomingAdapter.addCasts(media.getUpcoming(), settingsManager);

            binding.rvUpcoming.setVisibility(VISIBLE);
            binding.linearUpcoming.setVisibility(VISIBLE);

        }else {

            binding.rvUpcoming.setVisibility(GONE);
            binding.linearUpcoming.setVisibility(GONE);
        }
    }

    private void onLoadLatestAnimes(MovieResponse media) {


        Tools.onCreateCustomRv(binding.rvEpisodesLatestAnimes, requireActivity(),animesWithNewEpisodesAdapter);

        if (settingsManager.getSettings().getAnime() == 1) {

            animesWithNewEpisodesAdapter.addStreaming(requireActivity(),
                    media.getLatestEpisodesAnimes(), settingsManager, mediaRepository, authManager, tokenManager, animeRepository);


            if (animesWithNewEpisodesAdapter.getItemCount() == 0){


                binding.linearEpisodesAnimes.setVisibility(GONE);
                binding.rvEpisodesLatestAnimes.setVisibility(GONE);
            }else {

                binding.linearEpisodesAnimes.setVisibility(VISIBLE);
                binding.rvEpisodesLatestAnimes.setVisibility(VISIBLE);
            }

        }else {

            binding.linearEpisodesAnimes.setVisibility(View.GONE);
            binding.rvEpisodesLatestAnimes.setVisibility(GONE);

        }
    }

    private void onLoadPopularCasters(MovieResponse media) {


        Tools.onCreateCustomRv(binding.rvPopularCasters, requireActivity(),popularCastersAdapter);

        if (settingsManager.getSettings().getDefaultCastOption() !=null && !settingsManager.getSettings().getDefaultCastOption().equals("IMDB")){

            popularCastersAdapter.addMain(media.getPopularCasters(), requireActivity());

            if (popularCastersAdapter.getItemCount() == 0){


                binding.linearPopularCasters.setVisibility(GONE);
                binding.rvPopularCasters.setVisibility(GONE);
            }else {

                binding.linearPopularCasters.setVisibility(VISIBLE);
                binding.rvPopularCasters.setVisibility(VISIBLE);
            }

        }else {

            binding.linearPopularCasters.setVisibility(GONE);
            binding.rvPopularCasters.setVisibility(GONE);
        }
    }

    private void onLoadWatchInYourLangs(MovieResponse media) {


        Tools.onCreateCustomRv(binding.rvLanguages, requireActivity(),languagesAdapter);

        if (settingsManager.getSettings().getEnableWatchinyourlang() == 1 && !media.getLanguages().isEmpty()) {
            languagesAdapter.addMain(media.getLanguages(), requireActivity());


            binding.linearLanguages.setVisibility(VISIBLE);
            binding.rvLanguages.setVisibility(VISIBLE);

        }else {

            binding.linearLanguages.setVisibility(View.GONE);
            binding.rvLanguages.setVisibility(GONE);
        }
    }

    private void onLoadNetworks(MovieResponse media) {

        Tools.onCreateCustomRv(binding.rvNetworks, requireActivity(),networksAdapter);

        if (settingsManager.getSettings().getNetworks() == 1 && !media.getNetworks().isEmpty()) {

            networksAdapter.addMain(media.getNetworks(), requireActivity());
            binding.linearNetworks.setVisibility(VISIBLE);
            binding.rvNetworks.setVisibility(VISIBLE);

        }else {

            binding.linearNetworks.setVisibility(View.GONE);
            binding.rvNetworks.setVisibility(GONE);
        }
    }


    private void onLoadCustomLangs(MovieResponse media) {

        if (media.getRvcontentlangs() != null) {
            // Clear existing adapters, views, and title bindings



            adaptersMapLangs.clear();
            titleBindingsMap.clear();
            binding.rvHomecontentLangs.removeAllViews();


            // Loop through the key-value pairs in rvContent
            for (Map.Entry<String, List<Media>> entry : media.getRvcontentlangs().entrySet()) {
                String key = entry.getKey();
                List<Media> value = entry.getValue();




                View titleView = LayoutInflater.from(requireContext()).inflate(R.layout.row_homecontent_title, binding.rvHomecontentLangs, false);
                TextView rowTitleTextView = titleView.findViewById(R.id.row_title);
                rowTitleTextView.setText(key);

                TextView rowAllTextView = titleView.findViewById(R.id.contentall);


                rowAllTextView.setOnClickListener(v -> {

                    onShowCustomContentDialog(Color.TRANSPARENT, key, getByLangsitemPagedList());

                });


                binding.rvHomecontentLangs.addView(titleView);
                customHomeContentLangs = new RecyclerView(requireContext());
                customHomeContentLangs.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                customHomeContentLangs.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                customHomeContentLangs.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));

                binding.rvHomecontentLangs.addView(customHomeContentLangs);

                // Get or create an adapter dynamically based on the key
                CustomLangsAdapter adapter = getOrCreateAdapterLangs(key);

                // Set the data for the adapter
                adapter.setData(value, requireActivity());

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        }else {

            binding.rvHomecontentLangs.setVisibility(GONE);
        }

    }

    private CustomLangsAdapter getOrCreateAdapterLangs(String key) {

        // Check if the adapter for the key already exists
        CustomLangsAdapter adapter = adaptersMapLangs.get(key);

        if (adapter == null) {
            // If the adapter doesn't exist, create a new one
            adapter = new CustomLangsAdapter(settingsManager, appController);

            // Store the adapter in the map for future reference
            adaptersMapLangs.put(key, adapter);

            customHomeContentLangs.setAdapter(adapter);

            // Set the title binding for the adapter
            adapter.setTitleBinding(titleBindingsMap.get(key));
        }

        return adapter;
    }

    private void onLoadCustomNetwork(MovieResponse media) {


        if (media.getRvContentNetwork() != null) {
            // Clear existing adapters, views, and title bindings



            adaptersMapNetwork.clear();
            titleBindingsMap.clear();
            binding.rvHomecontentNetwork.removeAllViews();


            // Loop through the key-value pairs in rvContent
            for (Map.Entry<String, List<Media>> entry : media.getRvContentNetwork().entrySet()) {
                String key = entry.getKey();
                List<Media> value = entry.getValue();

                // Set the title using <include> when the key is not "CONTENT"
                View titleView = LayoutInflater.from(requireContext()).inflate(R.layout.row_homecontent_title, binding.rvHomecontentNetwork, false);
                TextView rowTitleTextView = titleView.findViewById(R.id.row_title);

                rowTitleTextView.setText(key);

                TextView rowAllTextView = titleView.findViewById(R.id.contentall);


                rowAllTextView.setOnClickListener(v -> {

                    onShowCustomContentDialog(Color.TRANSPARENT, key, getByNetworksitemPagedList());

                });


                binding.rvHomecontentNetwork.addView(titleView);
                customHomeContentNetwork = new RecyclerView(requireContext());
                customHomeContentNetwork.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                customHomeContentNetwork.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                customHomeContentNetwork.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));

                binding.rvHomecontentNetwork.addView(customHomeContentNetwork);

                // Get or create an adapter dynamically based on the key
                CustomNetworkAdapter adapter = getOrCreateAdapterNetwork(key);

                // Set the data for the adapter
                adapter.setData(value, requireActivity());

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        }else {

            binding.rvHomecontentNetwork.setVisibility(GONE);

        }

    }

    private void onShowCustomContentDialog(int transparent, String key, LiveData<PagedList<Media>> ByNetworksitemPagedList) {


        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_movies_by_genres);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(transparent));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.gravity = Gravity.BOTTOM;
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;

        RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
        TextView mGenreType = dialog.findViewById(R.id.movietitle);

        mGenreType.setText(key);

        customContentSearch.setValue(Tools.URLDecoder(key));

        ByNetworksitemPagedList.observe(getViewLifecycleOwner(), genresList -> {

            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
            recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(requireActivity(), 0), true));
            byGenreAdapter.submitList(genresList);
            recyclerView.setAdapter(byGenreAdapter);


        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                dialog.dismiss());
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private CustomNetworkAdapter getOrCreateAdapterNetwork(String key) {

        // Check if the adapter for the key already exists
        CustomNetworkAdapter adapter = adaptersMapNetwork.get(key);

        if (adapter == null) {
            // If the adapter doesn't exist, create a new one
            adapter = new CustomNetworkAdapter(settingsManager, appController);

            // Store the adapter in the map for future reference
            adaptersMapNetwork.put(key, adapter);

            customHomeContentNetwork.setAdapter(adapter);

            // Set the title binding for the adapter
            adapter.setTitleBinding(titleBindingsMap.get(key));
        }

        return adapter;

    }

    private void onLoadCustomGenres(MovieResponse media) {
        if (media.getRvContent() != null) {

            // Clear existing adapters, views, and title bindings
            adaptersMap.clear();
            titleBindingsMap.clear();
            binding.rvHomecontent.removeAllViews();

            // Loop through the key-value pairs in rvContent
            for (Map.Entry<String, List<Media>> entry : media.getRvContent().entrySet()) {
                String key = entry.getKey();
                List<Media> value = entry.getValue();

                // Set the title using <include> when the key is not "CONTENT"
                View titleView = LayoutInflater.from(requireContext()).inflate(R.layout.row_homecontent_title, binding.rvHomecontent, false);
                TextView rowTitleTextView = titleView.findViewById(R.id.row_title);
                TextView rowAllTextView = titleView.findViewById(R.id.contentall);


                rowAllTextView.setOnClickListener(v -> {

                    onShowCustomContentDialog(Color.TRANSPARENT, key, getByGenresitemPagedList());

                });

                rowTitleTextView.setText(key);

                binding.rvHomecontent.addView(titleView);

                customHomeContent = new RecyclerView(requireContext());
                customHomeContent.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                customHomeContent.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                customHomeContent.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));

                binding.rvHomecontent.addView(customHomeContent);

                // Get or create an adapter dynamically based on the key
                CustomGenreAdapter adapter = getOrCreateAdapter(key);

                // Set the data for the adapter
                adapter.setData(value, requireActivity());

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }
        }else {

            binding.rvHomecontent.setVisibility(GONE);
        }
    }





    private CustomGenreAdapter getOrCreateAdapter(String key) {
        // Check if the adapter for the key already exists
        CustomGenreAdapter adapter = adaptersMap.get(key);

        if (adapter == null) {
            // If the adapter doesn't exist, create a new one
            adapter = new CustomGenreAdapter(settingsManager, appController);

            // Store the adapter in the map for future reference
            adaptersMap.put(key, adapter);

            // Set the adapter to the existing RecyclerView (customHomeContent)
            customHomeContent.setAdapter(adapter);

            // Set the title binding for the adapter
            adapter.setTitleBinding(titleBindingsMap.get(key));
        }

        return adapter;
    }



    private void onLoadToolbar() {

        Tools.loadToolbar(((AppCompatActivity)requireActivity()),binding.toolbar.toolbar,binding.appbar);
        Tools.loadMiniLogo(requireActivity(),binding.toolbar.logoImageTop);

        CastButtonFactory.setUpMediaRouteButton(requireActivity(), binding.toolbar.mediaRouteMenuItem);


    }




    private void onLoadCountinueWatching() {


        int profileSelection = settingsManager.getSettings().getProfileSelection();


        LiveData<List<History>> historyLiveData = (profileSelection == 1 && authManager.getSettingsProfile().getId() != null && !sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT,false)) ?
                moviesListViewModel.getHistoryWatchForProfiles() :
                moviesListViewModel.getHistoryWatch();

        historyLiveData.observe(getViewLifecycleOwner(), history -> {
            Collections.reverse(history);

            historydapter.addToContent(history);
            binding.rvCountinueWatching.setAdapter(historydapter);

            updateUIBasedOnHistory(history);
        });


        binding.clearHistory.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(requireActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.clear_mylist);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            dialog.findViewById(R.id.bt_getcode).setOnClickListener(x -> {
                moviesListViewModel.deleteHistory();
                moviesListViewModel.deleteResume();
                Tools.ToastHelper(requireActivity(),requireActivity().getString(R.string.history_cleared));
                dialog.dismiss();

            });

            dialog.findViewById(R.id.bt_close).setOnClickListener(x -> dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);

        });

    }



    private void updateUIBasedOnHistory(List<History> history) {

        if (history.isEmpty()) {
            binding.linearWatch.setVisibility(View.GONE);
            binding.coutinueWatchingTitle.setText(getString(R.string.continue_watching));

        } else {
            String profileWatchHistory = Boolean.FALSE.equals(sharedPreferences.getBoolean(ISUSER_MAIN_ACCOUNT, false)) ?
                    authManager.getSettingsProfile().getName() : authManager.getUserInfo().getName();

            // Check if profileWatchHistory is null and replace it with an empty string if it is
            profileWatchHistory = profileWatchHistory != null ? profileWatchHistory : "";

            String profileWatchHistoryTitle;
            // If profileWatchHistory is empty, use "Continue Watching", otherwise use "Continue Watching for"
            if (profileWatchHistory.isEmpty()) {
                profileWatchHistoryTitle = getString(R.string.continue_watching);
            } else {
                profileWatchHistoryTitle = getString(R.string.continue_watching_profile);
            }

        // Set the title for the view, concatenating it with the profile watch history
            binding.coutinueWatchingTitle.setText(String.format("%s %s", profileWatchHistoryTitle, profileWatchHistory));

        // Make the linear layout containing the watch history visible
            binding.linearWatch.setVisibility(View.VISIBLE);

        }
    }


    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {


            int itemCount = mFeaturedAdapter.getItemCount();

            if (currentPage == itemCount - 1) {
                // If we are at the last item, smoothly scroll back to the first item
                binding.rvFeatured.scrollToPosition(0);
                currentPage = 0;
            } else {
                // Scroll to the next item
                binding.rvFeatured.scrollToPosition(currentPage + 1);
                currentPage++;
            }

            handler.postDelayed(this, settingsManager.getSettings().getSlidertimer() * 1000L);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isObserverRegistered) {
            mFeaturedAdapter.unregisterAdapterDataObserver(binding.indicator.getAdapterDataObserver());
            isObserverRegistered = false;
        }


        compositeDisposable.clear();


    }


    // Make sure all calls finished before showing results
    private void checkAllDataLoaded() {

        if (mFeaturedLoaded) {
            binding.scrollView.setVisibility(VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroy() {


        if (settingsManager.getSettings().getNotificationCounter() == 1){

            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(notificationReceiver);
        }

        if (nativeAdMedia != null) {
            nativeAdMedia.destroy();
            nativeAdMedia = null;
        }

        if (mNativeAd !=null) {

            mNativeAd.destroy();
        }

        if (nativeAd != null) {
            nativeAd.unregisterView();
            nativeAd.destroy();
            nativeAd = null;
        }

        if (adOptionsView !=null) {

            adOptionsView = null;
        }
        super.onDestroy();


        if (settingsManager.getSettings().getEnabledynamicslider() == 1) {
            stopAutoScrolling();
        }

        compositeDisposable.clear();
    }


    static PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(ByGenreListDataSource.PAGE_SIZE)
                    .setPrefetchDistance(ByGenreListDataSource.PAGE_SIZE)
                    .setInitialLoadSizeHint(ByGenreListDataSource.PAGE_SIZE)
                    .build();


    public  LiveData<PagedList<Media>> getByLangsitemPagedList() {
        return Transformations.switchMap(customContentSearch, query -> {
            LanguagesListDataSourceFactory factory = mediaRepository.langsListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }


    public LiveData<PagedList<Media>> getByNetworksitemPagedList() {
        return Transformations.switchMap(customContentSearch, query -> {
            NetworksListDataSourceFactory factory = mediaRepository.networksListDataSourceFactory(query,true,false);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }


    public LiveData<PagedList<Media>> getByGenresitemPagedList() {
        return Transformations.switchMap(customContentSearch, query -> {
            NetworksListDataSourceFactory factory = mediaRepository.networksListDataSourceFactory(query,true,true);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }

    @Override
    public void onNotificationClick(Notification notification, int position, Dialog dialog) {

        //
    }
}
