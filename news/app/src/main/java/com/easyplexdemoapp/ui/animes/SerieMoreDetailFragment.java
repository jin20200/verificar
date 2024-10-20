package com.easyplexdemoapp.ui.animes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.databinding.BrowseFragmentBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.library.SectionsPagerAdapter;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.Tools;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import javax.inject.Inject;

public class SerieMoreDetailFragment extends Fragment implements Injectable {

    BrowseFragmentBinding binding;


    @Inject
    SettingsRepository authRepository;

    @Inject
    SettingsManager settingsManager;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.browse_fragment, container, false);

        onLoadToolbar();
        onLoadAppLogo();
        onSetupTabs();

        setHasOptionsMenu(true);

        return binding.getRoot();


    }


    @SuppressLint("ResourceAsColor")
    private void onSetupTabs() {

        setupViewPager(binding.viewPager);


        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    if(position == 0)
                        tab.setText(getResources().getString(R.string.episodes));
                    else if (position == 1) {
                        tab.setText(getResources().getString(R.string.relateds));
                    }
                }).attach();



        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                // on Tab Selected


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                // on Tab UnSelected

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


                // on Tab ReSelected

            }
        });

    }


    // Load Logo
    private void onLoadAppLogo() {

        Tools.loadMiniLogo(getActivity(),binding.toolbar.logoImageTop);

    }

    private void onLoadToolbar() {

        Tools.loadToolbar(((AppCompatActivity) requireActivity()),binding.toolbar.toolbar,null);

    }

    private void setupViewPager(ViewPager2 viewPager) {

        SectionsPagerAdapter viewPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),getLifecycle());
        viewPagerAdapter.addFragment(new EpisodesFragment());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getActivity(), BaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.viewPager.setSaveFromParentEnabled(true);
        binding.viewPager.setAdapter(null);
        binding = null;

    }

}
