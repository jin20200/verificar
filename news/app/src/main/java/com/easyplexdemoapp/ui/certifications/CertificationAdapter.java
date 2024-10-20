package com.easyplexdemoapp.ui.certifications;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
import com.easyplexdemoapp.data.model.certifications.Certification;
import com.easyplexdemoapp.data.model.languages.Languages;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.RowCertificationBinding;
import com.easyplexdemoapp.databinding.RowLanguageBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.home.adapters.ByGenreAdapter;
import com.easyplexdemoapp.ui.moviedetails.MovieDetailsActivity;
import com.easyplexdemoapp.util.GlideApp;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import java.util.List;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class CertificationAdapter extends RecyclerView.Adapter<CertificationAdapter.MainViewHolder> {

    private List<Certification> castList;
    private Context context;



    public void addMain(List<Certification> mediaList, Context context) {
        this.castList = mediaList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CertificationAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowCertificationBinding binding = RowCertificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CertificationAdapter.MainViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificationAdapter.MainViewHolder holder, int position) {
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

        private final RowCertificationBinding binding;

        MainViewHolder(@NonNull RowCertificationBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Certification certification = castList.get(position);


            StringBuilder certificationList = new StringBuilder();

            if (certificationList.length() > 0) {
                certificationList.append(", ");
            }
            certificationList.append(certification.getCountryCode()).append(certification.getCertification());


            binding.viewMovieCertification.setText(certificationList);


            binding.viewMovieCertification.setOnClickListener(v -> Toast.makeText(context, "" + certification.getMeaning(), Toast.LENGTH_SHORT).show());


        }
    }

}
