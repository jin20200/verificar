package com.easyplexdemoapp.ui.languages;

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
import com.easyplexdemoapp.data.datasource.genreslist.ByGenreListDataSource;
import com.easyplexdemoapp.data.datasource.languages.LanguagesListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.languages.Languages;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowLanguageBinding;
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
public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.MainViewHolder> {

    private List<Languages> castList;
    private Context context;
    private final MediaRepository mediaRepository;
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final ByGenreAdapter byGenreAdapter;



    public LanguagesAdapter(MediaRepository mediaRepository, ByGenreAdapter byGenreAdapter) {
        this.mediaRepository = mediaRepository;
        this.byGenreAdapter = byGenreAdapter;
    }


    public void addMain(List<Languages> mediaList, Context context) {
        this.castList = mediaList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguagesAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowLanguageBinding binding = RowLanguageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new LanguagesAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagesAdapter.MainViewHolder holder, int position) {
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

        private final RowLanguageBinding binding;

        MainViewHolder(@NonNull RowLanguageBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Languages languages = castList.get(position);


            String currentLangName = languages.getName() == null ? languages.getEnglishName() : languages.getName();


            Tools.onLoadMediaCoverAdapters(context,binding.itemCastImage, languages.getLogoPath());


            binding.casttitle.setText(currentLangName);

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

                searchQuery.setValue(String.valueOf(languages.getIso6391()));

                getByGenresitemPagedList().observe(((BaseActivity)context), genresList -> {

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

    public LiveData<PagedList<Media>> getByGenresitemPagedList() {
        return Transformations.switchMap(searchQuery, query -> {
            LanguagesListDataSourceFactory factory = mediaRepository.langsListDataSourceFactory(query);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }
}
