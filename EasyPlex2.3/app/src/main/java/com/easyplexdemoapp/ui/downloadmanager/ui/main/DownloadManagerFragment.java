/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2022 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.main;

import static android.text.Html.fromHtml;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.downloadmanager.core.RepositoryHelper;
import com.easyplexdemoapp.ui.downloadmanager.core.model.DownloadEngine;
import com.easyplexdemoapp.ui.downloadmanager.core.settings.SettingsRepository;
import com.easyplexdemoapp.ui.downloadmanager.core.utils.Utils;
import com.easyplexdemoapp.ui.downloadmanager.service.DownloadService;
import com.easyplexdemoapp.ui.downloadmanager.ui.BaseAlertDialog;
import com.easyplexdemoapp.ui.downloadmanager.ui.PermissionDeniedDialog;
import com.easyplexdemoapp.util.Tools;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DownloadManagerFragment extends Fragment implements Injectable {




    private static final String TAG_ABOUT_DIALOG = "about_dialog";
    private static final String TAG_PERM_DENIED_DIALOG = "perm_denied_dialog";

    private Toolbar toolbar;
    private ImageView logoImageTop;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DownloadListPagerAdapter pagerAdapter;
    private DownloadsViewModel fragmentViewModel;
    private SearchView searchView;
    private DownloadEngine engine;
    private SettingsRepository pref;
    protected CompositeDisposable disposables = new CompositeDisposable();
    private BaseAlertDialog.SharedViewModel dialogViewModel;
    private BaseAlertDialog aboutDialog;
    private PermissionDeniedDialog permDeniedDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTheme(Utils.getAppTheme(requireActivity()));
        View root_view = inflater.inflate(R.layout.activity_main_download, container, false);
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);


        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.setSystemBarTransparent(requireActivity());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        ViewModelProvider provider = new ViewModelProvider(this);
        fragmentViewModel = provider.get(DownloadsViewModel.class);
        dialogViewModel = provider.get(BaseAlertDialog.SharedViewModel.class);
        FragmentManager fm = getChildFragmentManager();
        aboutDialog = (BaseAlertDialog)fm.findFragmentByTag(TAG_ABOUT_DIALOG);
        permDeniedDialog = (PermissionDeniedDialog)fm.findFragmentByTag(TAG_PERM_DENIED_DIALOG);

        if (!Utils.checkStoragePermission(requireActivity()) && permDeniedDialog == null) {
            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        pref = RepositoryHelper.getSettingsRepository(requireActivity());

        engine = DownloadEngine.getInstance(requireActivity());

        toolbar = root_view.findViewById(R.id.toolbar);
        tabLayout = root_view.findViewById(R.id.download_list_tabs);
        viewPager = root_view.findViewById(R.id.download_list_viewpager);

        logoImageTop = root_view.findViewById(R.id.logo_image_top);
        toolbar.setTitle(null);

        /* Disable elevation for portrait mode */
        if (!Utils.isTwoPane(requireActivity()))
            toolbar.setElevation(0);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        fragmentViewModel.resetSearch();

        pagerAdapter = new DownloadListPagerAdapter(requireActivity());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(DownloadListPagerAdapter.NUM_FRAGMENTS);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == DownloadListPagerAdapter.QUEUED_FRAG_POS) {
                        tab.setText(R.string.fragment_title_queued);
                    } else if (position == DownloadListPagerAdapter.COMPLETED_FRAG_POS) {
                        tab.setText(R.string.fragment_title_completed);
                    }
                }
        ).attach();
        onLoadAppLogo();
        engine.restoreDownloads();


        return  root_view;

    }

    private final ActivityResultLauncher<String> storagePermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (!isGranted && Utils.shouldRequestStoragePermission(requireActivity())) {
                    FragmentManager fm = getChildFragmentManager();
                    if (fm.findFragmentByTag(TAG_PERM_DENIED_DIALOG) == null) {
                        permDeniedDialog = PermissionDeniedDialog.newInstance();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.add(permDeniedDialog, TAG_PERM_DENIED_DIALOG);
                        ft.commitAllowingStateLoss();
                    }
                }
            });




    private void onLoadAppLogo() {

        Tools.loadMiniLogo(requireActivity(),logoImageTop);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        subscribeAlertDialog();
        subscribeSettingsChanged();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        disposables.clear();
    }

    private void subscribeAlertDialog()
    {
        Disposable d = dialogViewModel.observeEvents()
                .subscribe(event -> {
                    if (event.dialogTag == null) {
                        return;
                    }
                    if (event.dialogTag.equals(TAG_ABOUT_DIALOG)) {
                        if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                            openChangelogLink();
                        } else if (event.type == BaseAlertDialog.EventType.DIALOG_SHOWN) {
                            initAboutDialog();
                        }
                    } else if (event.dialogTag.equals(TAG_PERM_DENIED_DIALOG)) {
                        if (event.type != BaseAlertDialog.EventType.DIALOG_SHOWN) {
                            permDeniedDialog.dismiss();
                        }
                        if (event.type == BaseAlertDialog.EventType.NEGATIVE_BUTTON_CLICKED) {
                            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                });
        disposables.add(d);
    }

    void subscribeSettingsChanged() {
        requireActivity().invalidateOptionsMenu();
        disposables.add(pref.observeSettingsChanged()
                .subscribe(key -> {
                    if (key.equals(getString(R.string.pref_key_browser_hide_menu_icon))) {
                        requireActivity().invalidateOptionsMenu();
                    }
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.pause_all_menu) {
            pauseAll();
        } else if (itemId == R.id.resume_all_menu) {
            resumeAll();
        }
        return true;
    }

    private void pauseAll()
    {
        engine.pauseAllDownloads();
    }

    private void resumeAll()
    {
        engine.resumeDownloads(false);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    private void initAboutDialog()
    {
        if (aboutDialog == null)
            return;

        Dialog dialog = aboutDialog.getDialog();
        if (dialog != null) {
            @SuppressLint("CutPasteId") TextView versionTextView = dialog.findViewById(R.id.aboutus);
            @SuppressLint("CutPasteId") TextView descriptionTextView = dialog.findViewById(R.id.aboutus);
            String versionName = Utils.getAppVersionName(requireActivity());
            if (versionName != null)
                versionTextView.setText(versionName);
            descriptionTextView.setText(fromHtml(getString(R.string.about_description)));
            descriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private void openChangelogLink()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(getString(R.string.about_changelog_link)));
        startActivity(i);
    }

    public void shutdown()
    {
        Intent i = new Intent(requireActivity(), DownloadService.class);
        i.setAction(DownloadService.ACTION_SHUTDOWN);
        requireActivity().startService(i);
        requireActivity().finish();
    }

}
