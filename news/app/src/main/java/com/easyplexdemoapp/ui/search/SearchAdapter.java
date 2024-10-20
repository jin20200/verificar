package com.easyplexdemoapp.ui.search;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;
import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.History;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ItemSuggest2Binding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.util.Tools;

import java.util.List;


/**
 * Adapter for Search Results (Movies,Series,Animes,Streaming).
 *
 * @author Yobex.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {



    private List<Media> castList;
    private Context context;
    private SettingsManager settingsManager;
    private MediaRepository repository;
    private AuthManager authManager;
    private History history;
    private static final int PRELOAD_TIME_S = 2;


    @SuppressLint("NotifyDataSetChanged")
    public void setSearch(List<Media> castList, Context context, SettingsManager
            settingsManager, MediaRepository repository, AuthManager authManager) {
        this.castList = castList;
        this.context = context;
        this.settingsManager = settingsManager;
        this.repository = repository;
        this.authManager = authManager;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemSuggest2Binding binding = ItemSuggest2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
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

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private final ItemSuggest2Binding binding;


        SearchViewHolder(@NonNull ItemSuggest2Binding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final int position) {

            final Media media = castList.get(position);

            if ( media.getPosterPath() !=null &&  !media.getPosterPath().isEmpty()) {
                Tools.onLoadMediaCover(context, binding.itemMovieImage, media.getPosterPath());
            }else {

                Tools.onLoadMediaCoverEmptyCovers(context, binding.itemMovieImage, settingsManager.getSettings().getDefaultMediaPlaceholderPath());
            }


            if ( media.getBackdropPath() !=null &&  !media.getBackdropPath().isEmpty()) {
                Tools.onLoadMediaCover(context, binding.imageBackground, media.getBackdropPath());
            }else {

                Tools.onLoadMediaCoverEmptyCoversCardView(context, binding.imageBackground, settingsManager.getSettings().getDefaultMediaPlaceholderPath());
            }


            binding.mgenres.setText(media.getGenreName());


            if ("Streaming".equals(media.getType())) {

                if (media.getBackdropPath() != null && !media.getBackdropPath().isEmpty()) {
                    Tools.onLoadMediaCover(context, binding.itemMovieImage, media.getBackdropPath());
                } else {

                    Tools.onLoadMediaCoverEmptyCovers(context, binding.itemMovieImage, settingsManager.getSettings().getDefaultMediaPlaceholderPath());
                }


                if (media.getPosterPath() != null && !media.getPosterPath().isEmpty()) {
                    Tools.onLoadMediaCover(context, binding.imageBackground, media.getPosterPath());
                } else {

                    Tools.onLoadMediaCoverEmptyCovers(context, binding.imageBackground, settingsManager.getSettings().getDefaultMediaPlaceholderPath());
                }

                binding.eptitle.setText(media.getName());
                binding.epoverview.setText(media.getOverview());
                binding.viewMovieViews.setText(context.getResources().getString(R.string.streaming));
                binding.epoverview.setText(media.getOverview());
                binding.ratingBar.setVisibility(View.GONE);
                binding.viewMovieRating.setText(String.valueOf(media.getVoteAverage()));
                binding.linearrating.setVisibility(View.GONE);
                binding.substitle.setVisibility(View.GONE);


                if (media.getSubtitle() != null) {

                    binding.substitle.setText(media.getSubtitle());

                } else {

                    binding.substitle.setVisibility(View.GONE);
                }


            } else if ("anime".equals(media.getType())) {
                binding.eptitle.setText(media.getName());
                binding.epoverview.setText(media.getOverview());
                binding.viewMovieViews.setText(context.getResources().getString(R.string.animes));
                binding.epoverview.setText(media.getOverview());
                binding.ratingBar.setRating(media.getVoteAverage() / 2);
                binding.viewMovieRating.setText(String.valueOf(media.getVoteAverage()));


                if (media.getSubtitle() !=null) {

                    binding.substitle.setText(media.getSubtitle());

                }else {

                    binding.substitle.setVisibility(View.GONE);
                }

            } else if ("movie".equals(media.getType())) {
                binding.eptitle.setText(media.getName());
                binding.epoverview.setText(media.getOverview());
                binding.viewMovieViews.setText(context.getResources().getString(R.string.movies));
                binding.epoverview.setText(media.getOverview());
                binding.ratingBar.setRating(media.getVoteAverage() / 2);
                binding.viewMovieRating.setText(String.valueOf(media.getVoteAverage()));



                if (media.getSubtitle() !=null) {

                    binding.substitle.setText(media.getSubtitle());

                }else {

                    binding.substitle.setVisibility(View.GONE);
                }

            } else if ("serie".equals(media.getType())) {
                binding.eptitle.setText(media.getName());
                binding.epoverview.setText(media.getOverview());
                binding.viewMovieViews.setText(context.getResources().getString(R.string.series));
                binding.epoverview.setText(media.getOverview());
                binding.ratingBar.setRating(media.getVoteAverage() / 2);
                binding.viewMovieRating.setText(String.valueOf(media.getVoteAverage()));


                if (media.getSubtitle() !=null) {

                    binding.substitle.setText(media.getSubtitle());

                }else {

                    binding.substitle.setVisibility(View.GONE);
                }
            }


            binding.rootLayout.setOnClickListener(view -> {
                Intent intent;
                if ("anime".equals(media.getType())) {
                    intent = new Intent(context, AnimeDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);

                } else if ("Streaming".equals(media.getType())) {
                    intent = new Intent(context, StreamingetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);
                } else if ("movie".equals(media.getType())) {
                    intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);
                } else if ("serie".equals(media.getType())) {
                    intent = new Intent(context, SerieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);
                }


            });

        }



        private void startStreamFromDialogStreaming(Media movieDetail, String link, int hls) {

            Tools.startMainStreamStreaming(context,movieDetail, link,hls, null);

        }

        private void startStreamFromExternalLaunchers(Media movieDetail, String link, int hls, MediaStream mediaStream) {


            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_bottom_stream);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());

            lp.gravity = Gravity.BOTTOM;
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;


            LinearLayout vlc = dialog.findViewById(R.id.vlc);
            LinearLayout mxPlayer = dialog.findViewById(R.id.mxPlayer);
            LinearLayout easyplexPlayer = dialog.findViewById(R.id.easyplexPlayer);
            LinearLayout webcast = dialog.findViewById(R.id.webCast);


            vlc.setOnClickListener(v12 -> {
                Tools.streamMediaFromVlc(context,link,movieDetail,settingsManager, mediaStream);
                dialog.hide();
            });

            mxPlayer.setOnClickListener(v12 -> {
                Tools.streamMediaFromMxPlayer(context,link,movieDetail,settingsManager, mediaStream);
                dialog.hide();

            });

            webcast.setOnClickListener(v12 -> {
                Tools.streamMediaFromMxWebcast(context,link,movieDetail,settingsManager, mediaStream);
                dialog.hide();

            });


            easyplexPlayer.setOnClickListener(v12 -> {
                Tools.startMainStreamStreaming(context,movieDetail, link,hls, mediaStream);
                dialog.hide();


            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);

            dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                    dialog.dismiss());


            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

    }

}
