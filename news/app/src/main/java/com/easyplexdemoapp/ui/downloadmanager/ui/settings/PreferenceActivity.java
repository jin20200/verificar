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

package com.easyplexdemoapp.ui.downloadmanager.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.AppearanceSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.BehaviorSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.BrowserSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.LimitationsSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.StorageSettingsFragment;

public class PreferenceActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = PreferenceActivity.class.getSimpleName();

    public static final String TAG_CONFIG = "config";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Utils.getSettingsTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        /* Prevent create activity in two pane mode (after resizing window) */
        if (Utils.isLargeScreenDevice(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_preference);

        String fragment = null;
        String title = null;
        Intent intent = getIntent();
        if (intent.hasExtra(TAG_CONFIG)) {
            PreferenceActivityConfig config = intent.getParcelableExtra(TAG_CONFIG);
            fragment = config.getFragment();
            title = config.getTitle();
        }

        toolbar = findViewById(R.id.toolbar);
        if (title != null)
            toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (fragment != null && savedInstanceState == null)
            setFragment(getFragment(fragment));
    }

    public <F extends androidx.preference.PreferenceFragmentCompat> void setFragment(F fragment)
    {
        if (fragment == null)
            return;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private <F extends PreferenceFragmentCompat> F getFragment(String fragment)
    {
        if (fragment != null) {
            if (fragment.equals(AppearanceSettingsFragment.class.getSimpleName()))
                return (F)AppearanceSettingsFragment.newInstance();
            else if (fragment.equals(BehaviorSettingsFragment.class.getSimpleName()))
                return (F)BehaviorSettingsFragment.newInstance();
            else if (fragment.equals(StorageSettingsFragment.class.getSimpleName()))
                return (F)StorageSettingsFragment.newInstance();
            else if (fragment.equals(BrowserSettingsFragment.class.getSimpleName()))
                return (F)BrowserSettingsFragment.newInstance();
            else if (fragment.equals(LimitationsSettingsFragment.class.getSimpleName()))
                return (F)LimitationsSettingsFragment.newInstance();
            else
                return null;
        }

        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }
}
