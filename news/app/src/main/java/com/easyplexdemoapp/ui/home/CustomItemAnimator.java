package com.easyplexdemoapp.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @Nullable ItemHolderInfo preInfo, @Nullable ItemHolderInfo postInfo) {
        if (preInfo != null && preInfo instanceof CustomItemHolderInfo) {
            CustomItemHolderInfo customPreInfo = (CustomItemHolderInfo) preInfo;
            // Fade out animation for the old item
            oldHolder.itemView.animate()
                    .alpha(0.0f)
                    .setDuration(getChangeDuration())
                    .withEndAction(() -> dispatchChangeFinished(oldHolder, true));
        }

        if (postInfo != null && postInfo instanceof CustomItemHolderInfo) {
            CustomItemHolderInfo customPostInfo = (CustomItemHolderInfo) postInfo;
            // Fade in animation for the new item
            newHolder.itemView.setAlpha(0.0f);
            newHolder.itemView.animate()
                    .alpha(1.0f)
                    .setDuration(getChangeDuration())
                    .withEndAction(() -> dispatchChangeFinished(newHolder, true));
        }

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo);
    }


    @NonNull
    @Override
    public ItemHolderInfo obtainHolderInfo() {
        return super.obtainHolderInfo();
    }

    private static class CustomItemHolderInfo extends ItemHolderInfo {
        // Custom information about item changes can be added here
    }
}
