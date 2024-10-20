package com.easyplexdemoapp.ui.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.easyplexdemoapp.data.repository.SettingsRepository;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.streaming.StreamingFragment;
import com.google.android.material.tabs.TabLayout;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.BrowseFragmentBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.util.Tools;
import com.google.android.material.tabs.TabLayoutMediator;

import javax.inject.Inject;

public class LibraryFragment extends Fragment implements Injectable {

    BrowseFragmentBinding binding;


    @Inject
    SettingsRepository authRepository;

    @Inject
    SettingsManager settingsManager;


    boolean networksActivated;

    boolean languagesActivated;

    boolean animeActivated;


    SectionsPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.browse_fragment, container, false);

        binding.toolbar.MoreOptionsLinear.setVisibility(View.GONE);

        onLoadToolbar();
        onLoadAppLogo();
        onSetupTabs();


        networksActivated = settingsManager.getSettings().getNetworks() == 1;
        languagesActivated = settingsManager.getSettings().getInternallangs() == 1;
        animeActivated = settingsManager.getSettings().getAnime() == 1;

        setHasOptionsMenu(true);

        return binding.getRoot();


    }


    @SuppressLint("ResourceAsColor")
    private void onSetupTabs() {
        setupViewPager(binding.viewPager);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    int libraryStyle = settingsManager.getSettings().getLibraryStyle();
                    int networks = settingsManager.getSettings().getNetworks();
                    int internallangs = settingsManager.getSettings().getInternallangs();

                    if (libraryStyle == 1) {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.browse);
                                break;
                            case 1:
                                if (networks == 1) {
                                    tab.setText(R.string.networks);
                                } else if (internallangs == 1) {
                                    tab.setText(R.string.languages);
                                }
                                break;
                            case 2:
                                if (internallangs == 1) {
                                    tab.setText(R.string.languages);
                                }
                                break;
                        }
                    } else {
                        switch (position) {
                            case 0:
                                tab.setText(R.string.movies);
                                break;
                            case 1:
                                tab.setText(R.string.series);
                                break;
                            case 2:
                                if (settingsManager.getSettings().getAnime() == 1) {
                                    tab.setText(R.string.animes);
                                } else {
                                    if (networks == 1) {
                                        tab.setText(R.string.networks);
                                    } else if (internallangs == 1) {
                                        tab.setText(R.string.languages);
                                    }
                                }
                                break;
                            case 3:
                                if (settingsManager.getSettings().getAnime() == 1) {
                                    if (networks == 1) {
                                        tab.setText(R.string.networks);
                                    } else if (internallangs == 1) {
                                        tab.setText(R.string.languages);
                                    }
                                } else {
                                    if (networks == 1 && internallangs != 1) {
                                        tab.setText(R.string.networks);
                                    } else if (internallangs == 1) {
                                        tab.setText(R.string.languages);
                                    }
                                }
                                break;
                            case 4:
                                if (settingsManager.getSettings().getAnime() == 1 && networks == 1 && internallangs == 1) {
                                    tab.setText(R.string.languages);
                                }
                                break;
                        }
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


    private void setupViewPager(ViewPager2 viewPager) {

        viewPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), getLifecycle());


        if (settingsManager.getSettings().getLibraryStyle() == 1){


            viewPagerAdapter.addFragment(new LibraryStyleFragment());


            if (settingsManager.getSettings().getNetworks() == 1) {

                if (settingsManager.getSettings().getDefault_layout_networks().equals("Layout1")){

                    viewPagerAdapter.addFragment(new NetworksFragment2());

                }else {

                    viewPagerAdapter.addFragment(new NetworksFragment());
                }

            }

            // Add "Languages" fragment if internal languages setting is 1
            if (settingsManager.getSettings().getInternallangs() == 1) {
                viewPagerAdapter.addFragment(new LanguagesFragment());
            }


        }else {

            // Always add "Movies" and "Series" fragments
            viewPagerAdapter.addFragment(new MoviesFragment());
            viewPagerAdapter.addFragment(new SeriesFragment());

            // Add "Animes" fragment if anime setting is 1
            if (settingsManager.getSettings().getAnime() == 1) {
                viewPagerAdapter.addFragment(new AnimesFragment());
            }

            // Add "Networks" fragment if networks setting is 1
            if (settingsManager.getSettings().getNetworks() == 1) {
                if (settingsManager.getSettings().getDefault_layout_networks().equals("Layout1")) {
                    viewPagerAdapter.addFragment(new NetworksFragment2());
                } else {
                    viewPagerAdapter.addFragment(new NetworksFragment());
                }
            }

            // Add "Languages" fragment if internal languages setting is 1
            if (settingsManager.getSettings().getInternallangs() == 1) {
                viewPagerAdapter.addFragment(new LanguagesFragment());
            }

        }



        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getItemCount());
        viewPagerAdapter.notifyDataSetChanged();
    }




    // Load Logo
    private void onLoadAppLogo() {

        Tools.loadMiniLogo(getActivity(),binding.toolbar.logoImageTop);

    }

    private void onLoadToolbar() {

        Tools.loadToolbar(((AppCompatActivity) requireActivity()),binding.toolbar.toolbar,null);

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


}
