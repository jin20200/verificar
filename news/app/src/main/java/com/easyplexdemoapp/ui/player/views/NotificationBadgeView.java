package com.easyplexdemoapp.ui.player.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easyplexdemoapp.R;

public class NotificationBadgeView extends FrameLayout {
    private TextView tvBadgeCount;

    public NotificationBadgeView(Context context) {
        this(context, null);
    }

    public NotificationBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_notification_badge, this, true);
        tvBadgeCount = findViewById(R.id.tvBadgeCount);
    }

    public void setCount(int count) {
        tvBadgeCount.setText(String.valueOf(count));
        tvBadgeCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }
}
