package com.easyplexdemoapp.ui.mylist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.local.entity.Stream;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ItemShowStreamingHomeBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.streaming.StreamingetailsActivity;
import com.easyplexdemoapp.util.Constants;
import com.easyplexdemoapp.util.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Adapter for Movie Casts.
 *
 * @author Yobex.
 */
public class StreamMyListdapter extends RecyclerView.Adapter<StreamMyListdapter.MainViewHolder> {

    private List<Stream> castList;
    private Context context;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MediaRepository mediaRepository;
    private final SettingsManager settingsManager;

    public StreamMyListdapter(MediaRepository mediaRepository,SettingsManager settingsManager) {
        this.mediaRepository = mediaRepository;
        this.settingsManager = settingsManager;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addToContent(List<Stream> castList, Context context) {
        this.castList = castList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StreamMyListdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ItemShowStreamingHomeBinding binding = ItemShowStreamingHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new StreamMyListdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamMyListdapter.MainViewHolder holder, int position) {
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

        private final ItemShowStreamingHomeBinding binding;


        MainViewHolder(@NonNull ItemShowStreamingHomeBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void onBind(final int position) {

            final Stream myListMedia = castList.get(position);


            binding.movietitle.setText(myListMedia.getTitle());

            binding.deleteFromHistory.setOnClickListener(v -> {

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


                movieName.setText(myListMedia.getTitle());

                movieoverview.setText(context.getString(R.string.are_you_sure_to_delete_from_your_watching_history)+" "+myListMedia.getTitle()+context.getString(R.string.from_your_history));

                dialog.findViewById(R.id.view_delete_from_history).setOnClickListener(v12 -> {

                    compositeDisposable.add(Completable.fromAction(() -> mediaRepository.removeStreamFavorite(myListMedia))
                            .subscribeOn(Schedulers.io())
                            .subscribe());
                    dialog.dismiss();

                });


                dialog.findViewById(R.id.text_view_cancel).setOnClickListener(v1 -> dialog.dismiss());

                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                        dialog.dismiss());


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            });

            binding.rootLayout.setOnClickListener(view -> mediaRepository.getStream(myListMedia.getId(),settingsManager.getSettings().getApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@NotNull Media movieDetail) {

                            Intent intent = new Intent(context, StreamingetailsActivity.class);
                            intent.putExtra(Constants.ARG_MOVIE, movieDetail);
                            context.startActivity(intent);


                        }


                        @Override
                        public void onError(@NotNull Throwable e) {

                            //
                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    }));



            Tools.onLoadMediaCover(context,binding.imageViewShowCard, myListMedia.getBackdropPath());

        }
    }
}
