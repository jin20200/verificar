package com.easyplexdemoapp.ui.users;

import androidx.databinding.ObservableField;

import java.util.Observable;

public class MenuHandler extends Observable {



    public final ObservableField<Boolean> isNotificationCounterEnabled = new ObservableField<>(false);


    public final ObservableField<Boolean> isErrorLoadingPlaylist = new ObservableField<>(false);


    public final ObservableField<Boolean> isRTLenabled = new ObservableField<>(false);

    public final ObservableField<Boolean> isUserRefreshHome = new ObservableField<>(false);

    public final ObservableField<Boolean> isAppRelease = new ObservableField<>(false);

    public final ObservableField<Boolean> isAppDebug = new ObservableField<>(false);

    public final ObservableField<Boolean> isHomeBannerEnabled = new ObservableField<>(false);


    public final ObservableField<Boolean> isSearchhistory = new ObservableField<>(false);

    public final ObservableField<Boolean> isUserHasLogged = new ObservableField<>(false);

    public final ObservableField<Boolean> isDeviceOptionActivated = new ObservableField<>(false);


    public final ObservableField<Boolean> isProfileSettingEnabled = new ObservableField<>(false);


    public final ObservableField<String> manageProfileText = new ObservableField<>("");


    public final ObservableField<Boolean> isPlayerReady = new ObservableField<>(false);


    public final ObservableField<Boolean> isUserCreatingProfile = new ObservableField<>(false);

    public final ObservableField<Boolean> isDevicesLimitRevoked = new ObservableField<>(false);

    public final ObservableField<Boolean> isDevicesLimitReached = new ObservableField<>(false);


    public final ObservableField<Boolean> isNetworkActive = new ObservableField<>(false);


    public final ObservableField<Boolean> isDataLoaded = new ObservableField<>(false);

    public final ObservableField<Boolean> AppReadyToLoadUi = new ObservableField<>(false);

    public final ObservableField<Boolean> isUserHasProfiles = new ObservableField<>(false);


    public final ObservableField<Boolean> isUserEditMode = new ObservableField<>(false);


    public final ObservableField<String> favoriteText = new ObservableField<>("Add To MyList");



    public final ObservableField<Boolean> isLayoutChangeEnabled = new ObservableField<>(false);


}

