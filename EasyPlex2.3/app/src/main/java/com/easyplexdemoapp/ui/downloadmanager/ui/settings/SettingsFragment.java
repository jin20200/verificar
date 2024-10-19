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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.AppearanceSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.BehaviorSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.BrowserSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.LimitationsSettingsFragment;
import com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections.StorageSettingsFragment;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import static com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.AppearanceSettings;
import static com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.BehaviorSettings;
import static com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.BrowserSettings;
import static com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.LimitationsSettings;
import static com.easyplexdemoapp.ui.downloadmanager.ui.settings.SettingsActivity.StorageSettings;

public class SettingsFragment extends PreferenceFragmentCompat
{
    @SuppressWarnings("unused")
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private AppCompatActivity activity;
    private SettingsViewModel viewModel;

    public static SettingsFragment newInstance()
    {
        SettingsFragment fragment = new SettingsFragment();

        fragment.setArguments(new Bundle());

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        if (context instanceof AppCompatActivity)
            activity = (AppCompatActivity)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (activity == null)
            activity = (AppCompatActivity)getActivity();

        viewModel = new ViewModelProvider(activity).get(SettingsViewModel.class);

        String preference = activity.getIntent().getStringExtra(SettingsActivity.TAG_OPEN_PREFERENCE);
        if (preference != null) {
            openPreference(preference);
            if (!Utils.isLargeScreenDevice(activity))
                activity.finish();

        } else if (Utils.isTwoPane(activity)) {
            Fragment f = activity.getSupportFragmentManager()
                    .findFragmentById(R.id.detail_fragment_container);
            if (f == null)
                setFragment(AppearanceSettingsFragment.newInstance(),
                        getString(R.string.pref_header_appearance));
        }


        Preference storage = findPreference(StorageSettingsFragment.class.getSimpleName());
        storage.setOnPreferenceClickListener(prefClickListener);

        Preference limitations = findPreference(LimitationsSettingsFragment.class.getSimpleName());
        limitations.setOnPreferenceClickListener(prefClickListener);
    }

    private final Preference.OnPreferenceClickListener prefClickListener = (preference) -> {
        openPreference(preference.getKey());
        return true;
    };

    private void openPreference(String prefName)
    {
        switch (prefName) {

            case StorageSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(StorageSettingsFragment.newInstance(),
                            getString(R.string.pref_header_storage));
                } else {
                    startActivity(StorageSettingsFragment.class,
                            getString(R.string.pref_header_storage));
                }
                break;

            case LimitationsSettings:
                if (Utils.isLargeScreenDevice(activity)) {
                    setFragment(LimitationsSettingsFragment.newInstance(),
                            getString(R.string.pref_header_limitations));
                } else {
                    startActivity(LimitationsSettingsFragment.class,
                            getString(R.string.pref_header_limitations));
                }
                break;
        }
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.pref_headers, rootKey);
    }

    private <F extends PreferenceFragmentCompat> void setFragment(F fragment, String title)
    {
        viewModel.detailTitleChanged.setValue(title);

        if (Utils.isLargeScreenDevice(activity)) {
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    private <F extends PreferenceFragmentCompat> void startActivity(Class<F> fragment, String title)
    {
        Intent i = new Intent(activity, PreferenceActivity.class);
        PreferenceActivityConfig config = new PreferenceActivityConfig(
                fragment.getSimpleName(),
                title);

        i.putExtra(PreferenceActivity.TAG_CONFIG, config);
        startActivity(i);
    }
}
