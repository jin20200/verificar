package com.easyplexdemoapp.ui.home.adapters;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.databinding.ItemRelatedsBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

/**
 * Adapter for Movie Casts.
 *
 * @author Yobex.
 */
public class RelatedsAdapter extends RecyclerView.Adapter<RelatedsAdapter.MainViewHolder> {




    private List<Media> castList;


    final SettingsManager settingsManager;
    final AppController appController;

    public RelatedsAdapter(AppController appController, SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        this.appController = appController;

    }


    public void addToContent(List<Media> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RelatedsAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemRelatedsBinding binding = ItemRelatedsBinding.inflate(inflater, parent, false);

        // Set the AppController for data binding
        binding.setController(appController);


        // Update the shadow state
        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        // Get the root layout of the binding
        CardView rootLayout = binding.getRoot().findViewById(R.id.rootLayout);

        // Disable or enable shadow based on the boolean value
        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));

        return new RelatedsAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedsAdapter.MainViewHolder holder, int position) {
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



        private final ItemRelatedsBinding binding;


        MainViewHolder(@NonNull ItemRelatedsBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Media media = castList.get(position);

            Context context = binding.itemMovieImage.getContext();

            if (media !=null){


                String mediaType = media.getType();

                if ("movie".equals(mediaType)) {

                    binding.movietitle.setText(media.getTitle());

                } else if ("anime".equals(mediaType) || ("serie".equals(mediaType))) {

                    binding.movietitle.setText(media.getName());

                }


                if (media.getSubtitle() !=null) {

                    binding.substitle.setText(media.getSubtitle());

                }else {

                    binding.substitle.setVisibility(View.GONE);
                }


                binding.moviePremuim.setVisibility(media.getPremuim() == 1 ? View.VISIBLE : View.GONE);


                Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());

                Tools.onLoadReleaseDate(binding.mrelease,media.getReleaseDate());

                binding.rootLayout.setOnClickListener(v -> {



                    if ("movie".equals(media.getType())) {



                        ((Activity) context).finish();
                        Intent intent = new Intent(context, MovieDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else if ("anime".equals(media.getType())) {
                        ((Activity) context).finish();
                        Intent intent = new Intent(context, AnimeDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }else if ("serie".equals(media.getType())) {
                        ((Activity) context).finish();
                        Intent intent = new Intent(context, SerieDetailsActivity.class);
                        intent.putExtra(ARG_MOVIE, media);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                });

                Tools.onLoadMediaCover(context,binding.itemMovieImage,media.getPosterPath());

            }

        }
    }
}
