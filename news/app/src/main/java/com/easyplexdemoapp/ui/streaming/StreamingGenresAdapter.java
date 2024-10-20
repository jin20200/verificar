package com.easyplexdemoapp.ui.streaming;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
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
import com.easyplexdemoapp.databinding.ItemStreamingTwolinesBinding;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

public class StreamingGenresAdapter extends PagedListAdapter<Media, StreamingGenresAdapter.ItemViewHolder> {

    private final Context context;

    private int selectedForegroundColor = -1;
    private int defaultBackgroundColor = -1;


    public StreamingGenresAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemStreamingTwolinesBinding binding = ItemStreamingTwolinesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new StreamingGenresAdapter.ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.onBind(position);


        }


    private static final DiffUtil.ItemCallback<Media> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(Media oldItem, Media newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Media oldItem, @NonNull Media newItem) {
                    return oldItem.equals(newItem);
                }
            };



    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ItemStreamingTwolinesBinding binding;


        ItemViewHolder(@NonNull ItemStreamingTwolinesBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final int position) {


            final Media media = getItem(position);

            defaultBackgroundColor = ContextCompat.getColor(context, R.color.white);

            selectedForegroundColor = ContextCompat.getColor(context, R.color.red_A700);


            Tools.onLoadMediaCoverAdapters(context,binding.itemMovieImage, media.getPosterPath());

            int strikeApply = media.getVip() == 1 ? selectedForegroundColor : defaultBackgroundColor;

            binding.itemMovieImage.setBorderColor(strikeApply);

            binding.movietitle.setText(media.getName());


            binding.rootLayout.setOnClickListener(v -> {

                Intent intent = new Intent(context, StreamingetailsActivity.class);
                intent.putExtra(ARG_MOVIE, media);
                context.startActivity(intent);

            });

            String vip = media.getVip() == 1 ? context.getString(R.string.vip) : "";
            binding.qualities.setText(vip);
            binding.qualities.setVisibility(media.getVip() == 1 ? View.VISIBLE : View.GONE);


        }
    }
}
