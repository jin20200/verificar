package com.easyplexdemoapp.ui.networks;

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
import com.easyplexdemoapp.data.datasource.networks.NetworksListDataSourceFactory;
import com.easyplexdemoapp.data.local.entity.Media;
import com.easyplexdemoapp.data.model.networks.Network;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowItemCategoryBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class NetworksAdapter extends RecyclerView.Adapter<NetworksAdapter.MainViewHolder> {

    private List<Network> castList;
    private Context context;
    private final MediaRepository mediaRepository;
    public final MutableLiveData<String> searchQuery = new MutableLiveData<>();
    private final ByGenreAdapter byGenreAdapter;

    public NetworksAdapter(MediaRepository mediaRepository,ByGenreAdapter byGenreAdapter) {
        this.mediaRepository = mediaRepository;
        this.byGenreAdapter = byGenreAdapter;
    }


    public void addMain(List<Network> mediaList, Context context) {
        this.castList = mediaList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NetworksAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowItemCategoryBinding binding = RowItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new NetworksAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworksAdapter.MainViewHolder holder, int position) {
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

        private final RowItemCategoryBinding binding;

        MainViewHolder(@NonNull RowItemCategoryBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Network network = castList.get(position);

            GlideApp.with(context).asDrawable().load(network.getLogoPath())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.image);


            binding.lytParent.setOnClickListener(v -> {


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

                mGenreType.setText(network.getName());

                searchQuery.setValue(String.valueOf(network.getId()));
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
            NetworksListDataSourceFactory factory = mediaRepository.networksListDataSourceFactory(query,false,false);
            return new LivePagedListBuilder<>(factory, config).build();
        });
    }
}
