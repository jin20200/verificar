package com.easyplexdemoapp.ui.manager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.easyplexdemoapp.data.model.auth.Profile;
import com.easyplexdemoapp.data.model.auth.UserAuthInfo;

import static com.easyplexdemoapp.util.Constants.AUTH_AVATAR;
import static com.easyplexdemoapp.util.Constants.AUTH_EMAIL;
import static com.easyplexdemoapp.util.Constants.AUTH_EXPIRED_DATE;
import static com.easyplexdemoapp.util.Constants.AUTH_ID;
import static com.easyplexdemoapp.util.Constants.AUTH_NAME;
import static com.easyplexdemoapp.util.Constants.PREMUIM;
import static com.easyplexdemoapp.util.Constants.PREMUIM_MANUAL;
import static com.easyplexdemoapp.util.Constants.USER_PROFILE_AVATAR;
import static com.easyplexdemoapp.util.Constants.USER_PROFILE_ID;
import static com.easyplexdemoapp.util.Constants.USER_PROFILE_NAME;
import static com.easyplexdemoapp.util.Constants.USER_PROFILE_USER_ID;


/**
 * EasyPlex - Android Movie Portal App
 * @package EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright Copyright (c) 2024 Y0bEX,
 * @license     <a href="http://codecanyon.net/wiki/support/legal-terms/licensing-terms/">...</a>
 * @profile <a href="https://codecanyon.net/user/yobex">...</a>
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/




public class AuthManager {


    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    public AuthManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }

    public void saveSettings(UserAuthInfo userAuthInfo){

        editor.putInt(PREMUIM, userAuthInfo.getPremuim()).commit();
        editor.putInt(PREMUIM_MANUAL, userAuthInfo.getManualPremuim()).commit();
        editor.putString(AUTH_NAME, userAuthInfo.getName()).commit();
        editor.putString(AUTH_EMAIL, userAuthInfo.getEmail()).commit();
        editor.putInt(AUTH_ID, userAuthInfo.getId()).commit();
        editor.putString(AUTH_EXPIRED_DATE, userAuthInfo.getExpiredIn()).commit();
        editor.putString(AUTH_AVATAR, userAuthInfo.getAvatar()).commit();
        editor.apply();
    }


    public void saveSettingsProfile(Profile profile){

        editor.putInt(USER_PROFILE_ID, profile.getId()).commit();
        editor.putString(USER_PROFILE_NAME, profile.getName()).commit();
        editor.putString(USER_PROFILE_AVATAR, profile.getAvatar()).commit();
        editor.putInt(USER_PROFILE_USER_ID, profile.getUser_id()).commit();
        editor.apply();
    }



    public Profile getSettingsProfile(){

        Profile profile = new Profile();
        profile.setId(prefs.getInt(USER_PROFILE_ID, 0));
        profile.setName(prefs.getString(USER_PROFILE_NAME, null));
        profile.setAvatar(prefs.getString(USER_PROFILE_AVATAR, null));
        profile.setUser_id(prefs.getInt(USER_PROFILE_USER_ID, 0));
        return profile;
    }





    public void deleteSettingsProfile(){
        editor.remove(USER_PROFILE_ID).commit();
        editor.remove(USER_PROFILE_NAME).commit();
        editor.remove(USER_PROFILE_AVATAR).commit();
    }



    public void deleteAuth(){
        editor.remove(PREMUIM).commit();
        editor.remove(AUTH_NAME).commit();
        editor.remove(PREMUIM_MANUAL).commit();
        editor.remove(AUTH_ID).commit();
        editor.remove(AUTH_EXPIRED_DATE).commit();
        editor.remove(AUTH_EMAIL).commit();
    }

    public UserAuthInfo getUserInfo() {
        UserAuthInfo userAuthInfo = new UserAuthInfo();
        userAuthInfo.setPremuim(prefs.getInt(PREMUIM, 0));
        userAuthInfo.setManualPremuim(prefs.getInt(PREMUIM_MANUAL, 0));
        userAuthInfo.setName(prefs.getString(AUTH_NAME, null));
        userAuthInfo.setEmail(prefs.getString(AUTH_EMAIL, null));
        userAuthInfo.setId(prefs.getInt(AUTH_ID, 0));
        userAuthInfo.setExpiredIn(prefs.getString(AUTH_EXPIRED_DATE, null));
        return userAuthInfo;
    }


}
