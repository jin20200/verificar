package com.easyplexdemoapp.ui.moviedetails.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.databinding.ListItemCastBinding;
import com.easyplexdemoapp.ui.casts.CastDetailsActivity;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.Tools;
import java.util.List;
import javax.inject.Inject;

import static com.easyplexdemoapp.util.Constants.ARG_CAST;


/**
 * Adapter for Movie Casts.
 *
 * @author Yobex.
 */
public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<Cast> castList;
    private final Context context;


    @Inject
    SettingsManager settingsManager;


    public CastAdapter(SettingsManager settingsManager, Context context) {

        this.settingsManager = settingsManager;
        this.context = context;

    }



    public void addCasts(List<Cast> castList) {
        this.castList = castList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        ListItemCastBinding binding = ListItemCastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CastAdapter.CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
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

    class CastViewHolder extends RecyclerView.ViewHolder {

        private final ListItemCastBinding binding;


        CastViewHolder(@NonNull ListItemCastBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void onBind(final int position) {

            final Cast cast = castList.get(position);


            String imageUrl = settingsManager.getSettings().getDefaultCastOption() !=null && !settingsManager.getSettings().getDefaultCastOption().equals("IMDB") ? cast.getProfilePath() : settingsManager.getSettings().getImdbCoverPath() + cast.getProfilePath();


            boolean fromImdb = settingsManager.getSettings().getDefaultCastOption() !=null && !settingsManager.getSettings().getDefaultCastOption().equals("IMDB");


            GlideApp.with(context).asDrawable().load(imageUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.imageCast);


            binding.rootLayout.setOnClickListener(v -> {

                if (fromImdb){

                    return;
                }

                Intent intent = new Intent(context, CastDetailsActivity.class);
                intent.putExtra(ARG_CAST, cast);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            });

            binding.castName.setText(cast.getName());
            binding.caracter.setText(cast.getCharacter());

        }
    }
}
