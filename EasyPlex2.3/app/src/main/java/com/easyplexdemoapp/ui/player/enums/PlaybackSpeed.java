package com.easyplexdemoapp.ui.player.enums;

import android.content.Context;

import com.easyplexdemoapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public enum PlaybackSpeed {

    // Ordering here will effect ordering in UI
    A_QUARTER(R.string.playback_speed_a_quarter, 0.25f),
    A_HALF(R.string.playback_speed_a_half, 0.5f),
    THREE_QUARTER(R.string.playback_speed_three_quarter, 0.75f),
    NORMAL(R.string.playback_speed_normal, 1f),
    ONE_AND_A_QUARTER(R.string.playback_speed_one_and_a_quarter, 1.25f),
    ONE_AND_A_HALF(R.string.playback_speed_one_and_a_half, 1.5f),
    ONE_AND_THREE_QUARTER(R.string.playback_speed_one_and_three_quarter, 1.75f),
    TWO(R.string.playback_speed_two, 2f);

    private static final float EPSILON = 0.01f;

    private final int stringResourceId;
    private final float speedValue;

    PlaybackSpeed(int stringResourceId, float speedValue) {
        this.stringResourceId = stringResourceId;
        this.speedValue = speedValue;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public String getText(Context context) {
        return context.getString(stringResourceId);
    }

    public static PlaybackSpeed getPlaybackSpeedBySpeedValue(Float speedValue) {
        for (PlaybackSpeed playbackSpeed : PlaybackSpeed.getAllPlaybackSpeedEnums()) {
            if(Math.abs(playbackSpeed.speedValue - speedValue) < EPSILON) {
                return playbackSpeed;
            }
        }
        return null;
    }

    public static int getPlaybackSpeedPositionBySpeedValue(Float speedValue) {
        PlaybackSpeed targetPlaybackSpeed = getPlaybackSpeedBySpeedValue(speedValue);
        return getAllPlaybackSpeedEnums().indexOf(targetPlaybackSpeed);
    }

    public float getSpeedValue() {
        return speedValue;
    }

    public static ArrayList<PlaybackSpeed> getAllPlaybackSpeedEnums() {
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(PlaybackSpeed.class.getEnumConstants())));
    }
}
