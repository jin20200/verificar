package com.easyplexdemoapp.ui.streaming;

import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.databinding.ItemStreamingBinding;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

/**
 * Adapter for  Streaming Channels
 *
 * @author Yobex.
 */
public class LatestStreamingAdapter extends RecyclerView.Adapter<LatestStreamingAdapter.StreamingViewHolder> {

    private List<Media> streamingList;
    private Context context;


    private int selectedForegroundColor = -1;
    private int defaultBackgroundColor = -1;


    public void addStreaming(Context context,List<Media> castList) {
        this.streamingList = castList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StreamingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemStreamingBinding binding = ItemStreamingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new LatestStreamingAdapter.StreamingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamingViewHolder holder, int position) {
        holder.onBind(position);

    }

    @Override
    public int getItemCount() {
        if (streamingList != null) {
            return streamingList.size();
        } else {
            return 0;
        }
    }

    class StreamingViewHolder extends RecyclerView.ViewHolder {

        private final ItemStreamingBinding binding;


        StreamingViewHolder(@NonNull ItemStreamingBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final int position) {

            defaultBackgroundColor = ContextCompat.getColor(context, R.color.white);

            selectedForegroundColor = ContextCompat.getColor(context, R.color.red_A700);


            final Media media = streamingList.get(position);

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
