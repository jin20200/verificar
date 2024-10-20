package com.easyplexdemoapp.ui.manager;

import static com.easyplexdemoapp.util.Constants.ADS_CLICKTHROUGHURL;
import static com.easyplexdemoapp.util.Constants.ADS_CUSTOM;
import static com.easyplexdemoapp.util.Constants.ADS_DURATION;
import static com.easyplexdemoapp.util.Constants.ADS_LINK;
import static com.easyplexdemoapp.util.Constants.DEVICE_MODEL;
import static com.easyplexdemoapp.util.Constants.DEVICE_NAME;
import static com.easyplexdemoapp.util.Constants.DEVICE_SERIAL;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.easyplexdemoapp.data.model.Device;
import com.easyplexdemoapp.data.model.ads.Ads;


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




public class DeviceManager {



    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public DeviceManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }



    public void saveSettings(Device device){

        editor.putString(DEVICE_NAME, device.getName()).commit();
        editor.putString(DEVICE_SERIAL, device.getSerialNumber()).commit();
        editor.putString(DEVICE_MODEL, device.getModel()).commit();
        editor.apply();
    }

    public void deleteAds(){
        editor.remove(ADS_LINK).commit();
        editor.remove(ADS_CLICKTHROUGHURL).commit();
    }

    public Device getDevice(){


        Device device = new Device();
        prefs.getString(DEVICE_NAME,device.getName());
        prefs.getString(DEVICE_SERIAL,device.getSerialNumber());
        prefs.getString(DEVICE_MODEL,device.getModel());
        return device;
    }




}
