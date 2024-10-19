package com.easyplexdemoapp.ui.home.adapters;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.easyplexdemoapp.databinding.ItemTopttenBinding;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.util.AppController;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

public class MultiDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_RECOMMENDED_MOVIES = 1;
    private static final int VIEW_TYPE_TRENDING_MOVIES = 2;

    private static final int VIEW_TYPE_LATEST_MOVIES = 3;

    private static final int VIEW_TYPE_POPULAR_SERIES = 4;

    private static final int VIEW_TYPE_LATEST_SERIES = 5;

    private static final int VIEW_TYPE_LATEST_ANIMES = 6;

    private static final int VIEW_TYPE_ADDED_THISWEEK = 7;


    private static final int VIEW_TYPE_TOP10 = 8;

    private static final int VIEW_TYPE_LATEST_MOVIES_MEDIA = 9;

    private static final int VIEW_TYPE_PINNED = 10;


    private static final int VIEW_TYPE_CHOOSED = 11;


    private static final int VIEW_TYPE_POPULAR = 12;


    private List<Media> recommendedMoviesList;
    private List<Media> trendingMoviesList;

    private List<Media> latestMovies;

    private List<Media> popularSeries;

    private List<Media> latestSeriesList;

    private List<Media> latestAnimessList;


    private List<Media> newThisWeekList;

    private List<Media> top10List;

    private List<Media> latestMoviesMediaList;


    private List<Media> pinnedList;

    private List<Media> choosedList;

    private List<Media> popularList;

    private final Context context;

    final SettingsManager settingsManager;
    final AppController appController;

    public MultiDataAdapter(SettingsManager settingsManager, AppController appController, Context context) {
        this.settingsManager = settingsManager;
        this.appController = appController;
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecommendedMoviesList(List<Media> recommendedMoviesList) {
        this.recommendedMoviesList = recommendedMoviesList;
        notifyDataSetChanged();
    }

    public void setTrendingMoviesList(List<Media> trendingMoviesList) {
        this.trendingMoviesList = trendingMoviesList;
        notifyDataSetChanged();
    }

    public void setLatestMoviesList(List<Media> latestMovies) {
        this.latestMovies = latestMovies;
        notifyDataSetChanged();
    }


    public void setPopularSeriesList(List<Media> popularSeries) {
        this.popularSeries = popularSeries;
        notifyDataSetChanged();
    }


    public void setLatestSeriesList(List<Media> latestSeriesList) {
        this.latestSeriesList = latestSeriesList;
        notifyDataSetChanged();
    }


    public void setLatestAnimesList(List<Media> latestAnimesList) {
        this.latestAnimessList = latestAnimesList;
        notifyDataSetChanged();
    }


    public void setNewThisWeekList(List<Media> newThisWeekList) {
        this.newThisWeekList = newThisWeekList;
        notifyDataSetChanged();
    }


    public void setTo10List(List<Media> to10List) {
        this.top10List = to10List;
        notifyDataSetChanged();
    }



    public void setLatestMoviesMediaList(List<Media> latestMoviesMediaList) {
        this.latestMoviesMediaList = latestMoviesMediaList;
        notifyDataSetChanged();
    }


    public void setPinnedList(List<Media> pinnedList) {
        this.pinnedList = pinnedList;
        notifyDataSetChanged();
    }



    public void setChoosedList(List<Media> choosedList) {
        this.choosedList = choosedList;
        notifyDataSetChanged();
    }



    public void setPopularList(List<Media> popularList) {
        this.popularList = popularList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(inflater, parent, false);

        ItemTopttenBinding itemTopttenBinding = ItemTopttenBinding.inflate(inflater, parent, false);

        // Set the AppController for data binding
        itemMovieBinding.setController(appController);
        // Set the AppController for data binding
        itemTopttenBinding.setController(appController);

        // Update the shadow state
        appController.isShadowEnabled.set(settingsManager.getSettings().getEnableShadows() == 1);

        // Get the root layout of the binding
        CardView rootLayout = itemMovieBinding.getRoot().findViewById(R.id.rootLayout);
        CardView rootLayout2 = itemTopttenBinding.getRoot().findViewById(R.id.rootLayout);

        // Disable or enable shadow based on the boolean value
        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));
        Tools.onDisableShadow(parent.getContext().getApplicationContext(), rootLayout2, Boolean.TRUE.equals(appController.isShadowEnabled.get()));

        // Inflate different layouts based on view type
        if (viewType == VIEW_TYPE_RECOMMENDED_MOVIES) {
            return new RecommendedMoviesViewHolder(itemMovieBinding);
        } else if (viewType == VIEW_TYPE_TRENDING_MOVIES) {
            return new TrendingMoviesViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_LATEST_MOVIES) {

            return new LatestMoviesViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_POPULAR_SERIES) {

            return new PopularSeriesViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_LATEST_SERIES) {

            return new LatestSeriesViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_LATEST_ANIMES) {

            return new LatesAnimesViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_ADDED_THISWEEK) {

            return new AddedThisWeekViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_TOP10) {

            return new Top10ViewHolder(itemTopttenBinding);

        } else if (viewType == VIEW_TYPE_LATEST_MOVIES_MEDIA) {

            return new LatestMoviesMediaViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_PINNED) {

            return new PinnedViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_CHOOSED) {

            return new ChoosedViewHolder(itemMovieBinding);

        } else if (viewType == VIEW_TYPE_POPULAR) {

            return new PopularViewHolder(itemMovieBinding);

        }


        // Return a default ViewHolder if needed
        return new RecyclerView.ViewHolder(new View(parent.getContext())) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_RECOMMENDED_MOVIES:
                ((RecommendedMoviesViewHolder) holder).onBind(getItemAtPosition(position, recommendedMoviesList));
                break;
            case VIEW_TYPE_TRENDING_MOVIES:
                ((TrendingMoviesViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES), trendingMoviesList));
                break;
            case VIEW_TYPE_LATEST_MOVIES:
                ((LatestMoviesViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES), latestMovies));
                break;
            case VIEW_TYPE_POPULAR_SERIES:
                ((PopularSeriesViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES), popularSeries));
                break;
            case VIEW_TYPE_LATEST_SERIES:
                ((LatestSeriesViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES) - getItemCountForType(VIEW_TYPE_POPULAR_SERIES), latestSeriesList));
                break;
            case VIEW_TYPE_LATEST_ANIMES:
                ((LatesAnimesViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES) - getItemCountForType(VIEW_TYPE_POPULAR_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_SERIES), latestAnimessList));
                break;
            case VIEW_TYPE_ADDED_THISWEEK:
                ((AddedThisWeekViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES) - getItemCountForType(VIEW_TYPE_POPULAR_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_ANIMES), newThisWeekList));
                break;
            case VIEW_TYPE_TOP10:
                ((Top10ViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES)
                        - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES)
                        - getItemCountForType(VIEW_TYPE_POPULAR_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_ANIMES) - getItemCountForType(VIEW_TYPE_ADDED_THISWEEK), top10List), position);
                break;
            case VIEW_TYPE_LATEST_MOVIES_MEDIA:
                ((LatestMoviesMediaViewHolder) holder).onBind(getItemAtPosition(position - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) - getItemCountForType(VIEW_TYPE_LATEST_MOVIES) - getItemCountForType(VIEW_TYPE_POPULAR_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_SERIES) - getItemCountForType(VIEW_TYPE_LATEST_ANIMES) - getItemCountForType(VIEW_TYPE_TOP10), latestMoviesMediaList));
                break;
            case VIEW_TYPE_PINNED:
                ((PinnedViewHolder) holder).onBind(getItemAtPosition(position
                        - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES)
                        - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_MOVIES)
                        - getItemCountForType(VIEW_TYPE_POPULAR_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_ANIMES)
                        - getItemCountForType(VIEW_TYPE_LATEST_MOVIES_MEDIA), pinnedList));
                break;
            case VIEW_TYPE_CHOOSED:
                ((ChoosedViewHolder) holder).onBind(getItemAtPosition(position
                        - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES)
                        - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_MOVIES)
                        - getItemCountForType(VIEW_TYPE_POPULAR_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_ANIMES)
                        - getItemCountForType(VIEW_TYPE_PINNED), choosedList));
                break;
            case VIEW_TYPE_POPULAR:
                ((PopularViewHolder) holder).onBind(getItemAtPosition(position
                        - getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES)
                        - getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_MOVIES)
                        - getItemCountForType(VIEW_TYPE_POPULAR_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_SERIES)
                        - getItemCountForType(VIEW_TYPE_LATEST_ANIMES)
                        - getItemCountForType(VIEW_TYPE_CHOOSED), popularList));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }





    @Override
    public int getItemCount() {
        int recommendedCount = getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES);
        int trendingCount = getItemCountForType(VIEW_TYPE_TRENDING_MOVIES);
        int latestMoviesCount = getItemCountForType(VIEW_TYPE_LATEST_MOVIES);
        int popularSeriesCount = getItemCountForType(VIEW_TYPE_POPULAR_SERIES);
        int latestSeriesCount = getItemCountForType(VIEW_TYPE_LATEST_SERIES);
        int latestAnimesCount = getItemCountForType(VIEW_TYPE_LATEST_ANIMES);
        int addedThisWeekCount = getItemCountForType(VIEW_TYPE_ADDED_THISWEEK);
        int top10Count = getItemCountForType(VIEW_TYPE_TOP10);
        int latestMoviesMediaCount = getItemCountForType(VIEW_TYPE_LATEST_MOVIES_MEDIA);
        int pinnedCount = getItemCountForType(VIEW_TYPE_PINNED);
        int choosedCount = getItemCountForType(VIEW_TYPE_CHOOSED);
        int popularCount = getItemCountForType(VIEW_TYPE_POPULAR);


        return recommendedCount + trendingCount + latestMoviesCount +
                popularSeriesCount + latestSeriesCount + latestAnimesCount + addedThisWeekCount + top10Count + latestMoviesMediaCount + pinnedCount + choosedCount + popularCount;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES)) {
            return VIEW_TYPE_RECOMMENDED_MOVIES;

        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)) {
            return VIEW_TYPE_TRENDING_MOVIES;

        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)) {
            return VIEW_TYPE_LATEST_MOVIES;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES) + getItemCountForType(VIEW_TYPE_POPULAR_SERIES)) {
            return VIEW_TYPE_POPULAR_SERIES;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)+ getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES)) {
            return VIEW_TYPE_LATEST_SERIES;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)+ getItemCountForType(VIEW_TYPE_LATEST_MOVIES) + getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES)) {
            return VIEW_TYPE_LATEST_ANIMES;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)+ getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES) + getItemCountForType(VIEW_TYPE_ADDED_THISWEEK)) {
            return VIEW_TYPE_ADDED_THISWEEK;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES)+ getItemCountForType(VIEW_TYPE_LATEST_MOVIES) + getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES) + getItemCountForType(VIEW_TYPE_ADDED_THISWEEK) + getItemCountForType(VIEW_TYPE_TOP10)) {
            return VIEW_TYPE_TOP10;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)+ getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES) + getItemCountForType(VIEW_TYPE_ADDED_THISWEEK) + getItemCountForType(VIEW_TYPE_TOP10) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES_MEDIA)) {
            return VIEW_TYPE_LATEST_MOVIES_MEDIA;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)+ getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES) + getItemCountForType(VIEW_TYPE_ADDED_THISWEEK) + getItemCountForType(VIEW_TYPE_TOP10) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES_MEDIA) + getItemCountForType(VIEW_TYPE_PINNED)) {
            return VIEW_TYPE_PINNED;
        } else if (position < getItemCountForType(VIEW_TYPE_RECOMMENDED_MOVIES) + getItemCountForType(VIEW_TYPE_TRENDING_MOVIES) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES)+ getItemCountForType(VIEW_TYPE_POPULAR_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_SERIES) + getItemCountForType(VIEW_TYPE_LATEST_ANIMES) + getItemCountForType(VIEW_TYPE_ADDED_THISWEEK) + getItemCountForType(VIEW_TYPE_TOP10) + getItemCountForType(VIEW_TYPE_LATEST_MOVIES_MEDIA) + getItemCountForType(VIEW_TYPE_PINNED) + getItemCountForType(VIEW_TYPE_CHOOSED)) {
            return VIEW_TYPE_CHOOSED;
        } else {
            return VIEW_TYPE_POPULAR;
        }
    }






    private int getItemCountForType(int viewType) {
        switch (viewType) {
            case VIEW_TYPE_RECOMMENDED_MOVIES:
                return recommendedMoviesList != null ? recommendedMoviesList.size() : 0;
            case VIEW_TYPE_TRENDING_MOVIES:
                return trendingMoviesList != null ? trendingMoviesList.size() : 0;
            case VIEW_TYPE_LATEST_MOVIES:
                return latestMovies != null ? latestMovies.size() : 0;
            case VIEW_TYPE_POPULAR_SERIES:
                return popularSeries != null ? popularSeries.size() : 0;
            case VIEW_TYPE_LATEST_SERIES:
                return latestSeriesList != null ? latestSeriesList.size() : 0;

            case VIEW_TYPE_LATEST_ANIMES:
                return latestAnimessList != null ? latestAnimessList.size() : 0;

            case VIEW_TYPE_ADDED_THISWEEK:
                return newThisWeekList != null ? newThisWeekList.size() : 0;

            case VIEW_TYPE_TOP10:
                return top10List != null ? top10List.size() : 0;

            case VIEW_TYPE_LATEST_MOVIES_MEDIA:
                return latestMoviesMediaList != null ? latestMoviesMediaList.size() : 0;

            case VIEW_TYPE_PINNED:
                return pinnedList != null ? pinnedList.size() : 0;

            case VIEW_TYPE_CHOOSED:
                return choosedList != null ? choosedList.size() : 0;


            case VIEW_TYPE_POPULAR:
                return popularList != null ? popularList.size() : 0;
            default:
                return 0;
        }
    }


    private Media getItemAtPosition(int position, List<Media> list) {
        return list != null && position >= 0 && position < list.size() ? list.get(position) : null;
    }


    class RecommendedMoviesViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        RecommendedMoviesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }

    class TrendingMoviesViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        TrendingMoviesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }



    private class LatestMoviesViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        LatestMoviesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private class PopularSeriesViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        PopularSeriesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private class LatestSeriesViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        LatestSeriesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private class LatesAnimesViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        LatesAnimesViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }

    private class AddedThisWeekViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        AddedThisWeekViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private class Top10ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTopttenBinding binding;

        Top10ViewHolder(@NonNull ItemTopttenBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media, int position) {
            onGlobalDetailsTopTeen(media, binding, position);
        }
    }

    private void onGlobalDetailsTopTeen(Media media, ItemTopttenBinding binding , int position) {



        if (media.getSubtitle() !=null) {

            binding.substitle.setText(media.getSubtitle());
        }else {

            binding.substitle.setVisibility(View.GONE);
        }




        String mediaType = media.getType();
        if (mediaType.equals("movie")) {
            binding.movietitle.setText(media.getName());

            binding.mgenres.setText(""+(position + 1));


            binding.rootLayout.setOnLongClickListener(v -> {
                Toast.makeText(context, "" + media.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            });

            binding.rootLayout.setOnClickListener(view -> {


                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(ARG_MOVIE, media);
                context.startActivity(intent);


            });
        } else if (mediaType.equals("serie")) {
            binding.movietitle.setText(media.getName());
            binding.mgenres.setText(""+(position + 1));


            binding.rootLayout.setOnLongClickListener(v -> {
                Toast.makeText(context, "" + media.getName(), Toast.LENGTH_SHORT).show();
                return false;
            });

            binding.rootLayout.setOnClickListener(view -> {


                Intent intent = new Intent(context, SerieDetailsActivity.class);
                intent.putExtra(ARG_MOVIE, media);
                context.startActivity(intent);


            });
        } else if (mediaType.equals("anime")) {
            binding.movietitle.setText(media.getName());
            binding.mgenres.setText(""+(position + 1));

            binding.rootLayout.setOnLongClickListener(v -> {
                Toast.makeText(context, "" + media.getName(), Toast.LENGTH_SHORT).show();
                return false;
            });

            binding.rootLayout.setOnClickListener(view -> {


                Intent intent = new Intent(context, AnimeDetailsActivity.class);
                intent.putExtra(ARG_MOVIE, media);
                context.startActivity(intent);

            });
        }



        if (media.getPremuim() == 1) {

            binding.moviePremuim.setVisibility(View.VISIBLE);

        }else {

            binding.moviePremuim.setVisibility(View.GONE);
        }

        Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());

        Tools.onLoadReleaseDate(binding.mrelease,media.getReleaseDate());
    }


    private class LatestMoviesMediaViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        LatestMoviesMediaViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }



    private class PinnedViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        PinnedViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }



    private class ChoosedViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        ChoosedViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private class PopularViewHolder extends RecyclerView.ViewHolder {
        private final ItemMovieBinding binding;

        PopularViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        void onBind(final Media media) {
            onGlobalDetails(media, binding);
        }
    }


    private void onGlobalDetails(Media media, ItemMovieBinding binding){

        Tools.onDisableShadow(context.getApplicationContext(),binding.rootLayout, Boolean.TRUE.equals(appController.isShadowEnabled.get()));


        if (media !=null){


            String mediaType = media.getType();
            if ("movie".equals(mediaType)) {
                binding.rootLayout.setOnLongClickListener(v -> {
                    Toast.makeText(context, "" + media.getTitle(), Toast.LENGTH_SHORT).show();
                    return false;
                });

                binding.rootLayout.setOnClickListener(view -> {

                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);


                });

                binding.movietitle.setText(media.getName());
            } else if ("serie".equals(mediaType)) {
                binding.movietitle.setText(media.getName());


                binding.rootLayout.setOnLongClickListener(v -> {
                    Toast.makeText(context, "" + media.getName(), Toast.LENGTH_SHORT).show();
                    return false;
                });

                binding.rootLayout.setOnClickListener(view -> {


                    Intent intent = new Intent(context, SerieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);


                });


            } else if ("anime".equals(mediaType)) {
                binding.movietitle.setText(media.getName());

                binding.rootLayout.setOnLongClickListener(v -> {
                    Toast.makeText(context, "" + media.getName(), Toast.LENGTH_SHORT).show();
                    return false;
                });

                binding.rootLayout.setOnClickListener(view -> {

                    Intent intent = new Intent(context, AnimeDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);


                });

            }



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


            Tools.onLoadReleaseDate(binding.mrelease,media.getReleaseDate());


            binding.hasNewEpisodes.setVisibility(media.getNewEpisodes() == 1 ? View.VISIBLE : View.GONE);

        }


    }
}
