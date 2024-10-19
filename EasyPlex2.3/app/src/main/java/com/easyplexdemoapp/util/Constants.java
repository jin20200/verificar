package com.easyplexdemoapp.util;

import android.util.Base64;

import com.stringcare.library.SC;

import java.nio.charset.StandardCharsets;

import static com.easyplexdemoapp.BuildConfig.APP_STARUP;

/**
 * Application CONSTANTS.
 *
 * @author Yobex.
 */
public abstract class Constants {

    private Constants(){


    }


    // Enable this to test if your app link implementation is correct or not
    // when using this settings if the dialog appear on the home page check if your selected domain is under verified domains
    public static boolean ENABLE_APP_Link_TEST_WARNING = false;

    // Enable this to able to get the signature for the release apk or aab button inside the app setting

    // when you get the signature please turn if off

    public static boolean ENABLE_SIGNATURE_RELEASE = false;


    public static final String APP_DEFAULT_LANG = "en";



    //
    public static final String SERVER_FIREBASE_VALUE = "main_api";



    // Enable support for the firebase Remote Config
    public static boolean FIREBASECONFIG = false;

    // Enter the key from your firebase remote config value


    // this is your laravel Main Server Api Endpoint
    // for ex https://easyplex.yobdev.live/public/api/          !note : url must be ending with /

    public static final String SERVER_ENCODED = SC.obfuscate(Tools.encodeMainApiServer("http://yourdomain.com/public/api/"));

    // Your Purchase key from codecanyon
    public static final String PURCHASE_KEY = SC.obfuscate("");


    // Enter your Google Client Id
    public static final String GOOGLE_CLIENT_ID = SC.obfuscate("xxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com");

    // this is your Authorisation Bearer to access to your api inside the app without it all requests will be rejected
    // if not Authorisation Header is correctly passed
    // encode this using the base64
    // add the correct non encrypted key to your .env file in the TOKEN field

    // for ex : if you are using a key like that :  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
    // this is non ecrypted key to use inside the .env file
    // but here in the AUTHORISATION_BEARER value you must enter the encoded value
    // take a look at the upgrade 1.5 page http://demo.yobdev.live/upgrade1.5/
    // if you don't want to work with the Authorisation Bearer you can just download the AuthenticateOptionally.php file from the upgrade 1.5 and you can skip this option
    public static final String AUTHORISATION_BEARER = SC.obfuscate("QXVITElSUjgyTXZyZFRUZWFRS1V4ZEE3bWxOdWswV0Q2Tm5YMmZmcG4wd3FlTVA1endrQ0NsT0hDbFJJYkNGZg==");



    // this is not your laravel api , this key will be used to authorise all api calls inside your app ( this key must be the same as API_KEY value in your .env file otherwise you will get
    //  "Invalid access key" error in your api calls )
    // for ex http://192.168.1.130/public/api/movies/featured/p2lbgWkFrykA4QyUmpHihzmc5BNzIABq

    public static final String API_KEY =  SC.obfuscate("p2lbgWkFrykA4QyUmpHihzmc5BNzIABq");


    // Don't Change
    public static final String IMDB_ENCODED = "aHR0cHM6Ly9hcGkudGhlbW92aWVkYi5vcmcvMy8=";


    // Don't Change
    public static final String SUBS_ENCODED = "aHR0cHM6Ly9yZXN0Lm9wZW5zdWJ0aXRsZXMub3JnLw==";

    // Don't Change
    public static final String HXFILE_ENCODED = SC.obfuscate("aHR0cDovL2h4ZmlsZS5jby9hcGkvZmlsZS8=");

    // Don't Change
    public static final String IMDB_BASE_URL = decodeImdbApi();


    // Don't Change
    public static final String SERVER_BASE_URL = decodeServerMainApi();


    // Don't Change
    public static final String SERVER_OPENSUBS_URL = decodeSubsApi();


    // Don't Change
    public static final String AUTHORISATION_BEARER_STRING = decodeAuthorisationBearer();


    // Don't Change
    public static final String HXFILE = decodeHXFILE();



    public static final String VIDSRC_BASE_URL = "https://vidsrc.xyz/embed/";



