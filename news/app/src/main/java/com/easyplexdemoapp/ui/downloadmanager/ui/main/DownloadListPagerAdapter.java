/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class DownloadListPagerAdapter extends FragmentStateAdapter {




    @ViewPager2.OffscreenPageLimit
    public static final int NUM_FRAGMENTS = 2;

    public static final int QUEUED_FRAG_POS = 0;
    public static final int COMPLETED_FRAG_POS = 1;

    public DownloadListPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        /* Stubs */
        switch (position) {
            case QUEUED_FRAG_POS:
                return QueuedDownloadsFragment.newInstance();
            case COMPLETED_FRAG_POS:
                return FinishedDownloadsFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_FRAGMENTS;
    }
}
