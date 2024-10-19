package com.easyplexdemoapp.ui.collections;

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
import com.easyplexdemoapp.data.datasource.collections.CollectionsListDataSourceFactory;
import com.easyplexdemoapp.data.datasource.genreslist.ByGenreListDataSource;
import com.easyplexdemoapp.data.datasource.languages.LanguagesListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.collections.MediaCollection;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowCollectionBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.MainViewHolder> {

    private List<MediaCollection> castList;
    private Context context;
    private final MediaRepository mediaRepository;
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final ByGenreAdapter byGenreAdapter;

    public CollectionsAdapter(MediaRepository mediaRepository, ByGenreAdapter byGenreAdapter) {
        this.mediaRepository = mediaRepository;
        this.byGenreAdapter = byGenreAdapter;
    }


    public void addMain(List<MediaCollection> mediaList, Context context) {
        this.castList = mediaList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CollectionsAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowCollectionBinding binding = RowCollectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CollectionsAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsAdapter.MainViewHolder holder, int position) {
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

        private final RowCollectionBinding binding;

        MainViewHolder(@NonNull RowCollectionBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final MediaCollection mediaCollection = castList.get(position);


            GlideApp.with(context).asDrawable().load(mediaCollection.getPosterPath())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.itemCastImage);


            String name = mediaCollection.getName();
            if (name != null && !name.isEmpty()) {
                String[] words = name.split("\\s+");

                StringBuilder formattedText = new StringBuilder();

                for (String word : words) {
                    formattedText.append(word).append("\n");
                }

                binding.movietitle.setText(formattedText.toString().trim());


                // Use formattedText.toString() to get the formatted text
            } else {
                binding.movietitle.setText("");

            }




            String currentLangName = mediaCollection.getName();

            binding.rootLayout.setOnClickListener(v -> {



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

                mGenreType.setText(currentLangName);

                searchQuery.setValue(String.valueOf(mediaCollection.getId()));

                getCollectionByIdPagedList().observe(((BaseActivity)context), genresList -> {

                    recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(context, 0), true));
                    byGenreAdapter.submitList(genresList);
                    recyclerView.setAdapter(byGenreAdapter);


                });



                dialog.show();
                dialog.getWindow().setAttributes(lp);

                dialog.findViewById(R.id.bt_close).setOnClickListener(x ->

                        dialog.dismiss());
                dialog.show();
                dialog.getWindow().setAttributes(lp);

            });

        }

    }



    PagedList.Config config =
            (new PagedList.Config.Builder())
                    .setEnablePlaceholders(false)
                    .setPageSize(ByGenreListDataSource.PAGE_SIZE)
                    .setPrefetchDistance(ByGenreListDataSource.PAGE_SIZE)
                    .setInitialLoadSizeHint(ByGenreListDataSource.PAGE_SIZE)
                    .build();

    public LiveData<PagedList<Media>> getCollectionByIdPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            CollectionsListDataSourceFactory factory = mediaRepository.collectionsListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }
}
