package com.easyplexdemoapp.ui.player.utilities;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.player.activities.EasyPlexPlayerActivity;
import com.easyplexdemoapp.ui.player.enums.PlaybackSpeed;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlaybackSettingMenu {

    private WeakReference<ExoPlayer> contentPlayerRef;
    private WeakReference<EasyPlexPlayerActivity> activityRef;
    private AlertDialog mainDialog;

    public PlaybackSettingMenu() {
    }

    public PlaybackSettingMenu(@NonNull ExoPlayer contentPlayer, @NonNull EasyPlexPlayerActivity playerActivity) {
        this.contentPlayerRef = new WeakReference<>(contentPlayer);
        this.activityRef = new WeakReference<>(playerActivity);
    }

    public void setContentPlayer(@NonNull ExoPlayer contentPlayer) {
        this.contentPlayerRef = new WeakReference<>(contentPlayer);
    }

    public void setActivity(@NonNull EasyPlexPlayerActivity activity) {
        this.activityRef = new WeakReference<>(activity);
    }

    public void buildSettingMenuOptions() {
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        MenuOption playbackSpeedOption = new MenuOption(getActivity().getString(
                R.string.playback_setting_speed_title), new MenuOptionCallback() {
            @Override
            public void onClick() {
                showPlaybackSpeedDialog();
            }

            @Override
            public String getTitle(String defaultTitle) {
                ExoPlayer player = getContentPlayer();
                if (player != null) {
                    Float currentSpeedValue = player.getPlaybackParameters().speed;
                    PlaybackSpeed currentPlaybackSpeed = PlaybackSpeed.getPlaybackSpeedBySpeedValue(currentSpeedValue);
                    if (currentPlaybackSpeed != null) {
                        defaultTitle = defaultTitle + " - " + currentPlaybackSpeed.getText(getActivity());
                    }
                }
                return defaultTitle;
            }
        });

        menuOptions.add(playbackSpeedOption);
        // Additional menu options can be added here
    }

    public void show() {
        showPlaybackSpeedDialog();
    }

    private void showPlaybackSpeedDialog() {
        EasyPlexPlayerActivity activity = getActivity();
        ExoPlayer contentPlayer = getContentPlayer();
        if (activity == null || contentPlayer == null || activity.isFinishing()) {
            return;
        }

        ArrayList<String> playbackSpeedTexts = new ArrayList<>();
        ArrayList<Float> playbackSpeedValues = new ArrayList<>();

        for (PlaybackSpeed playbackSpeed : PlaybackSpeed.getAllPlaybackSpeedEnums()) {
            playbackSpeedTexts.add(playbackSpeed.getText(activity));
            playbackSpeedValues.add(playbackSpeed.getSpeedValue());
        }

        String[] speedOptionTextArray = playbackSpeedTexts.toArray(new String[0]);
        int currentSpeedPosition = PlaybackSpeed.getPlaybackSpeedPositionBySpeedValue(
                contentPlayer.getPlaybackParameters().speed);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setSingleChoiceItems(
                speedOptionTextArray,
                currentSpeedPosition,
                (dialog, i) -> {
                    ExoPlayer player = getContentPlayer();
                    EasyPlexPlayerActivity act = getActivity();
                    if (player == null || act == null || act.isFinishing()) {
                        dialog.dismiss();
                        return;
                    }
                    PlaybackParameters originParameters = player.getPlaybackParameters();
                    PlaybackParameters updatedSpeedParameters = new PlaybackParameters(
                            playbackSpeedValues.get(i),
                            originParameters.pitch
                    );

                    player.setPlaybackParameters(updatedSpeedParameters);

                    act.getPlayerController().getCurrentSpeed("Speed (" + playbackSpeedTexts.get(i) + ")");
                    dialog.dismiss();
                });

        mainDialog = builder.create();
        setAlertDialogGravityBottomCenter(mainDialog);
        alertDialogImmersiveShow(mainDialog);
    }

    public void dismiss() {
        if (mainDialog != null) {
            mainDialog.dismiss();
            mainDialog = null;
        }
    }

    private void setAlertDialogGravityBottomCenter(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.getWindow() != null) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams layoutParams = alertDialog.getWindow().getAttributes();
            if (layoutParams != null) {
                layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
                alertDialog.getWindow().setAttributes(layoutParams);
            }
        }
    }

    private void alertDialogImmersiveShow(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.getWindow() != null) {
            EasyPlexPlayerActivity activity = getActivity();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            alertDialog.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            alertDialog.show();

            alertDialog.getWindow().getDecorView().setSystemUiVisibility(
                    activity.getWindow().getDecorView().getSystemUiVisibility());
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
    }

    private ExoPlayer getContentPlayer() {
        return contentPlayerRef != null ? contentPlayerRef.get() : null;
    }

    private EasyPlexPlayerActivity getActivity() {
        return activityRef != null ? activityRef.get() : null;
    }

    @Override
    protected void finalize() throws Throwable {
        dismiss();
        super.finalize();
    }

    interface MenuOptionCallback {
        void onClick();
        String getTitle(String defaultTitle);
    }

    static class MenuOption {
        private final String title;
        private final MenuOptionCallback callback;

        MenuOption(String title, MenuOptionCallback callback) {
            this.title = title;
            this.callback = callback;
        }

        void onClick() {
            callback.onClick();
        }

        String getTitle() {
            return callback.getTitle(title);
        }
    }

    public void destroy() {
        dismiss();
        contentPlayerRef = null;
        activityRef = null;
    }
}