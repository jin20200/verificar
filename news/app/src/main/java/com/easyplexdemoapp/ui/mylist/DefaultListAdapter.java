package com.easyplexdemoapp.ui.mylist;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.easyplexdemoapp.util.Constants.ARG_MOVIE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.media.StatusFav;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.ui.animes.AnimeDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.ui.seriedetails.SerieDetailsActivity;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.Tools;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DefaultListAdapter extends PagedListAdapter<Media, DefaultListAdapter.ItemViewHolder> {

    private final Context context;
    private final int animationType;
    private final SettingsManager settingsManager;
    private AuthRepository authRepository;
    private final DeleteFavoriteDetectListner deleteFavoriteDetectListner;



    public DefaultListAdapter(Context context, int animationType, SettingsManager settingsManager,AuthRepository authRepository,DeleteFavoriteDetectListner deleteFavoriteDetectListner) {
        super(ITEM_CALLBACK);
        this.context = context;
        this.animationType = animationType;
        this.settingsManager = settingsManager;
        this.authRepository = authRepository;
        this.deleteFavoriteDetectListner= deleteFavoriteDetectListner;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fav, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Media media = getItem(position);


        if (media != null) {


            Tools.onLoadMediaCoverAdapters(context,holder.imageView,media.getPosterPath());

            setAnimation(holder.itemView, position);


            if ("anime".equals(media.getType())) {
                holder.title.setText(media.getName());
            } else if ("serie".equals(media.getType())) {
                holder.title.setText(media.getName());
            } else if ("movie".equals(media.getType())) {
                holder.title.setText(media.getTitle());
            }else if ("streaming".equals(media.getType())) {
                holder.title.setText(media.getName());

            }


            holder.movietype.setOnClickListener(v -> {

                if ("anime".equals(media.getType())) {
                    Intent intent = new Intent(context, AnimeDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("serie".equals(media.getType())) {
                    Intent intent = new Intent(context, SerieDetailsActivity.class);
                    intent.putExtra(ARG_MOVIE, media);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if ("movie".equals(media.getType())) {
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);
                }else if ("streaming".equals(media.getType())) {
                    Intent intent = new Intent(context, StreamingetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ARG_MOVIE, media);
                    context.startActivity(intent);

                }

            });


            holder.deleteFromHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_remove_movie_from_history);
                    dialog.setCancelable(false);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());

                    lp.gravity = Gravity.BOTTOM;
                    lp.width = MATCH_PARENT;
                    lp.height = MATCH_PARENT;

                    TextView movieName = dialog.findViewById(R.id.movietitle);
                    TextView movieoverview = dialog.findViewById(R.id.text_overview_label);

                    movieName.setText(media.getName());

                    movieoverview.setText(context.getString(R.string.are_you_sure_to_delete_from_your_watching_history)+" "+media.getName()+context.getString(R.string.from_your_lists));

                    dialog.findViewById(R.id.view_delete_from_history).setOnClickListener(v12 -> {

                        authRepository.getDeleteStreamingOnline(media.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .cache()
                                .subscribe(new Observer<>() {
                                    @Override
                                    public void onSubscribe(@NotNull Disposable d) {

                                        //

                                    }

                                    @Override
                                    public void onNext(@NotNull StatusFav statusFav) {

                                        Toast.makeText(context, "Removed From Watchlist", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(@NotNull Throwable e) {

                                        //
                                    }

                                    @Override
                                    public void onComplete() {

                                        //

                                    }
                                });

                        dialog.dismiss();

                        new Handler(Looper.getMainLooper()).postDelayed(() -> deleteFavoriteDetectListner.onMediaDeletedSuccess(true), 300);
                    });


                    dialog.findViewById(R.id.text_view_cancel).setOnClickListener(v1 -> dialog.dismiss());

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    dialog.findViewById(R.id.bt_close).setOnClickListener(x ->
                            dialog.dismiss());
                    dialog.show();
                    dialog.getWindow().setAttributes(lp);
                }
            });


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


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                onAttach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }



    private int lastPosition = -1;
    private boolean onAttach = true;


    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, onAttach ? position : -1, animationType);
            lastPosition = position;
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final LinearLayout movietype;
        final TextView mgenres;

        final TextView substitle;

        final TextView type;

        final TextView title;

        final ImageButton deleteFromHistory;
        public ItemViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_movie_image);
            movietype = itemView.findViewById(R.id.rootLayout);
            mgenres = itemView.findViewById(R.id.mgenres);
            substitle = itemView.findViewById(R.id.substitle);
            type = itemView.findViewById(R.id.type);
            deleteFromHistory = itemView.findViewById(R.id.deleteFromHistory);

            title = itemView.findViewById(R.id.movietitle);
        }
    }
}
