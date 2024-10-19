package com.easyplexdemoapp.ui.manager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import com.easyplexdemoapp.data.model.ads.Ads;
import static com.easyplexdemoapp.util.Constants.ADS_CLICKTHROUGHURL;
import static com.easyplexdemoapp.util.Constants.ADS_CUSTOM;
import static com.easyplexdemoapp.util.Constants.ADS_DURATION;
import static com.easyplexdemoapp.util.Constants.ADS_LINK;
import static com.easyplexdemoapp.util.Constants.CUSTOM_VAST_XML;


/**
 * EasyPlex - Android Movie Portal App
 * @package     EasyPlex - Android Movie Portal App
 * @author      @Y0bEX
 * @copyright   Copyright (c) 2022 Y0bEX,
 * @license     http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile     https://codecanyon.net/user/yobex
 * @link        yobexd@gmail.com
 * @skype       yobexd@gmail.com
 **/




public class AdsManager {



    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public AdsManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }



    public void saveSettings(Ads ads){


        editor.putString(ADS_LINK, ads.getLink()).commit();
        editor.putString(ADS_CLICKTHROUGHURL, ads.getClickThroughUrl()).commit();
        editor.putInt(ADS_CUSTOM, ads.getCustomVast()).commit();
        editor.putString(ADS_DURATION, ads.getDuration()).commit();
        editor.apply();
    }

    public void deleteAds(){
        editor.remove(ADS_LINK).commit();
        editor.remove(ADS_CLICKTHROUGHURL).commit();
    }

    public Ads getAds(){
        Ads ads = new Ads();



        ads.setLink(prefs.getString(ADS_LINK,
                null));
        ads.setClickThroughUrl(prefs.getString(ADS_CLICKTHROUGHURL, null));
        ads.setCustomVast(prefs.getInt(ADS_CUSTOM,
                0));
        ads.setDuration(prefs.getString(ADS_DURATION, null));


        return ads;
    }




}
