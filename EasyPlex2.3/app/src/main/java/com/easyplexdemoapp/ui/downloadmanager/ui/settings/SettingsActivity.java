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

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;

public class SettingsActivity extends AppCompatActivity
{
    @SuppressWarnings("unused")
    private static final String TAG = SettingsActivity.class.getSimpleName();

    public static final String TAG_OPEN_PREFERENCE = "open_preference";

    public static final String AppearanceSettings = "AppearanceSettingsFragment";
    public static final String BehaviorSettings = "BehaviorSettingsFragment";
    public static final String LimitationsSettings = "LimitationsSettingsFragment";
    public static final String StorageSettings = "StorageSettingsFragment";
    public static final String BrowserSettings = "BrowserSettingsFragment";

    private Toolbar toolbar;
    private TextView detailTitle;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(Utils.getSettingsTheme(getApplicationContext()));
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailTitle = findViewById(R.id.detail_title);
        viewModel.detailTitleChanged.observe(this, title -> {
            if (title != null && detailTitle != null)
                detailTitle.setText(title);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
