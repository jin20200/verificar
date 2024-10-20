package com.easyplexdemoapp.di.module;

import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.casts.CastDetailsActivity;
import com.easyplexdemoapp.ui.devices.UserDevicesManagement;
import com.easyplexdemoapp.ui.downloadmanager.ui.main.DownloadManagerFragment;
import com.easyplexdemoapp.ui.login.LoginActivity;
import com.easyplexdemoapp.ui.login.PasswordForget;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.notifications.NotificationManager;
import com.easyplexdemoapp.ui.payment.Payment;
import com.easyplexdemoapp.ui.payment.PaymentDetails;
import com.easyplexdemoapp.ui.payment.PaymentPaypal;
import com.easyplexdemoapp.ui.payment.PaymentStripe;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.ui.profile.EditProfileActivity;
import com.easyplexdemoapp.ui.register.RegisterActivity;
import com.easyplexdemoapp.ui.register.RegistrationSucess;
import com.easyplexdemoapp.ui.seriedetails.EpisodeDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.notifications.NotificationHandlerService;
import com.easyplexdemoapp.ui.splash.ConfiigurationFirstLaunch;
import com.easyplexdemoapp.ui.splash.SplashActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.ui.trailer.TrailerPreviewActivity;
import com.easyplexdemoapp.ui.upcoming.UpcomingTitlesActivity;
import com.easyplexdemoapp.ui.users.PhoneAuthActivity;
import com.easyplexdemoapp.ui.users.UserProfiles;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Binds all sub-components within the app. Add bindings for other sub-components here.
 * @ContributesAndroidInjector was introduced removing the need to:
 * a) Create separate components annotated with @Subcomponent (the need to define @Subcomponent classes.)
 * b) Write custom annotations like @PerActivity.
 *
 * @author Yobex.
 */
@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract NotificationHandlerService contributeNotificationHandlerService();

    @ContributesAndroidInjector()
    abstract ConfiigurationFirstLaunch contributeConfiigurationFirstLaunch();

    @ContributesAndroidInjector()
    abstract UserDevicesManagement contributeUserDevicesManagement();


    @ContributesAndroidInjector()
    abstract PhoneAuthActivity contributePhoneAuthActivity();

    @ContributesAndroidInjector()
    abstract UserProfiles contributeUserProfiles();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract BaseActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract DownloadManagerFragment contributeMainActivityDown();

    @ContributesAndroidInjector()
    abstract Payment contributePayment();

    @ContributesAndroidInjector()
    abstract PaymentPaypal contributePaymentPaypal();

    @ContributesAndroidInjector()
    abstract PaymentStripe contributePaymentStripe();

    @ContributesAndroidInjector()
    abstract NotificationManager contributeNotificationManager();

    @ContributesAndroidInjector()
    abstract PaymentDetails contributePaymentDetails();

    @ContributesAndroidInjector()
    abstract RegistrationSucess contributeRegistrationSucess();

    @ContributesAndroidInjector()
    abstract EditProfileActivity contributeEditProfileActivity();

    @ContributesAndroidInjector()
    abstract MovieDetailsActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector()
    abstract SerieDetailsActivity contributeSerieDetailActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector()
    abstract RegisterActivity contributeRegisterActivity();

    @ContributesAndroidInjector()
    abstract TrailerPreviewActivity contributeTrailerPreviewActivity();

    @ContributesAndroidInjector()
    abstract UpcomingTitlesActivity contributeUpcomingTitlesActivity();

    @ContributesAndroidInjector()
    abstract AnimeDetailsActivity contributeAnimeDetailsActivity();

    @ContributesAndroidInjector()
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector()
    abstract EmbedActivity contributeEmbedActivity();

    @ContributesAndroidInjector()
    abstract EasyPlexMainPlayer contributeEasyPlexMainPlayer();

    @ContributesAndroidInjector()
    abstract PasswordForget contributePasswordForget();

    @ContributesAndroidInjector()
    abstract CastDetailsActivity contributeCastDetailsActivity();

    @ContributesAndroidInjector()
    abstract StreamingetailsActivity contributeStreamingetailsActivity();

    @ContributesAndroidInjector()
    abstract EpisodeDetailsActivity contributeEpisodeDetailsActivity();
}
