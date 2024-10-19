package com.easyplexdemoapp.ui.search;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.datasource.medialibrary.MediaLibraryDataSourceFactory;
import com.easyplexdemoapp.data.datasource.stream.StreamDataSource;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.genres.Genre;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowDiscoverStyleBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class DiscoverStyleAdapter extends RecyclerView.Adapter<DiscoverStyleAdapter.MainViewHolder> {

    private List<Genre> castList;
    private Context context;
    private final MediaRepository mediaRepository;
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    public final MutableLiveData<String> mediatype = new MutableLiveData<>();
    private final ByGenreAdapter byGenreAdapter;

    public DiscoverStyleAdapter(MediaRepository mediaRepository,ByGenreAdapter byGenreAdapter) {
        this.mediaRepository = mediaRepository;
        this.byGenreAdapter = byGenreAdapter;
    }


    public void addMain(List<Genre> mediaList, Context context) {
        this.castList = mediaList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscoverStyleAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowDiscoverStyleBinding binding = RowDiscoverStyleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DiscoverStyleAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverStyleAdapter.MainViewHolder holder, int position) {
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

        private final RowDiscoverStyleBinding binding;

        MainViewHolder(@NonNull RowDiscoverStyleBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Genre genre = castList.get(position);

            binding.genreName.setText(genre.getName());


            GlideApp.with(context).asDrawable().load(genre.getLogoPath())
                    .fitCenter()
                    .placeholder(R.drawable.discover_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageBackground);





            binding.discoverCardView.setOnClickListener(v -> {


                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_movies_by_genres);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());

                lp.gravity = Gravity.BOTTOM;
                lp.width = MATCH_PARENT;
                lp.height = MATCH_PARENT;


                RecyclerView recyclerView = dialog.findViewById(R.id.rv_movies_genres);
                TextView mGenreType = dialog.findViewById(R.id.movietitle);


                mGenreType.setText(genre.getName());
                searchQuery.setValue(String.valueOf(genre.getId()));
                getMediaLibraryPagedList().observe((BaseActivity) context, byGenreAdapter::submitList);
                recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
                recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(context, 0), true));
                recyclerView.setAdapter(byGenreAdapter);



                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                dialog.dismiss());


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            });

        }


        public LiveData<PagedList<Media>> getMediaLibraryPagedList() {
            return Transformations.switchMap(searchQuery, query -> {
                MediaLibraryDataSourceFactory factory = mediaRepository.mediaLibraryListDataSourceFactory(query,"");
                return new LivePagedListBuilder<>(factory, config).build();
            });
        }


        final PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(StreamDataSource.PAGE_SIZE)
                        .setPrefetchDistance(StreamDataSource.PAGE_SIZE)
                        .setInitialLoadSizeHint(StreamDataSource.PAGE_SIZE)
                        .build();
    }
}
