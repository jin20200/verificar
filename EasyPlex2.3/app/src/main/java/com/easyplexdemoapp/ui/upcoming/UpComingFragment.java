package com.easyplexdemoapp.ui.upcoming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import com.bumptech.glide.Glide;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.databinding.FragmentUpcomingBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.viewmodels.UpcomingViewModel;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;
import javax.inject.Inject;

public class UpComingFragment extends Fragment implements Injectable{

    @Inject
    AppController appController;

    @Inject
    SettingsManager settingsManager;

    FragmentUpcomingBinding binding;

    private boolean mUpcomingSectionLoaded;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UpcomingViewModel upcomingViewModel;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming, container, false);

        onLoadToolbar();

        setHasOptionsMenu(true);

        binding.progressBar.setVisibility(View.VISIBLE);

        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upcomingViewModel = new ViewModelProvider(this, viewModelFactory).get(UpcomingViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);

        onLoadUpcoming();

    }



    private void onLoadToolbar() {

        Tools.loadToolbar(((AppCompatActivity)requireActivity()),binding.toolbar,null);
        Tools.loadMiniLogo(getActivity(),binding.logoImageTop);

    }

    private void onLoadUpcoming() {

        UpcomingAdapter mUpcomingAdapter = new UpcomingAdapter(appController);
        binding.recyclerViewUpcoming.setAdapter(mUpcomingAdapter);
        binding.recyclerViewUpcoming.setHasFixedSize(true);
        binding.recyclerViewUpcoming.setNestedScrollingEnabled(false);
        binding.recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewUpcoming.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));
        binding.recyclerViewUpcoming.setItemViewCacheSize(20);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.recyclerViewUpcoming);

        binding.indicator.attachToRecyclerView(binding.recyclerViewUpcoming, pagerSnapHelper);
        binding.indicator.createIndicators(mUpcomingAdapter.getItemCount(),0);
        mUpcomingAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());
        ViewCompat.setNestedScrollingEnabled(binding.recyclerViewUpcoming, false);
        upcomingViewModel.getUpcomingMovie();
        upcomingViewModel.upcomingResponseMutableLive.observe(getViewLifecycleOwner(), upcoming -> {

                mUpcomingAdapter.addCasts(upcoming.getUpcoming(),settingsManager);

                if (mUpcomingAdapter.getItemCount() == 0) {

                    binding.noResults.setVisibility(View.VISIBLE);

                }else {


                    binding.noResults.setVisibility(View.GONE);

                }

                mUpcomingSectionLoaded = true;
                checkAllDataLoaded();

        });


    }


    private void checkAllDataLoaded() {
        if (mUpcomingSectionLoaded ) {
            binding.progressBar.setVisibility(View.GONE);
        }
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.recyclerViewUpcoming.setAdapter(null);
        Glide.get(requireActivity()).clearMemory();
        binding.constraintLayout.removeAllViews();
        binding =null;

    }


}
