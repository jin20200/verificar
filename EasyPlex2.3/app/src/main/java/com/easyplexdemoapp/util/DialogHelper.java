package com.easyplexdemoapp.util;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import static com.easyplexdemoapp.util.Constants.DISABLE_CUSTOM_MESSAGE;
import static com.easyplexdemoapp.util.Constants.WIFI_CHECK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.search.AdapterSuggestionSearch;
import com.easyplexdemoapp.ui.settings.SettingsActivity;
import com.easyplexdemoapp.ui.splash.SplashActivity;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Objects;


public class DialogHelper {



    private static boolean customMessageShow = false;


    private DialogHelper(){


    }


    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    public static boolean checkVpn (){
        String vpn = "";
        try {
            for (NetworkInterface networkInst : Collections.list(NetworkInterface.getNetworkInterfaces())){
                if (networkInst.isUp())
                    vpn = networkInst.getName();
                if ( vpn.contains("tun") || vpn.contains("ppp") || vpn.contains("pptp")) {
                    return true;
                }
            }


        }catch (SocketException e){
            e.printStackTrace();
        }
        return false;
    }


    public static void snifferAppDetectorDialog(@NonNull Context context,String appSnifferName){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sniffer);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = MATCH_PARENT;
        lp.height = MATCH_PARENT;
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> ((SplashActivity) context).finishAffinity());
        dialog.findViewById(R.id.bt_getcode).setOnClickListener(view -> ((SplashActivity) context).finishAffinity());
        TextView snifName = dialog.findViewById(R.id.app_sniffer_name);
        snifName.setText(String.format("%s Detected !", appSnifferName));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




    public static void premuim(@NonNull Context context,String linkUpdate,String version,String updateMessage){




        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_msg);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_getcode).setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(linkUpdate));
            context.startActivity(i);

        });

        TextView message = dialog.findViewById(R.id.app_update_message);
        TextView appVersion = dialog.findViewById(R.id.app_version);


        ImageView imageView = dialog.findViewById(R.id.app_logo);

        message.setText(updateMessage);
        appVersion.setText(version);

        Tools.loadMiniLogo(context,imageView);

        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }






    public static void erroPayment(@NonNull Context context){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error_payment);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;
        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }


    public static void erroLogin(@NonNull Context context){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;
        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }


    public static void erroLogin(@NonNull Context context,String message){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error_login);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView textViewmessage = dialog.findViewById(R.id.message);
        textViewmessage.setText(message);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;
        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }


    // Show  AlertDialog Warning if no stream
    public static void showNoStreamEpisode(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about_no_stream_episode);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public static void showNoDownloadAvailable(@NonNull Context context , String downloaded) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about_no_download);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;
        TextView textViewDownload = dialog.findViewById(R.id.download_message);

        textViewDownload.setText(downloaded);

        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void showNoDownloadAvailableEpisode(@NonNull Context context,String downloaded) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about_no_download_episode);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        TextView textViewDownload = dialog.findViewById(R.id.download_message);

        textViewDownload.setText(downloaded);

        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




    // Show  AlertDialog Warning if no stream
    public static void showNoStreamAvailable(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about_no_stream);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void showNoTrailerAvailable(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_no_trailer);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;



        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }




    public static void  showWifiWarning(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about_wifi);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> {
            context.startActivity(new Intent(context, SettingsActivity.class));
            dialog.dismiss();

        });

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void  showPaypalWarning(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_paypal_warning);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void  showSuggestWarning(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_suggest_warning);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

      dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static void  showPremuimWarning(@NonNull Context context) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_premuim);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

         dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public static void  showAdsFailedWarning(@NonNull Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ads_failed);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

                dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public static void  showCustomAlert(@NonNull Context context, String customMessage) {



        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_alert);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WRAP_CONTENT;
        lp.height = WRAP_CONTENT;

        TextView customAlert = dialog.findViewById(R.id.custom_alert_text);


        dialog.findViewById(R.id.bt_getcode).setOnClickListener(v -> dialog.dismiss());


        dialog.findViewById(R.id.bt_close).setOnClickListener(v ->

       dialog.dismiss());


        customAlert.setText(customMessage);


        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }


}