    // Don't Change
    private static String decodeServerMainApi(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(SC.reveal(SERVER_ENCODED).getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }







    // Don't Change
    private static String decodeAuthorisationBearer() {

        byte[] valueDecoded;
        valueDecoded = Base64.decode(SC.reveal(AUTHORISATION_BEARER).getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    // Don't Change
    private static String decodeImdbApi(){

        byte[] valueDecoded;
        valueDecoded = Base64.decode(IMDB_ENCODED.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    // Don't Change
    private static String decodeSubsApi(){

        byte[] valueDecoded;
        valueDecoded = Base64.decode(SUBS_ENCODED.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


    // Don't Change
    private static String decodeHXFILE(){

        byte[] valueDecoded;
        valueDecoded = Base64.decode(SC.reveal(HXFILE_ENCODED).getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }




    public static final int RADUIS = 5;
    public static final int SAMPLING = 2;



    public static final String MOVIE ="MOVIE";
    public static final String SERIE ="SERIE";
    public static final String EMPTY_URL ="about:blank";
    public static final String PAYPAL_CLIENT_ID = "clientid";
    public static final String PAYMENT = "payment";
    public static final String SUBS_SIZE = "subs_size";
    public static final String SUBS_DEFAULT_LANG = "subs_default_lang";


    public static final String SUBS_DEFAULT_LANG_NAME = "English";
    public static final String SUBS_BACKGROUND = "subs_background";
    public static final String PLAYER_ASPECT_RATIO = "player_aspect_ratio";
    public static final String CACHE_CONTROL = "Cache-Control";


    public static final String ARG_MOVIE = "movie";
    public static final String ARG_CAST= "cast";

    public static final String ARG_COMMENT= "user";
    public static final String ARG_MOVIE_HISTORY = "history";
    public static final String ARG_PAYMENT = "payment";
    public static final String SUBSCRIPTIONS = "You Subscription has ended !";
    public static final String MOVIE_LINK = "link";


    // Buttons Switch Constants
    public static final String WIFI_CHECK = "wifi_check";
    public static final String SWITCH_PUSH_NOTIFICATION = "switch_push_notification";
    public static final String AUTO_PLAY = "autoplay_check";
    public static final String EXTENTIONS = "enable_extentions";
    public static final String SOFTWARE_EXTENTIONS = "enable_software_extentions";


    public static final String DISABLE_CUSTOM_MESSAGE = "disable_custom_message";


    public static final String CUSTOM_MESSAGE = "custom_message";


    // Auth Constants

    public static final String USER_PROFILE_ID = "profile_id";
    public static final String USER_PROFILE_NAME = "profile_name";

    public static final String USER_PROFILE_AVATAR = "profile_avatar";

    public static final String USER_PROFILE_USER_ID = "user_id";


    public static final String PREMUIM = "premuim";
    public static final String PREMUIM_MANUAL = "premuim_manual";
    public static final String AUTH_NAME = "name";

    public static final String AUTH_AVATAR= "avatar";

    public static final String AUTH_EMAIL = "email";
    public static final String AUTH_ID = "id";
    public static final String AUTH_EXPIRED_DATE = "expired_in";
    public static final String ERROR = "Error";


    // Ads Constants
    public static final String ADS_LINK = "link";

    public static final String ADS_CUSTOM = "customVast";

    public static final String ADS_DURATION = "duration";


    public static final String ADS_CLICKTHROUGHURL = "clickThroughUrl";



    // Devices
    public static final String DEVICE_NAME = "name";

    public static final String DEVICE_SERIAL = "serial_number";


    public static final String DEVICE_MODEL = "model";


    // Admob



    // Remote
    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT = "Accept";

    // Substitles


    public static final String ZIP_FILE_NAME4 = "1.ass";
    public static final String ZIP_FILE_NAME = "1.srt";
    public static final String ZIP_FILE_NAME2 = "1.vtt";
    public static final String ZIP_FILE_NAME3 = "1.ssa";
    public static final String SUBSTITLE_LOCATION = "file:///storage/emulated/0/Android/data/";
    public static final String SUBSTITLE_SUB_FILENAME_ZIP = "/subs.zip";


    // Settings Constants
    public static final String APP_VERSION = "latestVersion";
    public static final String APP_NAME = "app_name";
    public static final String AD_FACEBOOK_INTERSTITIAL_SHOW = "facebook_show_interstitial";
    public static final String AD_FACEBOOK_NATIVE_ENABLE = "ad_face_audience_native";
    public static final String AD_FACEBOOK_NATIVE_UNIT_ID = "ad_unit_id_facebook_native_audience";
    public static final String AD_INTERSTITIAL_SHOW = "ad_show_interstitial";
    public static final String AD_INTERSTITIAL = "ad_interstitial";
    public static final String AD_INTERSTITIAL_UNIT = "ad_unit_id_interstitial";
    public static final String AD_BANNER = "ad_banner";
    public static final String AD_BANNER_UNIT = "ad_unit_id_banner";
    public static final String TMDB = "tmdb_api_key";
    public static final String APP_URL_ANDROID = "app_url_android";
    public static final String PRIVACY_POLICY = "privacy_policy";
    public static final String LATEST_VERSION = "latestVersion";
    public static final String UPDATE_TITLE = "update_title";
    public static final String RELEASE_NOTES = "releaseNotes";
    public static final String PAYPAL_AMOUNT = "paypal_amount";
    public static final String FEATURED_HOME_NUMBERS = "featured_home_numbers";
    public static final String IMDB_COVER_PATH = "imdb_cover_path";
    public static final String AUTOSUBSTITLES = "autosubstitles";
    public static final String ANIME = "anime";
    public static final String ENABLE_STREAMING = "streaming";
    public static final String ADS_SETTINGS = "ads";
    public static final String AD_INTERSTITIAL_FACEEBOK_ENABLE = "ad_face_audience_interstitial";
    public static final String AD_INTERSTITIAL_FACEEBOK_UNIT_ID = "ad_unit_id_facebook_interstitial_audience";
    public static final String AD_INTERSTITIAL_APPODEAL_UNIT_ID = "ad_unit_id__appodeal_rewarded";
    public static final String AD_BANNER_APPODEAL_UNIT_ID = "ad_unit_id_appodeal_banner_audience";
    public static final String AD_NATIVEADS_ADMOB_UNIT_ID = "admob_native_ads";
    public static final String AD_NATIVEADS_ADMOB_ENABLE = "ad_unit_id_native_enable";
    public static final String PAYPAL_CURRENCY = "paypal_currency";
    public static final String AD_INTERSTITIAL_APPOBEAL_ENABLE = "ad_unit_id_native";
    public static final String AD_INTERSTITIAL_APPOBEAL_SHOW = "appodeal_show_interstitial";
    public static final String DEFAULT_PAYMENT = "default_payment";
    public static final String AD_BANNER_FACEEBOK_ENABLE = "ad_face_audience_banner";
    public static final String AD_BANNER_FACEEBOK_UNIT_ID = "ad_unit_id_facebook_banner_audience";
    public static final String DEFAULT_NETWORK = "default_network";
    public static final String DEFAULT_NETWORK_PLAYER = "default_network_player";
    public static final String STARTAPP_ID = "startapp_id";
    public static final String ADMOB_REWARD = "ad_unit_id_rewarded";
    public static final String FACEBOOK_REWARD = "ad_unit_id__facebook_rewarded";
    public static final String UNITY_GAME_ID = "unity_game_id";
    public static final String WATCH_ADS_TO_UNLOCK = "wach_ads_to_unlock";
    public static final String WATCH_ADS_TO_UNLOCK_PLAYER = "wach_ads_to_unlock_player";
    public static final String ENABLE_CUSTOM_MESSAGE = "enable_custom_message";
    public static final String STRIPE_PUBLISHABLE_KEY = "stripe_publishable_key";
    public static final String STRIPE_SECRET_KEY = "stripe_secret_key";
    public static final String APPODEAL_REWARD = "ad_unit_id__appodeal_rewarded";
    public static final String APPODEAL_BANNER = "appodeal_banner";
    public static final String DOWNLOADS_PREMUIM_ONLY = "download_premuim_only";
    public static final String NEXT_EPISODE_TIMER = "next_episode_timer";
    public static final String FACEBOOK = "facebook";
    public static final String TWITTER = "twitter";
    public static final String INSTAGRAM = "instagram";
    public static final String YOUTUBE = "youtube";
    public static final String ENABLE_SERVER_DIALOG_SELECTION = "server_dialog_selection";
    public static final String ENABLE_CUSTOM_BANNER = "enable_custom_banner";
    public static final String CUSTOM_BANNER_IMAGE = "custom_banner_image";
    public static final String CUSTOM_BANNER_IMAGE_LINK = "custom_banner_image_link";
    public static final String MANTENANCE_MESSAGE = "mantenance_mode_message";
    public static final String MANTENANCE_MODE = "mantenance_mode";
    public static final String SPLASH_IMAGE = "splash_image";
    public static final String DEFAULT_YOUTUBE_QUALITY = "default_youtube_quality";
    public static final String ALLOW_ADM_DOWNLOADS = "allow_adm";
    public static final String DEFAULT_DOWNLOADS_OPTION = "default_downloads_options";
    public static final String STARTAPP_BANNER = "startapp_banner";
    public static final String STARTAPP_INTER = "startapp_interstitial";
    public static final String VLC = "vlc";
    public static final String OFFLINE_RESUME = "resume_offline";
    public static final String PINNED = "enable_pinned";
    public static final String UPCOMING = "enable_upcoming";
    public static final String PREVIEWS = "enable_previews";
    public static final String USER_AGENT = "user_agent";
    public static final String UNITYADS_BANNER = "unityads_banner";
    public static final String UNITYADS_INTER = "unityads_interstitial";
    public static final String ENABLE_BOTTOM_ADS_HOME = "enable_banner_bottom";
    public static final String DEFAULT_MEDIA_COVER = "default_media_placeholder_path";
    public static final String DEFAULT_DOWNLOAD_DIRECTORY = "cache";
    public static final String ENABLE_WEBVIEW = "enable_webview";
    public static final String ENABLE_LEFT_NAVBAR = "leftnavbar";
    public static final String ENABLE_FAVONLINE = "favoriteonline";
    public static final String TRAILER_OPTIONS = "default_trailer_default";
    public static final String NOTIFICATION_SEPARATED = "notification_separated";
    public static final String PACKAGE_NAME_ANDROID_APP = "app_packagename";
    public static final String DEFAULT_CAST_OPTION = "default_cast_option";
    public static final String DOWNLOAD_SEPARATED = "separate_download";
    public static final String DOWNLOAD_ENABLE = "enable_media_download";
    public static final String ENABLE_VPN_DETECTION = "vpn";
    public static final String ENABLE_ROOT_DETECTION = "root_detection";
    public static final String ENABLE_DIRECT_STREAM_FROM_NOTIFICATION = "notification_style";
    public static final String APPNEXT_BANNER = "appnext_banner";
    public static final String APPNEXT_INTERSTITIAL = "appnext_interstitial";
    public static final String APPNEXT_PLACEMENT_ID = "appnext_placementid";
    public static final String APPNEXT_INTERSTITIAL_SHOW = "appnext_interstitial_show";
    public static final String LIVE_MULTI_SERVERS = "livetv_multi_servers";
    public static final String ENABLE_FORCE_LOGIN = "force_login";
    public static final String FORCE_SUGGEST_AUTH_USERS = "suggest_auth";
    public static final String NETWORKS = "networks";
    public static final String WEBVIEW_LINK_REWARD = "webview_link";
    public static final String VUNGLE_APP_ID = "vungle_appid";
    public static final String VUNGLE_BANNER_ENABLE = "vungle_interstitial";
    public static final String VUNGLE_INTERSTITIAL_ENABLE = "appnext_interstitial";
    public static final String VUNGLE_INTERSTITIAL_PLACEMENT_ID = "appnext_placementid";
    public static final String VUNGLE_BANNER_PLACEMENT_ID = "appnext_placementid";
    public static final String VUNGLE_REWARDS_PLACEMENT_ID = "appnext_placementid";
    public static final String VUNGLE_INTERSTITIAL_SHOW = "vungle_interstitial_show";
    public static final String ENABLE_FLAG_SECURE = "enable_networks";
    public static final String FORCE_EMAIL_CONFIRMATION = "email_verify";
    public static final String FORCE_UPDATE = "force_update";
    public static final String HXFILE_KEY = "hxfile_api_key";
    public static final String SEASONS_STYLE = "season_style";
    public static final String UNITY_SHOW_FREQUENCY = "unity_show";
    public static final String FORCE_PASSWORD_ACCESS = "force_password_access";
    public static final String FORCE_INAPPUPDATE = "force_inappupdate";
    public static final String NETWORKS_LAYOUT_OPTIONS = "default_layout_networks";

    public static final String APPLOVIN_BANNER_ENABLE = "applovin_banner";
    public static final String APPLOVIN_BANNER_UNIT_ID = "applovin_banner_unitid";
    public static final String APPLOVIN_ENABLE_INTERSTITIAL = "applovin_interstitial";
    public static final String APPLOVIN_INTERSTITIAL_UNIT_ID = "applovin_interstitial_unitid";
    public static final String APPLOVIN_REWARD_UNIT_ID = "applovin_reward_unitid";
    public static final String APPLOVIN_NATIVE_ENABLE = "applovin_banner";
    public static final String APPLOVIN_NATIVE_UNIT_ID = "applovin_native_unitid";
    public static final String APPLOVIN_INTERSTITIAL_SHOW = "applovin_interstitial_show";
    public static final String UNITY_REWARD_UNIT_ID = "unity_banner_placement_id";
    public static final String UNITY_BANNER_UNIT_ID = "unity_interstitial_placement_id";
    public static final String UNITY_INTERSTITIAL_UNIT_ID = "unity_reward_placement_id";

    public static final String IRONSOURCE_APPKEY = "ironsource_app_key";
    public static final String IRONSOURCE_INTERSTITIAL_ENABLED = "ironsource_interstitial";
    public static final String IRONSOURCE_BANNER_ENABLED = "ironsource_banner";

    public static final String IRONSOURCE_BANNER_UNIT_ID = "ironsource_banner_placement_name";
    public static final String IRONSOURCE_INTERSTITIAL_UNIT_ID = "ironsource_interstitial_placement_name";
    public static final String IRONSOURCE_REWARD_UNIT_ID = "ironsource_reward_placement_name";
    public static final String IRONSOURCE_INTERSTITIAL_SHOW = "ironsource_interstitial_show";


    public static final String DEVICE_LIMITATION = "device_management_limit";

    public static final String SLIDER_TIMER = "slidertimer";


    public static final String USERPROFILE_LIMIT = "profile_selection_limit";

    public static final String LAYOUT_CHANGE = "enablelayoutchange";

    public static final String ENABLE_DYNAMIC_SLIDER = "enabledynamicslider";

    public static final String ENABLE_SOCIAL_LOGIN = "enablesociallogins";


    public static final String WORTISE_APPKEY = "wortise_appid";
    public static final String WORTISE_INTERSTITIAL_ENABLED = "wortise_interstitial";

    public static final String WORTISE_BANNER_ENABLED = "wortise_banner_enable";

    public static final String WORTISE_PLACEMENT_UNITID = "wortise_banner";


    public static final String WORTISE_BANNER_UNITID = "wortise_banner_unitid";

    public static final String WORTISE_REWARD_UNITID = "wortise_reward_unitid";



    public static final String WORTISE_INTERSTITIAL_SHOW = "wortise_interstitial_show";


    public static final String ENABLE_LANGS_SERVER_SELECTION = "enablelangsinservers";

    public static final String ENABLE_COMMENTS = "enable_comment";
    public static final String SUBSTITLE_DEFAULT_OPTIONS = "default_substitle_option";
    public static final String ENABLE_PLAYER_INTER_EXIST = "enable_player_inter";
    public static final String ENABLE_POSTERS_SHADOW = "enable_shadows";


    public static final String DISABLE_LOGIN = "enablecustomcontentgenre";


    public static final String DEFAULT_NOTIFICATION_API = "default_notification";


    public static final String ENABLE_COLLECTIONS = "enable_collections";
    public static final String VIDSRC = "vidsrc";


    public static final String NOTIFICATION_COUNTER = "notification_counter";


    public static final String ENABLE_M3U_PLAYLIST = "m3uplaylist";

    public static final String M3U_PLAYLIST_PATH = "m3uplaylistpath";


    public static final String FORCE_API_AUTH = "fore_api_auth";


    public static final String ENABLE_CUSTOM_HOME_CONTENT = "enablecustomcontent";


    public static final String ENABLE_RTL = "rtl";

    public static final String ENABLE_DISCOVER_STYLE = "discover_style";

    public static final String LIBRARY_STYLE = "library_style";

    public static final String PROFILE_SELECTION = "profile_selection";

    public static final String ALLOW_ALL_CERTIFICATS = "trustAllCerts";


    public static final String DEVICE_MANAGEMENT = "device_management";


    public static final String ISUSER_MAIN_ACCOUNT = "main_account";

    public static final String FORCE_WATCH_BY_AUTH = "forwatchbyauth";


    public static final String ENABLE_SAFE_MODE = "safemode";

    public static final String ENABLE_WATCH_IN_YOUR_LANG = "enable_watchinyourlang";

    public static final String ENABLE_SUBS_MERGE = "mergesubs";

    public static final String SEARCH_HISTORY = "searchhistory";

    public static final String INTERNAL_LANG = "internallangs";

    public static final String ENABLE_MEDIA_STREAM = "enable_stream";


    public static final String ENABLE_MEDIA_DOWNLOAD = "enable_download";

    public static final String PHONE_VERIFICATION = "phone_verification";

    // Status
    public static final String CODE = "code";


    public static String PLAYER_HEADER = "";
    public static String PLAYER_USER_AGENT = "EasyPlex";
    public static String APP_PASSWORD = "";



    public static final String ENABLE_API_CHECK_HASH_256 = "ENABLE_API_CHECK_HASH_256";


    public static final String ENABLE_API_AUTH = "ENABLE_API_AUTH";



    // TV-SERIES
    public static final String SPECIALS = "Specials";
    public static final String SEASONS = "Seasons: ";
    public static final int UNITY_ADS_BANNER_WIDTH = 320;
    public static final int UNITY_ADS_BANNER_HEIGHT = 50;
    public static final String WEBVIEW = "webview";


    // Player Constants
    public static final long DEFAULT_WEBVIEW_ADS_RUNNING = 10000;
    public static final int CUSTOM_SEEK_CONTROL_STATE = 2; // Every time long press left/right will enter this state
    public static final int EDIT_CUSTOM_SEEK_CONTROL_STATE = 3; // After long press left/right will enter this state
    public static final long DEFAULT_FREQUENCY = 1000;
    public static final String FSMPLAYER_TESTING = "FSM_LOGGING";
    public static final String UPNEXT = "Up Next in : ";



    public static final String EP = "EP";
    public static final String S0 = "S0";
    public static final String E = "E";
    public static final String STREAMING = "streaming";
    public static final String BRX = "brx";
    public static final String TRANSPARENT = "Transparent";
    public static final String FIRST_INSTALL = "first_install";

    public static final String CUSTOM_DIALOG_MESSAGE = "custom_message";

    public static final String FIRST_PASSWORD_CHECK = "first_password_check";


    // Shared Preferences Constants
    public static final String PREF_FILE = "Preferences";
    public static final String AUTHORIZATION = "Authorization";
    public static final String PACKAGE_NAME = "packagename";
    public static final String BEARER = "Bearer ";



    public static final String VLC_INTENT = "market://details?id=org.videolan.vlc";

    public static final String MAXPLAYER_INTENT = "com.mxtech.videoplayer.ad";
    public static final String VLC_PACKAGE_NAME = "org.videolan.vlc";




    public static String CUSTOM_VAST_XML = "";


    public static final String PIP_ACTION_PAUSE = "pause";
    public static final String PIP_ACTION_PLAY = "play";
    public static final String PIP_ENABLE_KEY = "pipEnable";
    public static final int PIP_NUMERATOR_DEFAULT = 4;
    public static final int PIP_DENOMINATOR_DEFAULT = 3;
    public static final boolean PIP_ENABLE_VALUE_DEFAULT = true;


}
