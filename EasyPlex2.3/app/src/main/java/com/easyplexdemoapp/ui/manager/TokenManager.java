package com.easyplexdemoapp.ui.manager;

import android.content.SharedPreferences;

import com.easyplexdemoapp.data.model.auth.Login;

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

public class TokenManager {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // Empty constructor
    public TokenManager() {
        // Initialize fields if necessary
    }

    public TokenManager(SharedPreferences prefs, SharedPreferences.Editor editor) {
        this.prefs = prefs;
        this.editor = editor;
    }

    public void saveToken(Login token) {
        editor.putString(ACCESS_TOKEN, token.getAccessToken()).commit();
        editor.putString(REFRESH_TOKEN, token.getRefresh()).commit();
        editor.apply();
    }

    public void deleteToken() {
        editor.remove(ACCESS_TOKEN).commit();
        editor.remove(REFRESH_TOKEN).commit();
    }

    public Login getToken() {
        Login token = new Login();
        token.setAccessToken(prefs.getString(ACCESS_TOKEN, null));
        token.setRefresh(prefs.getString(REFRESH_TOKEN, null));
        return token;
    }
}