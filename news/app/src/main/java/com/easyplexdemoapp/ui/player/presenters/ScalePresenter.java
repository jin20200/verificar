package com.easyplexdemoapp.ui.player.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.enums.ScaleMode;
import com.easyplexdemoapp.util.Tools;

import static android.content.Context.MODE_PRIVATE;
import static com.easyplexdemoapp.EasyPlexApp.getContext;
import static com.easyplexdemoapp.util.Constants.PLAYER_ASPECT_RATIO;
import static com.easyplexdemoapp.util.Constants.PREF_FILE;


public class ScalePresenter {



    final SharedPreferences sharedPreferences;

    private final Context mContext;
    private final PlayerController mUserController;
    private ScaleMode mCurrentScaleMode = ScaleMode.MODE_DEFAULT;
    private final boolean rotate = false;


    public ScalePresenter(Context context, PlayerController userController) {

        mContext = context;
        mUserController = userController;

        sharedPreferences = getContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);


        switch (sharedPreferences.getString(PLAYER_ASPECT_RATIO, "default")) {
            case "16:9":

                doScale(ScaleMode.MODE_16_9);

                break;
            case "default":

                doScale(ScaleMode.MODE_DEFAULT);

                break;
            case "4:3":

                doScale(ScaleMode.MODE_4_3);

                break;
            case "full screen":

                doScale(ScaleMode.MODE_FULL_SCREEN);

                break;
            case "Zoom":

                doScale(ScaleMode.MODE_ZOOMED);

                break;
        }

    }

    public void doScale() {
        ScaleMode nextScaleMode = mCurrentScaleMode.nextMode();
        doScale(nextScaleMode);
        mCurrentScaleMode = nextScaleMode;
    }

    public void doScale(ScaleMode mode) {
        switch (mode) {
            case MODE_DEFAULT:
                float initVideoAspectRatio = mUserController.getInitVideoAspectRatio();
                if (initVideoAspectRatio > 0) {
                    mUserController.setVideoAspectRatio(initVideoAspectRatio);
                }
                break;
            case MODE_4_3:
                mUserController.setVideoAspectRatio((float) 4 / 3);
                break;
            case MODE_FULL_SCREEN:
                mUserController.setVideoAspectRatio(getScreenWidthHeightRatio());
                break;

            default:
                mUserController.setVideoAspectRatio((float) 16 / 9);
                break;


        }
        mUserController.setResizeMode(mode.getMode());
    }

    public ScaleMode getCurrentScaleMode() {
        return mCurrentScaleMode;
    }

    private float getScreenWidthHeightRatio() {

        WindowManager windowManager =
                (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (19 <= Build.VERSION.SDK_INT) {
            // include navigation bar
            display.getRealSize(outPoint);
        } else {
            // exclude navigation bar
            display.getSize(outPoint);


        }

        return (float) outPoint.x / outPoint.y;
    }


    public void lockedClicked(View v){

        Tools.showIn(v);

    }
}
