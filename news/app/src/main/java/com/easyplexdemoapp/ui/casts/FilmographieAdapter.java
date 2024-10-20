package com.easyplexdemoapp.ui.casts;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.databinding.ItemFilmographieBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Tools;

import org.jetbrains.annotations.NotNull;


/**
 * Adapter for Next Movie.
 *
 * @author Yobex.
 */
public class FilmographieAdapter extends PagedListAdapter<Media, FilmographieAdapter.NextPlayMoviesViewHolder> {

    final Context context;

    final SettingsManager settingsManager;
    final AppController appController;

    public FilmographieAdapter(Context context,AppController appController,SettingsManager settingsManager) {
        super(ITEM_CALLBACK);
        this.context = context;
        this.appController = appController;
        this.settingsManager = settingsManager;
    }

    @NonNull
    @Override
    public NextPlayMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemFilmographieBinding binding = ItemFilmographieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        // Set the AppController for data binding
        binding.setController(appController);

        // Update the shadow state
        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        CardView rootLayout = binding.getRoot().findViewById(R.id.rootLayout);

        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));


        return new FilmographieAdapter.NextPlayMoviesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NextPlayMoviesViewHolder holder, int position) {

        Media media = getItem(position);

        holder.onBind(media);
    }


    class NextPlayMoviesViewHolder extends RecyclerView.ViewHolder {


        private final ItemFilmographieBinding binding;

        NextPlayMoviesViewHolder(@NonNull ItemFilmographieBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final Media media) {

            binding.movietitle.setText(media.getName());


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


            binding.rootLayout.setOnClickListener(view -> {

                if (media.getType().equals("serie")) {

                    Intent intent = new Intent(context, SerieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }else  if (media.getType().equals("anime")) {


                    Intent intent = new Intent(context, AnimeDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }else {

                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }



            });

            Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());

            Tools.onLoadReleaseDate(binding.mrelease,media.getReleaseDate());

        }


    }


    private static final DiffUtil.ItemCallback<Media> ITEM_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(Media oldItem, Media newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Media oldItem, @NotNull Media newItem) {
                    return oldItem.equals(newItem);
                }
            };


}
