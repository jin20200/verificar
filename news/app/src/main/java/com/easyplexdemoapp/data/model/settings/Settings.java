package com.easyplexdemoapp.data.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {



    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private String apiKey;

    @SerializedName("id")
    @Expose
    private Integer id;

    public boolean isCrash() {
        return crash;
    }

    public void setCrash(boolean crash) {
        this.crash = crash;
    }

    @SerializedName("crash")
    @Expose
    private boolean crash;
    @SerializedName("latestVersion")
    @Expose
    private String latestVersion;

    @SerializedName("app_name")
    @Expose
    private String appName;

    @SerializedName("paypal_client_id")
    @Expose
    private String paypalClientId;

    @SerializedName("paypal_amount")
    @Expose
    private String paypalAmount;

    @SerializedName("privacy_policy")
    @Expose
    private String privacyPolicy;


    @SerializedName("tmdb_api_key")
    @Expose
    private String tmdbApiKey;


    @SerializedName("purchase_key")
    @Expose
    private String purchaseKey;


    @SerializedName("stripe_publishable_key")
    @Expose
    private String stripePublishableKey;


    @SerializedName("stripe_secret_key")
    @Expose
    private String stripeSecretKey;

    public String getHxfileApiKey() {
        return hxfileApiKey;
    }

    public void setHxfileApiKey(String hxfileApiKey) {
        this.hxfileApiKey = hxfileApiKey;
    }

    @SerializedName("hxfile_api_key")
    @Expose
    private String hxfileApiKey;

    public String getDefaultCastOption() {
        return defaultCastOption;
    }

    public void setDefaultCastOption(String defaultCastOption) {
        this.defaultCastOption = defaultCastOption;
    }

    @SerializedName("default_cast_option")
    @Expose
    private String defaultCastOption;


    public String getDefault_layout_networks() {
        return default_layout_networks;
    }

    public void setDefault_layout_networks(String default_layout_networks) {
        this.default_layout_networks = default_layout_networks;
    }

    @SerializedName("default_layout_networks")
    @Expose
    private String default_layout_networks;

    @SerializedName("app_packagename")
    @Expose
    private String appPackagename;

    public String getAppPackagename() {
        return appPackagename;
    }

    public void setAppPackagename(String appPackagename) {
        this.appPackagename = appPackagename;
    }

    public String getDefaultTrailerDefault() {
        return defaultTrailerDefault;
    }

    public void setDefaultTrailerDefault(String defaulttrailerdefault) {
        this.defaultTrailerDefault = defaulttrailerdefault;
    }


    @SerializedName("separate_download")
    @Expose
    private int separateDownload;

    @SerializedName("default_trailer_default")
    @Expose
    private String defaultTrailerDefault;



    public int getSeparateDownload() {
        return separateDownload;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void setSeparateDownload(int separateDownload) {
        this.separateDownload = separateDownload;
    }



    @SerializedName("flag_secure")
    @Expose
    private int flagSecure;


    public int getFlagSecure() {
        return flagSecure;
    }

    public void setFlagSecure(int flagSecure) {
        this.flagSecure = flagSecure;
    }


    @SerializedName("suggest_auth")
    @Expose
    private int suggestAuth;

    public int getSuggestAuth() {
        return suggestAuth;
    }

    public void setSuggestAuth(int suggestAuth) {
        this.suggestAuth = suggestAuth;
    }

    @SerializedName("enable_download")
    @Expose
    private int enableDownload;

    public int getForce_inappupdate() {
        return force_inappupdate;
    }

    public void setForce_inappupdate(int force_inappupdate) {
        this.force_inappupdate = force_inappupdate;
    }

    @SerializedName("force_inappupdate")
    @Expose
    private int force_inappupdate;


    public int getForce_password_access() {
        return force_password_access;
    }

    public void setForce_password_access(int force_password_access) {
        this.force_password_access = force_password_access;
    }

    @SerializedName("force_password_access")
    @Expose
    private int force_password_access;


    public int getEmailVerify() {
        return emailVerify;
    }

    public int getUnityShow() {
        return unityShow;
    }

    public void setUnityShow(int unityShow) {
        this.unityShow = unityShow;
    }

    public void setEmailVerify(int emailVerify) {
        this.emailVerify = emailVerify;
    }

    @SerializedName("email_verify")
    @Expose
    private int emailVerify;


    public int getSeasonStyle() {
        return seasonStyle;
    }

    public void setSeasonStyle(int seasonStyle) {
        this.seasonStyle = seasonStyle;
    }

    @SerializedName("season_style")
    @Expose
    private int seasonStyle;



    @SerializedName("unity_show")
    @Expose
    private int unityShow;

    public int getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(int enableDownload) {
        this.enableDownload = enableDownload;
    }

    public int getForceLogin() {
        return forceLogin;
    }

    public void setForceLogin(int forceLogin) {
        this.forceLogin = forceLogin;
    }

    @SerializedName("force_login")
    @Expose
    private int forceLogin;

    @SerializedName("force_update")
    @Expose
    private int forceUpdate;

    public String getDefaultMediaPlaceholderPath() {
        return defaultMediaPlaceholderPath;
    }

    public void setDefaultMediaPlaceholderPath(String defaultMediaPlaceholderPath) {
        this.defaultMediaPlaceholderPath = defaultMediaPlaceholderPath;
    }

    @SerializedName("default_media_placeholder_path")
    @Expose
    private String defaultMediaPlaceholderPath;

    @SerializedName("app_url_android")
    @Expose
    private String appUrlAndroid;


    @SerializedName("facebook_url")
    @Expose
    private String facebookUrl;

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;


    @SerializedName("instagram_url")
    @Expose
    private String instagramUrl;


    @SerializedName("telegram_url")
    @Expose
    private String telegram;


    @SerializedName("default_substitle_option")
    @Expose
    private String defaultSubstitleOption;

    public String getDefaultSubstitleOption() {
        return defaultSubstitleOption;
    }

    public void setDefaultSubstitleOption(String defaultSubstitleOption) {
        this.defaultSubstitleOption = defaultSubstitleOption;
    }

    @SerializedName("autosubstitles")
    @Expose
    private int autosubstitles;


    public int getDownloadPremuimOnly() {
        return downloadPremuimOnly;
    }

    public void setDownloadPremuimOnly(int downloadPremuimOnly) {
        this.downloadPremuimOnly = downloadPremuimOnly;
    }

    public int getNextEpisodeTimer() {
        return nextEpisodeTimer;
    }

    public void setNextEpisodeTimer(int nextEpisodeTimer) {
        this.nextEpisodeTimer = nextEpisodeTimer;
    }

    @SerializedName("next_episode_timer")
    @Expose
    private int nextEpisodeTimer;

    public int getAllowAdm() {
        return allowAdm;
    }

    public void setAllowAdm(int allowAdm) {
        this.allowAdm = allowAdm;
    }



    public int getVlc() {
        return vlc;
    }

    public void setVlc(int vlc) {
        this.vlc = vlc;
    }

    @SerializedName("enable_vlc")
    @Expose
    private int vlc;

    @SerializedName("startapp_interstitial")
    @Expose
    private int startappInterstitial;

    public int getStartappInterstitial() {
        return startappInterstitial;
    }

    public void setStartappInterstitial(int startappInterstitial) {
        this.startappInterstitial = startappInterstitial;
    }

    public int getAppReadyToLoadUi() {
        return AppReadyToLoadUi;
    }

    public void setAppReadyToLoadUi(int appReadyToLoadUi) {
        this.AppReadyToLoadUi = appReadyToLoadUi;
    }

    public int getUnityadsInterstitial() {
        return unityadsInterstitial;
    }

    public void setUnityadsInterstitial(int unityadsInterstitial) {
        this.unityadsInterstitial = unityadsInterstitial;
    }

    public int getUnityadsBanner() {
        return unityadsBanner;
    }

    public void setUnityadsBanner(int unityadsBanner) {
        this.unityadsBanner = unityadsBanner;
    }


    public int getStreaming() {
        return streaming;
    }

    public void setStreaming(int streaming) {
        this.streaming = streaming;
    }


    public int getEnableBannerBottom() {
        return enableBannerBottom;
    }

    public void setEnableBannerBottom(int enableBannerBottom) {
        this.enableBannerBottom = enableBannerBottom;
    }

    @SerializedName("enable_banner_bottom")
    @Expose
    private int enableBannerBottom;

    @SerializedName("streaming")
    @Expose
    private int streaming;


    public int getNetworks() {
        return networks;
    }

    public void setNetworks(int networks) {
        this.networks = networks;
    }

    @SerializedName("networks")
    @Expose
    private int networks;

    @SerializedName("unityads_interstitial")
    @Expose
    private int unityadsInterstitial;

    @SerializedName("unityads_banner")
    @Expose
    private int unityadsBanner;

    @SerializedName("startapp_banner")
    @Expose
    private int AppReadyToLoadUi;

    public int getResumeOffline() {
        return resumeOffline;
    }

    public void setResumeOffline(int resumeOffline) {
        this.resumeOffline = resumeOffline;
    }

    @SerializedName("resume_offline")
    @Expose
    private int resumeOffline;

    @SerializedName("allow_adm")
    @Expose
    private int allowAdm;

    @SerializedName("download_premuim_only")
    @Expose
    private int downloadPremuimOnly;


    @SerializedName("ads_player")
    @Expose
    private int ads;

    @SerializedName("anime")
    @Expose
    private Integer anime;

    public String getDefaultNotification() {
        return defaultNotification;
    }

    public void setDefaultNotification(String defaultNotification) {
        this.defaultNotification = defaultNotification;
    }

    @SerializedName("ad_app_id")
    @Expose
    private String adAppId;


    @SerializedName("default_notification")
    @Expose
    private String defaultNotification;


    @SerializedName("update_title")
    @Expose
    private String updateTitle;


    @SerializedName("releaseNotes")
    @Expose
    private String releaseNotes;


    @SerializedName("url")
    @Expose
    private String url;



    @SerializedName("imdb_cover_path")
    @Expose
    private String imdbCoverPath;




    @SerializedName("custom_message")
    @Expose
    private String customMessage;

    @SerializedName("trustAllCerts")
    @Expose
    private int trustAllCerts;

    public int getTrustAllCerts() {
        return trustAllCerts;
    }

    public void setTrustAllCerts(int trustAllCerts) {
        this.trustAllCerts = trustAllCerts;
    }


    @SerializedName("device_management")
    @Expose
    private int deviceManagement;

    public int getInternallangs() {
        return internallangs;
    }

    public void setInternallangs(int internallangs) {
        this.internallangs = internallangs;
    }

    @SerializedName("internallangs")
    @Expose
    private int internallangs;


    public int getSearchhistory() {
        return searchhistory;
    }

    public void setSearchhistory(int searchhistory) {
        this.searchhistory = searchhistory;
    }

    @SerializedName("searchhistory")
    @Expose
    private int searchhistory;

    public int getForcewatchbyauth() {
        return forcewatchbyauth;
    }

    public void setForcewatchbyauth(int forcewatchbyauth) {
        this.forcewatchbyauth = forcewatchbyauth;
    }

    @SerializedName("forcewatchbyauth")
    @Expose
    private int forcewatchbyauth;

    public int getMergesubs() {
        return mergesubs;
    }

    public void setMergesubs(int mergesubs) {
        this.mergesubs = mergesubs;
    }


    @SerializedName("mergesubs")
    @Expose
    private int mergesubs;



    public int getEnableWatchinyourlang() {
        return enableWatchinyourlang;
    }

    public void setEnableWatchinyourlang(int enableWatchinyourlang) {
        this.enableWatchinyourlang = enableWatchinyourlang;
    }

    @SerializedName("enable_watchinyourlang")
    @Expose
    private int enableWatchinyourlang;


    public int getSafemode() {
        return safemode;
    }

    public void setSafemode(int safemode) {
        this.safemode = safemode;
    }

    @SerializedName("safemode")
    @Expose
    private int safemode;

    public int getDeviceManagement() {
        return deviceManagement;
    }

    public void setDeviceManagement(int deviceManagement) {
        this.deviceManagement = deviceManagement;
    }

    @SerializedName("profile_selection")
    @Expose
    private int profileSelection;

    public int getProfileSelection() {
        return profileSelection;
    }

    public void setProfileSelection(int profileSelection) {
        this.profileSelection = profileSelection;
    }

    public int getPhoneVerification() {
        return phoneVerification;
    }

    public void setPhoneVerification(int phoneVerification) {
        this.phoneVerification = phoneVerification;
    }

    @SerializedName("phone_verification")
    @Expose
    private int phoneVerification;


    public int getEnablePlayerInter() {
        return enable_player_inter;
    }

    public int getEnable_player_inter() {
        return enable_player_inter;
    }

    public void setEnable_player_inter(int enable_player_inter) {
        this.enable_player_inter = enable_player_inter;
    }

    public int getEnableShadows() {
        return enableShadows;
    }

    public void setEnableShadows(int enableShadows) {
        this.enableShadows = enableShadows;
    }

    public void setEnablePlayerInter(int enablePlayerInter) {
        this.enable_player_inter = enablePlayerInter;
    }

    @SerializedName("enable_player_inter")
    @Expose
    private int enable_player_inter;


    public int getEnablecustomcontent() {
        return enablecustomcontent;
    }

    public void setEnablecustomcontent(int enablecustomcontent) {
        this.enablecustomcontent = enablecustomcontent;
    }

    @SerializedName("enablecustomcontent")
    @Expose
    private int enablecustomcontent;

    @SerializedName("discover_style")
    @Expose
    private int discoverStyle;

    public int getLibraryStyle() {
        return libraryStyle;
    }

    public void setLibraryStyle(int libraryStyle) {
        this.libraryStyle = libraryStyle;
    }

    @SerializedName("library_style")
    @Expose
    private int libraryStyle;

    public int getDiscoverStyle() {
        return discoverStyle;
    }

    public void setDiscoverStyle(int discoverStyle) {
        this.discoverStyle = discoverStyle;
    }

    @SerializedName("enable_shadows")
    @Expose
    private int enableShadows;

    @SerializedName("wach_ads_to_unlock")
    @Expose
    private int wachAdsToUnlock;

    public int getWachAdsToUnlockPlayer() {
        return wachAdsToUnlockPlayer;
    }

    public void setWachAdsToUnlockPlayer(int wachAdsToUnlockPlayer) {
        this.wachAdsToUnlockPlayer = wachAdsToUnlockPlayer;
    }

    @SerializedName("wach_ads_to_unlock_player")
    @Expose
    private int wachAdsToUnlockPlayer;


    @SerializedName("startapp_id")
    @Expose
    private String startappId;


    public int getNotificationStyle() {
        return notificationStyle;
    }

    public void setNotificationStyle(int notificationStyle) {
        this.notificationStyle = notificationStyle;
    }

    @SerializedName("notification_style")
    @Expose
    private int notificationStyle;




    @SerializedName("root_detection")
    @Expose
    private int rootDetection;

    public int getRootDetection() {
        return rootDetection;
    }

    public void setRootDetection(int rootDetection) {
        this.rootDetection = rootDetection;
    }

    public int getVpn() {
        return vpn;
    }

    public void setVpn(int vpn) {
        this.vpn = vpn;
    }

    @SerializedName("vpn")
    @Expose
    private int vpn;


    public int getDeviceManagementLimit() {
        return deviceManagementLimit;
    }

    public void setDeviceManagementLimit(int deviceManagementLimit) {
        this.deviceManagementLimit = deviceManagementLimit;
    }

    @SerializedName("device_management_limit")
    @Expose
    private int deviceManagementLimit;


    public int getEnablelayoutchange() {
        return enablelayoutchange;
    }

    public void setEnablelayoutchange(int enablelayoutchange) {
        this.enablelayoutchange = enablelayoutchange;
    }

    @SerializedName("enablelayoutchange")
    @Expose
    private int enablelayoutchange;

    public int getProfileSelectionLimit() {
        return profileSelectionLimit;
    }

    public void setProfileSelectionLimit(int profileSelectionLimit) {
        this.profileSelectionLimit = profileSelectionLimit;
    }

    @SerializedName("profile_selection_limit")
    @Expose
    private int profileSelectionLimit;


    public int getSlidertimer() {
        return slidertimer;
    }

    public void setSlidertimer(int slidertimer) {
        this.slidertimer = slidertimer;
    }

    @SerializedName("slidertimer")
    @Expose
    private int slidertimer;

    public int getEnabledynamicslider() {
        return enabledynamicslider;
    }

    public void setEnabledynamicslider(int enabledynamicslider) {
        this.enabledynamicslider = enabledynamicslider;
    }

    @SerializedName("enabledynamicslider")
    @Expose
    private int enabledynamicslider;


    public int getForeApiAuth() {
        return foreApiAuth;
    }

    public void setForeApiAuth(int foreApiAuth) {
        this.foreApiAuth = foreApiAuth;
    }

    @SerializedName("fore_api_auth")
    @Expose
    private int foreApiAuth;


    public int getEnableCollections() {
        return enableCollections;
    }

    public void setEnableCollections(int enableCollections) {
        this.enableCollections = enableCollections;
    }

    @SerializedName("enable_collections")
    @Expose
    private int enableCollections;


    public int getM3uplaylist() {
        return m3uplaylist;
    }

    public void setM3uplaylist(int m3uplaylist) {
        this.m3uplaylist = m3uplaylist;
    }

    @SerializedName("m3uplaylist")
    @Expose
    private int m3uplaylist;

    public int getNotificationCounter() {
        return notificationCounter;
    }

    public void setNotificationCounter(int notificationCounter) {
        this.notificationCounter = notificationCounter;
    }

    @SerializedName("notification_counter")
    @Expose
    private int notificationCounter;


    public String getM3uplaylistpath() {
        return m3uplaylistpath;
    }

    public void setM3uplaylistpath(String m3uplaylistpath) {
        this.m3uplaylistpath = m3uplaylistpath;
    }

    @SerializedName("m3uplaylistpath")
    @Expose
    private String m3uplaylistpath;


    public int getVidsrc() {
        return vidsrc;
    }

    public void setVidsrc(int vidsrc) {
        this.vidsrc = vidsrc;
    }

    @SerializedName("vidsrc")
    @Expose
    private int vidsrc;
    public int getDisablelogin() {
        return disablelogin;
    }

    public void setDisablelogin(int disablelogin) {
        this.disablelogin = disablelogin;
    }

    @SerializedName("disablelogin")
    @Expose
    private int disablelogin;


    public int getEnablesociallogins() {
        return enablesociallogins;
    }

    public void setEnablesociallogins(int enablesociallogins) {
        this.enablesociallogins = enablesociallogins;
    }

    @SerializedName("enablesociallogins")
    @Expose
    private int enablesociallogins;

    public int getEnablelangsinservers() {
        return enablelangsinservers;
    }

    public void setEnablelangsinservers(int enablelangsinservers) {
        this.enablelangsinservers = enablelangsinservers;
    }

    @SerializedName("enablelangsinservers")
    @Expose
    private int enablelangsinservers;


    @SerializedName("wortise_interstitial")
    @Expose
    private int wortiseInterstitial;

    public int getWortiseBannerEnable() {
        return wortiseBannerEnable;
    }

    public void setWortiseBannerEnable(int wortiseBannerEnable) {
        this.wortiseBannerEnable = wortiseBannerEnable;
    }

    @SerializedName("wortise_banner_enable")
    @Expose
    private int wortiseBannerEnable;




    @SerializedName("wortise_appid")
    @Expose
    private String wortiseAppid;


    public int getWortiseInterstitial() {
        return wortiseInterstitial;
    }

    public void setWortiseInterstitial(int wortiseInterstitial) {
        this.wortiseInterstitial = wortiseInterstitial;
    }

    public String getWortiseAppid() {
        return wortiseAppid;
    }

    public void setWortiseAppid(String wortiseAppid) {
        this.wortiseAppid = wortiseAppid;
    }

    public String getWortisePlacementUnitId() {
        return wortiseInterstitialUnitid;
    }

    public void setWortiseInterstitialUnitid(String wortiseInterstitialUnitid) {
        this.wortiseInterstitialUnitid = wortiseInterstitialUnitid;
    }

    public int getWortiseInterstitialShow() {
        return wortiseInterstitialShow;
    }

    public void setWortiseInterstitialShow(int wortiseInterstitialShow) {
        this.wortiseInterstitialShow = wortiseInterstitialShow;
    }

    @SerializedName("wortise_placement_unitid")
    @Expose
    private String wortiseInterstitialUnitid;

    public String getWortiseBannerUnitid() {
        return wortiseBannerUnitid;
    }

    public void setWortiseBannerUnitid(String wortiseBannerUnitid) {
        this.wortiseBannerUnitid = wortiseBannerUnitid;
    }

    public String getWortiseRewardUnitid() {
        return wortiseRewardUnitid;
    }

    public void setWortiseRewardUnitid(String wortiseRewardUnitid) {
        this.wortiseRewardUnitid = wortiseRewardUnitid;
    }

    @SerializedName("wortise_banner_unitid")
    @Expose
    private String wortiseBannerUnitid;



    @SerializedName("wortise_reward_unitid")
    @Expose
    private String wortiseRewardUnitid;




    @SerializedName("wortise_interstitial_show")
    @Expose
    private int wortiseInterstitialShow;


    public int getEnableMediaDownload() {
        return enableMediaDownload;
    }

    public void setEnableMediaDownload(int enableMediaDownload) {
        this.enableMediaDownload = enableMediaDownload;
    }

    @SerializedName("enable_media_download")
    @Expose
    private int enableMediaDownload;

    @SerializedName("notification_separated")
    @Expose
    private int notificationSeparated;

    public int getNotificationSeparated() {
        return notificationSeparated;
    }

    public void setNotificationSeparated(int notificationSeparated) {
        this.notificationSeparated = notificationSeparated;
    }

    @SerializedName("leftnavbar")
    @Expose
    private int leftnavbar;


    public int getLeftnavbar() {
        return leftnavbar;
    }

    public void setLeftnavbar(int leftnavbar) {
        this.leftnavbar = leftnavbar;
    }

    public int getFavoriteonline() {
        return favoriteonline;
    }

    public void setFavoriteonline(int favoriteonline) {
        this.favoriteonline = favoriteonline;
    }

    @SerializedName("favoriteonline")
    @Expose
    private int favoriteonline;


    public int getEnableWebview() {
        return enableWebview;
    }

    public void setEnableWebview(int enablewebview) {
        this.enableWebview = enablewebview;
    }

    @SerializedName("enable_webview")
    @Expose
    private int enableWebview;



    @SerializedName("ad_unit_id_rewarded")
    @Expose
    private String adUnitIdRewarded;


    @SerializedName("ad_unit_id__facebook_rewarded")
    @Expose
    private String adUnitIdFacebookRewarded;

    public int getAdFaceAudienceNative() {
        return adFaceAudienceNative;
    }

    public void setAdFaceAudienceNative(int adfaceaudiencenative) {
        this.adFaceAudienceNative = adfaceaudiencenative;
    }

    @SerializedName("ad_face_audience_native")
    @Expose
    private int adFaceAudienceNative;

    public String getAdUnitIdFacebookNativeAudience() {
        return adUnitIdFacebookNativeAudience;
    }

    public void setAdUnitIdFacebookNativeAudience(String adunitidfacebooknativeaudience) {
        this.adUnitIdFacebookNativeAudience = adunitidfacebooknativeaudience;
    }

    @SerializedName("ad_unit_id_facebook_native_audience")
    @Expose
    private String adUnitIdFacebookNativeAudience;

    @SerializedName("unity_game_id")
    @Expose
    private String unityGameId;


    public String getDefaultNetworkPlayer() {
        return defaultNetworkPlayer;
    }

    public void setDefaultNetworkPlayer(String defaultNetworkPlayer) {
        this.defaultNetworkPlayer = defaultNetworkPlayer;
    }

    @SerializedName("default_network_player")
    @Expose
    private String defaultNetworkPlayer;


    @SerializedName("default_network")
    @Expose
    private String defaultRewardedNetworkAds;

    public int getServerDialogSelection() {
        return serverDialogSelection;
    }

    public void setServerDialogSelection(int serverDialogSelection) {
        this.serverDialogSelection = serverDialogSelection;
    }

    public String getDefaultPayment() {
        return defaultPayment;
    }

    public void setDefaultPayment(String defaultpayment) {
        this.defaultPayment = defaultpayment;
    }

    @SerializedName("default_payment")
    @Expose
    private String defaultPayment;


    public int getLivetvMultiServers() {
        return livetvMultiServers;
    }

    public void setLivetvMultiServers(int livetvMultiServers) {
        this.livetvMultiServers = livetvMultiServers;
    }

    public void setAppnextBanner(int appnextBanner) {
        this.appnextBanner = appnextBanner;
    }

    @SerializedName("livetv_multi_servers")
    @Expose
    private int livetvMultiServers;

    @SerializedName("server_dialog_selection")
    @Expose
    private int serverDialogSelection;



    @SerializedName("ad_unit_id__appodeal_rewarded")
    @Expose
    private String adUnitIdAppodealRewarded;

    @SerializedName("facebook_show_interstitial")
    @Expose
    private int facebookShowInterstitial;

    @SerializedName("ad_show_interstitial")
    @Expose
    private int adShowInterstitial;

    @SerializedName("ad_interstitial")
    @Expose
    private int adInterstitial;




    @SerializedName("hash256")
    @Expose
    private int hash256;

    public int getHash256() {
        return hash256;
    }

    public void setHash256(int hash) {
        this.hash256 = hash;
    }

    public int getSha1() {
        return sha1;
    }

    public void setSha1(int sha1) {
        this.sha1 = sha1;
    }

    @SerializedName("sha1")
    @Expose
    private int sha1;

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    @SerializedName("auth")
    @Expose
    private int auth;


    public int getAppodealInterstitial() {
        return appodealInterstitial;
    }

    public int getAppodealBanner() {
        return appodealBanner;
    }

    public void setAppodealBanner(int appodealBanner) {
        this.appodealBanner = appodealBanner;
    }

    public void setAppodealInterstitial(int appodealInterstitial) {
        this.appodealInterstitial = appodealInterstitial;
    }

    @SerializedName("appodeal_interstitial")
    @Expose
    private int appodealInterstitial;



    @SerializedName("appnext_banner")
    @Expose
    private int appnextBanner;


    @SerializedName("appnext_interstitial")
    @Expose
    private int appnextInterstitial;

    public int getAppnextBanner() {
        return appnextBanner;
    }

    public void setappnextBanner(int appnextBanner) {
        this.appnextBanner = appnextBanner;
    }

    public int getAppnextInterstitial() {
        return appnextInterstitial;
    }

    public void setAppnextInterstitial(int appnextInterstitial) {
        this.appnextInterstitial = appnextInterstitial;
    }

    public String getAppnextPlacementid() {
        return appnextPlacementid;
    }

    public void setAppnextPlacementid(String appnextPlacementid) {
        this.appnextPlacementid = appnextPlacementid;
    }


    public int getVungleBanner() {
        return vungleBanner;
    }

    public void setVungleBanner(int vungleBanner) {
        this.vungleBanner = vungleBanner;
    }

    public int getVungleInterstitial() {
        return vungleInterstitial;
    }

    public void setVungleInterstitial(int vungleInterstitial) {
        this.vungleInterstitial = vungleInterstitial;
    }

    @SerializedName("vungle_banner")
    @Expose
    private int vungleBanner;


    @SerializedName("vungle_interstitial")
    @Expose
    private int vungleInterstitial;

    public int getApplovin_native() {
        return applovin_native;
    }

    public void setApplovin_native(int applovin_native) {
        this.applovin_native = applovin_native;
    }

    @SerializedName("applovin_native")
    @Expose
    private int applovin_native;


    @SerializedName("applovin_banner")
    @Expose
    private int applovin_banner;

    public int getApplovinBanner() {
        return applovin_banner;
    }

    public void setApplovin_banner(int applovin_banner) {
        this.applovin_banner = applovin_banner;
    }

    public int getApplovinInterstitial() {
        return applovinInterstitial;
    }

    public void setApplovinInterstitial(int applovinInterstitial) {
        this.applovinInterstitial = applovinInterstitial;
    }

    public String getApplovinBannerUnitid() {
        return applovinBannerUnitid;
    }

    public void setApplovinBannerUnitid(String applovinBannerUnitid) {
        this.applovinBannerUnitid = applovinBannerUnitid;
    }

    public String getApplovinInterstitialUnitid() {
        return applovinInterstitialUnitid;
    }

    public void setApplovinInterstitialUnitid(String applovinInterstitialUnitid) {
        this.applovinInterstitialUnitid = applovinInterstitialUnitid;
    }

    @SerializedName("applovin_interstitial")
    @Expose
    private int applovinInterstitial;


    public String getApplovinNativeUnitid() {
        return applovinNativeUnitid;
    }

    public void setApplovinNativeUnitid(String applovinNativeUnitid) {
        this.applovinNativeUnitid = applovinNativeUnitid;
    }

    @SerializedName("applovin_native_unitid")
    @Expose
    private String applovinNativeUnitid;



    @SerializedName("applovin_banner_unitid")
    @Expose
    private String applovinBannerUnitid;


    @SerializedName("applovin_interstitial_unitid")
    @Expose
    private String applovinInterstitialUnitid;

    public int getApplovin_banner() {
        return applovin_banner;
    }

    public String getApplovinRewardUnitid() {
        return applovinRewardUnitid;
    }

    public void setApplovinRewardUnitid(String applovinRewardUnitid) {
        this.applovinRewardUnitid = applovinRewardUnitid;
    }


    public int getApplovinInterstitialShow() {
        return applovinInterstitialShow;
    }

    public void setApplovinInterstitialShow(int applovinInterstitialShow) {
        this.applovinInterstitialShow = applovinInterstitialShow;
    }

    public int getVungle_interstitial_show() {
        return vungle_interstitial_show;
    }

    public void setVungleInterstitialShow(int vungle_interstitial_show) {
        this.vungle_interstitial_show = vungle_interstitial_show;
    }

    @SerializedName("vungle_interstitial_show")
    @Expose
    private int vungle_interstitial_show;

    public int getIronsourceInterstitialShow() {
        return ironsourceInterstitialShow;
    }

    public void setIronsourceInterstitialShow(int ironsourceInterstitialShow) {
        this.ironsourceInterstitialShow = ironsourceInterstitialShow;
    }

    @SerializedName("ironsource_interstitial_show")
    @Expose
    private int ironsourceInterstitialShow;


    public int getAppnextInterstitialShow() {
        return appnextInterstitialShow;
    }

    public void setAppnextInterstitialShow(int appnextInterstitialShow) {
        this.appnextInterstitialShow = appnextInterstitialShow;
    }

    @SerializedName("appnext_interstitial_show")
    @Expose
    private int appnextInterstitialShow;


    @SerializedName("applovin_interstitial_show")
    @Expose
    private int applovinInterstitialShow;

    @SerializedName("applovin_reward_unitid")
    @Expose
    private String applovinRewardUnitid;




    public String getVungleAppid() {
        return vungleAppid;
    }

    public void setVungleAppid(String vungleAppid) {
        this.vungleAppid = vungleAppid;
    }

    @SerializedName("vungle_appid")
    @Expose
    private String vungleAppid;

    public String getWebviewLink() {
        return webviewLink;
    }








    @SerializedName("unity_banner_placement_id")
    @Expose
    private String unityBannerPlacementId;


    @SerializedName("unity_interstitial_placement_id")
    @Expose
    private String unityInterstitialPlacementId;


    public void setVungle_interstitial_show(int vungle_interstitial_show) {
        this.vungle_interstitial_show = vungle_interstitial_show;
    }

    public String getUnityBannerPlacementId() {
        return unityBannerPlacementId;
    }

    public void setUnityBannerPlacementId(String unityBannerPlacementId) {
        this.unityBannerPlacementId = unityBannerPlacementId;
    }

    public String getUnityInterstitialPlacementId() {
        return unityInterstitialPlacementId;
    }

    public void setUnityInterstitialPlacementId(String unityInterstitialPlacementId) {
        this.unityInterstitialPlacementId = unityInterstitialPlacementId;
    }

    public String getUnityRewardPlacementId() {
        return unityRewardPlacementId;
    }

    public void setUnityRewardPlacementId(String unityRewardPlacementId) {
        this.unityRewardPlacementId = unityRewardPlacementId;
    }

    @SerializedName("unity_reward_placement_id")
    @Expose
    private String unityRewardPlacementId;





    @SerializedName("vungle_interstitial_placement_name")
    @Expose
    private String vungleInterstitialPlacementName;

    public String getVungleInterstitialPlacementName() {
        return vungleInterstitialPlacementName;
    }

    public void setVungleInterstitialPlacementName(String vungleInterstitialPlacementName) {
        this.vungleInterstitialPlacementName = vungleInterstitialPlacementName;
    }

    public String getVungleBannerPlacementName() {
        return vungleBannerPlacementName;
    }

    public void setVungleBannerPlacementName(String vungleBannerPlacementName) {
        this.vungleBannerPlacementName = vungleBannerPlacementName;
    }

    public String getVungleRewardPlacementName() {
        return vungleRewardPlacementName;
    }

    public void setVungleRewardPlacementName(String vungleRewardPlacementName) {
        this.vungleRewardPlacementName = vungleRewardPlacementName;
    }

    @SerializedName("vungle_banner_placement_name")
    @Expose
    private String vungleBannerPlacementName;


    @SerializedName("vungle_reward_placement_name")
    @Expose
    private String vungleRewardPlacementName;



    public void setWebviewLink(String webviewLink) {
        this.webviewLink = webviewLink;
    }

    @SerializedName("webview_link")
    @Expose
    private String webviewLink;

    @SerializedName("appnext_placementid")
    @Expose
    private String appnextPlacementid;


    public int getEnableComments() {
        return enableComments;
    }

    public void setEnableComments(int enableComments) {
        this.enableComments = enableComments;
    }

    @SerializedName("appodeal_banner")
    @Expose
    private int appodealBanner;


    public int getEpisodes_style() {
        return episodes_style;
    }

    public void setEpisodes_style(int episodes_style) {
        this.episodes_style = episodes_style;
    }

    @SerializedName("episodes_style")
    @Expose
    private int episodes_style;

    @SerializedName("enable_comments")
    @Expose
    private int enableComments;


    public int getEnablePinned() {
        return enablePinned;
    }

    public void setEnablePinned(int enablePinned) {
        this.enablePinned = enablePinned;
    }

    public int getEnableUpcoming() {
        return enableUpcoming;
    }

    public void setEnableUpcoming(int enableUpcoming) {
        this.enableUpcoming = enableUpcoming;
    }

    @SerializedName("enable_upcoming")
    @Expose
    private int enableUpcoming;

    @SerializedName("enable_pinned")
    @Expose
    private int enablePinned;

    public int getEnablePreviews() {
        return enablePreviews;
    }

    public void setEnablePreviews(int enablePreviews) {
        this.enablePreviews = enablePreviews;
    }

    @SerializedName("enable_previews")
    @Expose
    private int enablePreviews;


    public int getAppodealShowInterstitial() {
        return appodealShowInterstitial;
    }

    public void setAppodealShowInterstitial(int appodealShowInterstitial) {
        this.appodealShowInterstitial = appodealShowInterstitial;
    }

    @SerializedName("appodeal_show_interstitial")
    @Expose
    private int appodealShowInterstitial;



    @SerializedName("ad_banner")
    @Expose
    private int adBanner;


    public int getAdUnitIdNativeEnable() {
        return adUnitIdNativeEnable;
    }

    public void setAdUnitIdNativeEnable(int adUnitIdNativeEnable) {
        this.adUnitIdNativeEnable = adUnitIdNativeEnable;
    }

    @SerializedName("ad_unit_id_native_enable")
    @Expose
    private int adUnitIdNativeEnable;


    @SerializedName("ad_face_audience_interstitial")
    @Expose
    private int adFaceAudienceInterstitial;

    @SerializedName("ad_face_audience_banner")
    @Expose
    private int adFaceAudienceBanner;

    public String getAdUnitIdNative() {
        return adUnitIdNative;
    }

    public void setAdUnitIdNative(String adUnitIdNative) {
        this.adUnitIdNative = adUnitIdNative;
    }


    public String getPaypalCurrency() {
        return paypalCurrency;
    }

    public void setPaypalCurrency(String paypalCurrency) {
        this.paypalCurrency = paypalCurrency;
    }

    @SerializedName("paypal_currency")
    @Expose
    private String paypalCurrency;

    @SerializedName("ad_unit_id_native")
    @Expose
    private String adUnitIdNative;

    @SerializedName("enable_custom_message")
    @Expose
    private int enableCustomMessage;


    public int getEnableCustomBanner() {
        return enableCustomBanner;
    }

    public void setEnableCustomBanner(int enableCustomBanner) {
        this.enableCustomBanner = enableCustomBanner;
    }

    public String getCustomBannerImage() {
        return customBannerImage;
    }

    public void setCustomBannerImage(String customBannerImage) {
        this.customBannerImage = customBannerImage;
    }



    @SerializedName("ironsource_banner")
    @Expose
    private int ironsourceBanner;

    public int getIronsourceBanner() {
        return ironsourceBanner;
    }

    public void setIronsourceBanner(int ironsourceBanner) {
        this.ironsourceBanner = ironsourceBanner;
    }

    public int getIronsourceInterstitial() {
        return ironsourceInterstitial;
    }

    public void setIronsourceInterstitial(int ironsourceInterstitial) {
        this.ironsourceInterstitial = ironsourceInterstitial;
    }

    @SerializedName("ironsource_interstitial")
    @Expose
    private int ironsourceInterstitial;

    @SerializedName("enable_custom_banner")
    @Expose
    private int enableCustomBanner;

    public String getCustomBannerImageLink() {
        return customBannerImageLink;
    }

    public void setCustomBannerImageLink(String customBannerImageLink) {
        this.customBannerImageLink = customBannerImageLink;
    }

    @SerializedName("custom_banner_image_link")
    @Expose
    private String customBannerImageLink;


    @SerializedName("custom_banner_image")
    @Expose
    private String customBannerImage;


    @SerializedName("ad_unit_id_facebook_interstitial_audience")
    @Expose
    private String adUnitIdFacebookInterstitialAudience;

    public int getMantenanceMode() {
        return mantenanceMode;
    }

    public void setMantenanceMode(int mantenanceMode) {
        this.mantenanceMode = mantenanceMode;
    }

    public String getMantenanceModeMessage() {
        return mantenanceModeMessage;
    }

    public void setMantenanceModeMessage(String mantenanceModeMessage) {
        this.mantenanceModeMessage = mantenanceModeMessage;
    }

    @SerializedName("mantenance_mode")
    @Expose
    private int mantenanceMode;

    public String getSplashImage() {
        return splashImage;
    }

    public void setSplashImage(String splashImage) {
        this.splashImage = splashImage;
    }

    @SerializedName("mantenance_mode_message")
    @Expose
    private String mantenanceModeMessage;


    public String getDefaultYoutubeQuality() {
        return defaultYoutubeQuality;
    }

    public void setDefaultYoutubeQuality(String defaultYoutubeQuality) {
        this.defaultYoutubeQuality = defaultYoutubeQuality;
    }

    @SerializedName("default_youtube_quality")
    @Expose
    private String defaultYoutubeQuality;

    public String getDefaultDownloadsOptions() {
        return defaultDownloadsOptions;
    }

    public void setDefaultDownloadsOptions(String defaultDownloadsOptions) {
        this.defaultDownloadsOptions = defaultDownloadsOptions;
    }



    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @SerializedName("header")
    @Expose
    private String header;

    @SerializedName("user_agent")
    @Expose
    private String userAgent;

    @SerializedName("default_downloads_options")
    @Expose
    private String defaultDownloadsOptions;

    @SerializedName("splash_image")
    @Expose
    private String splashImage;


    public String getAdUnitIdAppodealBannerAudience() {
        return adUnitIdAppodealBannerAudience;
    }

    public void setAdUnitIdAppodealBannerAudience(String adUnitIdAppodealBannerAudience) {
        this.adUnitIdAppodealBannerAudience = adUnitIdAppodealBannerAudience;
    }

    public String getAdUnitIdAppodealInterstitialAudience() {
        return adUnitIdAppodealInterstitialAudience;
    }

    public void setAdUnitIdAppodealInterstitialAudience(String adUnitIdAppodealInterstitialAudience) {
        this.adUnitIdAppodealInterstitialAudience = adUnitIdAppodealInterstitialAudience;
    }




    @SerializedName("ad_unit_id_appodeal_banner_audience")
    @Expose
    private String adUnitIdAppodealBannerAudience;

    @SerializedName("ad_unit_id_appodeal_interstitial_audience")
    @Expose
    private String adUnitIdAppodealInterstitialAudience;


    @SerializedName("ad_unit_id_facebook_banner_audience")
    @Expose
    private String adUnitIdFacebookBannerAudience;


    public String getIronsourceAppKey() {
        return ironsourceAppKey;
    }

    public void setIronsourceAppKey(String ironsourceAppKey) {
        this.ironsourceAppKey = ironsourceAppKey;
    }

    @SerializedName("ironsource_app_key")
    @Expose
    private String ironsourceAppKey;



    @SerializedName("ironsource_interstitial_placement_name")
    @Expose
    private String ironsourceInterstitialPlacementName;



    @SerializedName("ironsource_banner_placement_name")
    @Expose
    private String ironsourceBannerPlacementName;

    public String getIronsourceInterstitialPlacementName() {
        return ironsourceInterstitialPlacementName;
    }

    public void setIronsourceInterstitialPlacementName(String ironsourceInterstitialPlacementName) {
        this.ironsourceInterstitialPlacementName = ironsourceInterstitialPlacementName;
    }

    public String getIronsourceBannerPlacementName() {
        return ironsourceBannerPlacementName;
    }

    public void setIronsourceBannerPlacementName(String ironsourceBannerPlacementName) {
        this.ironsourceBannerPlacementName = ironsourceBannerPlacementName;
    }

    public String getIronsourceRewardPlacementName() {
        return ironsourceRewardPlacementName;
    }

    public void setIronsourceRewardPlacementName(String ironsourceRewardPlacementName) {
        this.ironsourceRewardPlacementName = ironsourceRewardPlacementName;
    }

    @SerializedName("ironsource_reward_placement_name")
    @Expose
    private String ironsourceRewardPlacementName;



    @SerializedName("ad_unit_id_banner")
    @Expose
    private String adUnitIdBanner;


    @SerializedName("ad_unit_id_interstitial")
    @Expose
    private String adUnitIdInterstitial;

    public String getPurchaseKey() {
        return purchaseKey;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentkey) {
        this.currentKey = currentkey;
    }

    @SerializedName("current_key")
    @Expose
    private String currentKey;


    @SerializedName("featured_home_numbers")
    @Expose
    private int featuredHomeNumbers;


    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;


    public String getAdUnitIdAppodealRewarded() {
        return adUnitIdAppodealRewarded;
    }

    public void setAdUnitIdAppodealRewarded(String adUnitIdAppodealRewarded) {
        this.adUnitIdAppodealRewarded = adUnitIdAppodealRewarded;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTmdbApiKey() {
        return tmdbApiKey;
    }

    public void setTmdbApiKey(String tmdbApiKey) {
        this.tmdbApiKey = tmdbApiKey;
    }

    public String getCue() {
        return purchaseKey;
    }

    public void setPurchaseKey(String purchaseKey) {
        this.purchaseKey = purchaseKey;
    }

    public String getAppUrlAndroid() {
        return appUrlAndroid;
    }

    public void setAppUrlAndroid(String appUrlAndroid) {
        this.appUrlAndroid = appUrlAndroid;
    }


    public Integer getAnime() {
        return anime;
    }

    public void setAnime(Integer anime) {
        this.anime = anime;
    }

    public int getAds() {
        return ads;
    }

    public void setAds(int ads) {
        this.ads = ads;
    }

    public String getAdAppId() {
        return adAppId;
    }

    public void setAdAppId(String adAppId) {
        this.adAppId = adAppId;
    }

    public int getAdInterstitial() {
        return adInterstitial;
    }

    public void setAdInterstitial(int adInterstitial) {
        this.adInterstitial = adInterstitial;
    }

    public String getAdUnitIdInterstitial() {
        return adUnitIdInterstitial;
    }

    public void setAdUnitIdInterstitial(String adUnitIdInterstitial) {
        this.adUnitIdInterstitial = adUnitIdInterstitial;
    }


    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }


    public int getEnableCustomMessage() {
        return enableCustomMessage;
    }

    public void setEnableCustomMessage(int enableCustomMessage) {
        this.enableCustomMessage = enableCustomMessage;
    }



    public String getAdUnitIdBanner() {
        return adUnitIdBanner;
    }

    public void setAdUnitIdBanner(String adUnitIdBanner) {
        this.adUnitIdBanner = adUnitIdBanner;
    }


    public int getAdBanner() {
        return adBanner;
    }

    public void setAdBanner(int adBanner) {
        this.adBanner = adBanner;
    }


    public int getFacebookShowInterstitial() {
        return facebookShowInterstitial;
    }

    public void setFacebookShowInterstitial(int facebookShowInterstitial) {
        this.facebookShowInterstitial = facebookShowInterstitial;
    }


    public int getAdShowInterstitial() {
        return adShowInterstitial;
    }

    public void setAdShowInterstitial(int adShowInterstitial) {
        this.adShowInterstitial = adShowInterstitial;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }



    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }


    public String getPaypalClientId() {
        return paypalClientId;
    }

    public void setPaypalClientId(String paypalClientId) {
        this.paypalClientId = paypalClientId;
    }


    public String getStripePublishableKey() {
        return stripePublishableKey;
    }

    public void setStripePublishableKey(String stripePublishableKey) {
        this.stripePublishableKey = stripePublishableKey;
    }

    public String getStripeSecretKey() {
        return stripeSecretKey;
    }

    public void setStripeSecretKey(String stripeSecretKey) {
        this.stripeSecretKey = stripeSecretKey;
    }

    public int getAutosubstitles() {
        return autosubstitles;
    }

    public void setAutosubstitles(int autosubstitles) {
        this.autosubstitles = autosubstitles;
    }


    public String getPaypalAmount() {
        return paypalAmount;
    }

    public void setPaypalAmount(String paypalAmount) {
        this.paypalAmount = paypalAmount;
    }


    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }


    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }


    public String getImdbCoverPath() {
        return imdbCoverPath;
    }

    public void setImdbCoverPath(String imdbCoverPath) {
        this.imdbCoverPath = imdbCoverPath;
    }


    public int getFeaturedHomeNumbers() {
        return featuredHomeNumbers;
    }

    public void setFeaturedHomeNumbers(int featuredHomeNumbers) {
        this.featuredHomeNumbers = featuredHomeNumbers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAdFaceAudienceInterstitial() {
        return adFaceAudienceInterstitial;
    }

    public void setAdFaceAudienceInterstitial(int adFaceAudienceInterstitial) {
        this.adFaceAudienceInterstitial = adFaceAudienceInterstitial;
    }

    public int getAdFaceAudienceBanner() {
        return adFaceAudienceBanner;
    }

    public void setAdFaceAudienceBanner(int adFaceAudienceBanner) {
        this.adFaceAudienceBanner = adFaceAudienceBanner;
    }

    public String getAdUnitIdFacebookInterstitialAudience() {
        return adUnitIdFacebookInterstitialAudience;
    }

    public void setAdUnitIdFacebookInterstitialAudience(String adUnitIdFacebookInterstitialAudience) {
        this.adUnitIdFacebookInterstitialAudience = adUnitIdFacebookInterstitialAudience;
    }

    public String getAdUnitIdFacebookBannerAudience() {
        return adUnitIdFacebookBannerAudience;
    }

    public void setAdUnitIdFacebookBannerAudience(String adUnitIdFacebookBannerAudience) {
        this.adUnitIdFacebookBannerAudience = adUnitIdFacebookBannerAudience;
    }


    public String getDefaultRewardedNetworkAds() {
        return defaultRewardedNetworkAds;
    }

    public void setDefaultRewardedNetworkAds(String defaultRewardedNetworkAds) {
        this.defaultRewardedNetworkAds = defaultRewardedNetworkAds;
    }


    public int getWachAdsToUnlock() {
        return wachAdsToUnlock;
    }

    public void setWachAdsToUnlock(int wachAdsToUnlock) {
        this.wachAdsToUnlock = wachAdsToUnlock;
    }


    public String getStartappId() {
        return startappId;
    }

    public void setStartappId(String startappId) {
        this.startappId = startappId;
    }

    public String getAdUnitIdRewarded() {
        return adUnitIdRewarded;
    }

    public void setAdUnitIdRewarded(String adUnitIdRewarded) {
        this.adUnitIdRewarded = adUnitIdRewarded;
    }

    public String getAdUnitIdFacebookRewarded() {
        return adUnitIdFacebookRewarded;
    }

    public void setAdUnitIdFacebookRewarded(String adUnitIdFacebookRewarded) {
        this.adUnitIdFacebookRewarded = adUnitIdFacebookRewarded;
    }

    public String getUnityGameId() {
        return unityGameId;
    }

    public void setUnityGameId(String unityGameId) {
        this.unityGameId = unityGameId;
    }

}