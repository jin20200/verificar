package com.easyplexdemoapp.ui.streaming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.databinding.ItemShowStreamingHomeBinding;
import com.easyplexdemoapp.databinding.ItemStreamingBinding;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

public class StreamingGenresHomeAdapter extends PagedListAdapter<Media, StreamingGenresHomeAdapter.ItemViewHolder> {

    private final Context context;

    private int selectedForegroundColor = -1;
    private int defaultBackgroundColor = -1;

    public StreamingGenresHomeAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemStreamingBinding binding = ItemStreamingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new StreamingGenresHomeAdapter.ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.onBind(position);


        }


    private static final DiffUtil.ItemCallback<Media> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Media>() {
                @Override
                public boolean areItemsTheSame(Media oldItem, Media newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Media oldItem, Media newItem) {
                    return oldItem.equals(newItem);
                }
            };



    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemStreamingBinding binding;


        ItemViewHolder(@NonNull ItemStreamingBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final int position) {

            final Media media = getItem(position);

            Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());

            defaultBackgroundColor = ContextCompat.getColor(context, R.color.white);

            selectedForegroundColor = ContextCompat.getColor(context, R.color.red_A700);

            int strikeApply = media.getVip() == 1 ? selectedForegroundColor : defaultBackgroundColor;

            binding.itemMovieImage.setBorderColor(strikeApply);

            binding.movietitle.setText(media.getName());


            binding.rootLayout.setOnClickListener(v -> {

                Intent intent = new Intent(context, StreamingetailsActivity.class);
                intent.putExtra(ARG_MOVIE, media);
                context.startActivity(intent);


            });


        }

    }
}
