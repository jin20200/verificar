package com.easyplexdemoapp.ui.manager;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.easyplexdemoapp.util.Constants.*;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.easyplexdemoapp.BuildConfig;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.settings.BehaviorSettings;
import com.easyplexdemoapp.data.model.settings.Settings;
import com.stringcare.library.SC;

/**
 * EasyPlex - Android Movie Portal App
 * @package     EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2024 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/



public class AppBehaviorManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    public AppBehaviorManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();

    }

    public AppBehaviorManager() {


    }


    public void saveSettings(BehaviorSettings behaviorSettings){

        editor.putBoolean(ENABLE_API_CHECK_HASH_256, behaviorSettings.isHash256()).commit();
        editor.putBoolean(ENABLE_API_AUTH, behaviorSettings.isAuth()).commit();

    }

    public void deleteSettings() {
        editor.remove(ENABLE_API_CHECK_HASH_256).commit();
        // Commit the changes
        editor.apply();
    }


    public BehaviorSettings getSettings(){

        BehaviorSettings behaviorSettings = new BehaviorSettings();
        behaviorSettings.setHash256(prefs.getBoolean(ENABLE_API_CHECK_HASH_256, false));
        behaviorSettings.setAuth(prefs.getBoolean(ENABLE_API_AUTH, false));

        return behaviorSettings;


    }


}
