package com.easyplexdemoapp.ui.upcoming;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.upcoming.Upcoming;
import com.easyplexdemoapp.databinding.ItemUpcomingBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.DialogHelper;
import com.easyplexdemoapp.util.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Adapter for  Upcoming Movies
 *
 * @author Yobex.
 */
public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    private List<Upcoming> upcomingList;
    private SettingsManager settingsManager;


    final AppController appController;

    public UpcomingAdapter(AppController appController) {
        this.appController = appController;
    }

    public void addCasts(List<Upcoming> castList,SettingsManager settingsManager) {
        this.upcomingList = castList;
        this.settingsManager = settingsManager;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemUpcomingBinding binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        binding.setController(appController);

        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        Tools.onDisableShadow(binding.cardView.getContext(),binding.cardView, Boolean.TRUE.equals(appController.isShadowEnabled.get()));


        return new UpcomingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        if (upcomingList != null) {
            return upcomingList.size();
        } else {
            return 0;
        }
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {

        private final ItemUpcomingBinding binding;


        UpcomingViewHolder (@NonNull ItemUpcomingBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;


        }

        @SuppressLint("SetTextI18n")
        void onBind(final int position) {



            final Upcoming upcoming = upcomingList.get(position);

            binding.mgenres.setText(upcoming.getGenre());

            if (Tools.isRTL(Locale.getDefault())){
                binding.mgenres.setBackgroundResource(R.drawable.bg_label_rtl);
            }

            Context context = binding.itemMovieImage.getContext();

            Glide.with(context).asBitmap().load(upcoming.getPosterPath())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into(binding.itemMovieImage);

            binding.movietitle.setText(upcoming.getTitle());

            if (upcoming.getReleaseDate() != null && !upcoming.getReleaseDate().trim().isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
                try {
                    Date releaseDate = sdf1.parse(upcoming.getReleaseDate());
                    binding.releaseShowCard.setText("Coming "+sdf2.format(releaseDate));
                } catch (ParseException e) {

                    Timber.d("%s", Arrays.toString(e.getStackTrace()));

                }
            } else {
                binding.releaseShowCard.setText("");
            }

            binding.cardView.setOnClickListener(v -> {

                if (upcoming.getTrailerId() !=null && !upcoming.getTrailerId().isEmpty()){

                    Tools.startTrailer(context,upcoming.getTrailerId(),upcoming.getTitle(),upcoming.getBackdropPath(),settingsManager,upcoming.getTrailerId());
                }else {

                    DialogHelper.showNoStreamAvailable(context);
                }

            });


        }
    }


}
