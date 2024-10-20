package com.easyplexdemoapp.ui.home.adapters;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.databinding.ItemMovieBinding;
import com.easyplexdemoapp.databinding.RowHomecontentTitleBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class CustomLangsAdapter extends RecyclerView.Adapter<CustomLangsAdapter.MainViewHolder> {

    public List<Media> castList;
    private Context context;
    final SettingsManager settingsManager;
    final AppController appController;


    private RowHomecontentTitleBinding titleBinding;


    public CustomLangsAdapter(SettingsManager settingsManager, AppController appController) {

        this.settingsManager = settingsManager;
        this.appController = appController;
    }


    public void setData(List<Media> data,Context context) {
        this.castList = data;
        this.context = context;
    }


    // Set the title binding for the adapter
    public void setTitleBinding(RowHomecontentTitleBinding titleBinding) {
        this.titleBinding = titleBinding;
    }


    @NonNull
    @Override
    public CustomLangsAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        // Set the AppController for data binding
        binding.setController(appController);

        // Update the shadow state
        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        // Get the root layout of the binding
        CardView rootLayout = binding.getRoot().findViewById(R.id.rootLayout);

        // Disable or enable shadow based on the boolean value
        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));

        return new CustomLangsAdapter.MainViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.onBind(position);

    }



    @Override
    public int getItemCount() {
        if (castList != null) {
            return castList.size();
        } else {
            return 0;
        }
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        MainViewHolder(@NonNull ItemMovieBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;


        }


        void onBind(final int position) {

            final Media media = castList.get(position);


            binding.hasNewEpisodes.setVisibility(media.getNewEpisodes() == 1 ? View.VISIBLE : View.GONE);

            binding.movietitle.setText(media.getName());

            String mediaType = media.getType();

            binding.rootLayout.setOnClickListener(view -> {

                Intent intent;

                switch (mediaType){

                    case "movie":
                        intent = new Intent(context, MovieDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        context.startActivity(intent);
                        break;

                    case "serie":
                        intent = new Intent(context, SerieDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        context.startActivity(intent);
                        break;
                    case "anime":
                        intent = new Intent(context, AnimeDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        context.startActivity(intent);
                        break;
                }


            });

            if (media.getSubtitle() !=null) {

                binding.substitle.setText(media.getSubtitle());

            }else {

                binding.substitle.setVisibility(View.GONE);
            }


            if (media.getPremuim() == 1) {

                binding.moviePremuim.setVisibility(View.VISIBLE);


            }else {

                binding.moviePremuim.setVisibility(View.GONE);
            }


            Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());


            String date = media.getReleaseDate();


            if (date != null && !date.trim().isEmpty()) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
                Date releaseDate = null;
                try {
                    releaseDate = sdf1.parse(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                assert releaseDate != null;
                binding.mrelease.setText(sdf2.format(releaseDate));
            } else {
                binding.mrelease.setText("");

            }


        }


    }



}
