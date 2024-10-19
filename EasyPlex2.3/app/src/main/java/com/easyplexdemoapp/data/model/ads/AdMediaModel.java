package com.easyplexdemoapp.data.model.ads;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyplexdemoapp.data.model.media.MediaModel;

import java.util.List;

/**
 * this is the wrapper class store a representation of AdBreak ojects, could be a list of ad {@link MediaModel}
 */
public class AdMediaModel {

    private final List<MediaModel> listOfAds;

    public AdMediaModel(@NonNull List<MediaModel> listOfAds) {
        this.listOfAds = listOfAds;
    }

    @Nullable
    public MediaModel nextAD() {
        return listOfAds != null && !listOfAds.isEmpty() ? listOfAds.get(0) : null;
    }

    public List<MediaModel> getListOfAds() {
        return listOfAds;
    }

    public void popFirstAd() {
        if (listOfAds != null && !listOfAds.isEmpty()) {
            listOfAds.remove(0);
        }
    }

    public int nubmerOfAd() {
        if (listOfAds == null) {
            return 0;
        } else {
            return listOfAds.size();
        }
    }

}
