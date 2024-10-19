package com.easyplexdemoapp.ui.player.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.UiControllerViewBinding;
import com.easyplexdemoapp.ui.player.bindings.PlayerController;
import com.easyplexdemoapp.ui.player.utilities.ExoPlayerLogger;
import com.google.android.exoplayer2.Player;

public class UIControllerView extends FrameLayout {

    PlayerController playerController;
    private static final String TAG = UIControllerView.class.getSimpleName();
    private static final int TIME_TO_HIDE_CONTROL = 5000;
    private UiControllerViewBinding binding;
    private Handler countdownHandler;

    /**
     *  Hide Controllers (buttons play - forward - back - settings)
     */


    // Lambda Runnable
    final Runnable hideUIAction = () -> {

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(500);
        binding.controllerPanel.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                //

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                binding.controllerPanel.setVisibility(GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {


                //

            }
        });
    };




    public UIControllerView(final Context context) {
        this(context, null);
    }

    public UIControllerView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIControllerView(Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initLayout(context);

    }

    public UIControllerView setPlayerController(PlayerController playerController) {
        if (playerController == null) {
            ExoPlayerLogger.w(TAG, "setUserController()--> param passed in null");
            return null;
        }


        this.playerController = playerController;

        if (binding != null) {
            binding.setController(playerController);


            if (Boolean.TRUE.equals(playerController.isPlayerLocked.get())) {

                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(500);
                binding.playerLockedIcon.startAnimation(alphaAnimation);

                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {


                        //
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //binding.playerLockedIcon.setVisibility(GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //
                    }
                });

            }

        }


        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ExoPlayerLogger.w(TAG, "onDetachedFromWindow");
        countdownHandler.removeCallbacks(hideUIAction);
    }


    private void initLayout(Context context) {

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.ui_controller_view, this, true);

        countdownHandler = new Handler(Looper.getMainLooper());

        new Thread(hideUIAction).start();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        countdownHandler.removeCallbacks(hideUIAction);

        if (binding.unlockBtnSecond.getVisibility() == VISIBLE) {

            playerController.isPlayerLocked2.set(false);

        }

        if (binding.controllerPanel.getVisibility() == VISIBLE) {


            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(500);
            binding.controllerPanel.startAnimation(alphaAnimation);

            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {


                    //
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    binding.controllerPanel.setVisibility(GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    //
                }
            });






        } else {

            if (playerController.playerPlaybackState.get() != Player.STATE_IDLE) {

                binding.controllerPanel.setVisibility(VISIBLE);
                if (!playerController.isUserDraggingSeekBar.get()){
                    hideUiTimeout();
                }
            }


        }



        return super.onTouchEvent(event);
    }


    private void hideUiTimeout() {
        countdownHandler.postDelayed(hideUIAction, TIME_TO_HIDE_CONTROL);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        binding.controllerPanel.startAnimation(alphaAnimation);

    }


}
