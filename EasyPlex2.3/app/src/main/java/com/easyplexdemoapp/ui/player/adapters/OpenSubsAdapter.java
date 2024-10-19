package com.easyplexdemoapp.ui.player.adapters;

import static com.easyplexdemoapp.util.Constants.SUBSTITLE_LOCATION;
import static com.easyplexdemoapp.util.Constants.SUBSTITLE_SUB_FILENAME_ZIP;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME2;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME4;
import static com.google.android.exoplayer2.util.Log.i;
import static java.lang.String.valueOf;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.substitles.MediaSubstitle;
import com.easyplexdemoapp.databinding.RowSubstitleBinding;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.util.DownloadFileAsync;
import com.easyplexdemoapp.util.Tools;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Adapter for Movie or Serie Substitles.
 *
 * @author Yobex.
 */
public class OpenSubsAdapter extends RecyclerView.Adapter<OpenSubsAdapter.SubstitlesViewHolder> {

    private List<MediaSubstitle> mediaSubstitles;
    private MediaModel mMediaModel;
    private Context context;
    ClickDetectListner clickDetectListner;
    private String subsExtracted;


    public void addSubtitle(List<MediaSubstitle> castList, ClickDetectListner clickDetectListner, Context context) {
        this.mediaSubstitles = castList;
        notifyDataSetChanged();
        this.clickDetectListner = clickDetectListner;
        this.context = context;

    }

    @NonNull
    @Override
    public SubstitlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RowSubstitleBinding binding = RowSubstitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new SubstitlesViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SubstitlesViewHolder holder, int position) {
        holder.onBind(position);
    }



    @Override
    public int getItemCount() {
        if (mediaSubstitles != null) {
            return mediaSubstitles.size();
        } else {
            return 0;
        }
    }

    class SubstitlesViewHolder extends RecyclerView.ViewHolder {

        private final RowSubstitleBinding binding;

        SubstitlesViewHolder (@NonNull RowSubstitleBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        void onBind(final int position) {

            final MediaSubstitle mediaSubstitle = mediaSubstitles.get(position);

        }
    }

}
