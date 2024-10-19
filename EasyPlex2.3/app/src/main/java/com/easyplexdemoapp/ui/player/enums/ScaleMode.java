package com.easyplexdemoapp.ui.player.enums;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

public enum ScaleMode {

    MODE_DEFAULT(RESIZE_MODE_FIT, "default"),
    MODE_4_3(RESIZE_MODE_FIT, "4:3"),
    MODE_16_9(RESIZE_MODE_FIT, "16:9"),
    MODE_FULL_SCREEN(RESIZE_MODE_FILL, "full screen"),
    MODE_ZOOMED (RESIZE_MODE_ZOOM, "Zoom");

    private final int mMode;
    private final int mResId;
    private final String mDescription;

    ScaleMode(int mode, String description) {
        mMode = mode;
        mResId = -1;
        mDescription = description;
    }

    public int getMode() {
        return mMode;
    }

    public int getRestId() {
        return mResId;
    }

    public String getDescription() {
        return mDescription;
    }

    public ScaleMode nextMode() {
        ScaleMode[] modes = ScaleMode.values();
        for (int i = 0; i < modes.length; i++) {
            if (this == modes[i]) {
                return modes[(i+1) % modes.length];
            }
        }
        return MODE_DEFAULT;
    }
}