package com.easyplexdemoapp.ui.player.adapters;

import static com.easyplexdemoapp.util.Constants.SUBSTITLE_LOCATION;
import static com.easyplexdemoapp.util.Constants.SUBSTITLE_SUB_FILENAME_ZIP;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME2;
import static com.easyplexdemoapp.util.Constants.ZIP_FILE_NAME4;
import static com.google.android.exoplayer2.util.Log.*;
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
public class SubstitlesAdapter extends RecyclerView.Adapter<SubstitlesAdapter.SubstitlesViewHolder> {

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

            String subs = mediaSubstitle.getLink();

            String substitleLanguage = mediaSubstitle.getLang();

            String substitleType = mediaSubstitle.getType();

            binding.eptitle.setText(mediaSubstitle.getLang());

            binding.eptitle.setOnClickListener(v -> {

                if (mediaSubstitle.getZip() == 1) {

                    DownloadFileAsync download = new DownloadFileAsync(
                            context
                                    .getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath())
                                    + SUBSTITLE_SUB_FILENAME_ZIP, file -> {
                        i("TAG", "file download completed");
                        // check unzip file now
                        ZipFile zipFile;
                        zipFile = new ZipFile("subs.zip");
                        FileHeader fileHeader;
                        fileHeader = zipFile.getFileHeader(
                                context.getExternalFilesDir(Environment.getDataDirectory()
                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP);
                        if (fileHeader != null) {
                            zipFile.removeFile(fileHeader);
                        } else {

                            if (substitleType !=null && !substitleType.isEmpty() && substitleType.equals("vtt")) {

                                List<FileHeader> fileHeaders = new ZipFile(context.getExternalFilesDir(Environment.getDataDirectory()
                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                fileHeaders.forEach(fileHeaderx -> {

                                    try {
                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                valueOf(context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                , ZIP_FILE_NAME2);
                                    } catch (ZipException e) {
                                        e.printStackTrace();
                                    }
                                    i("TAG", "file unzip completed");
                                });


                            }else if (substitleType !=null && !substitleType.isEmpty() && substitleType.equals("ass")) {

                                List<FileHeader> fileHeaders = new ZipFile(context.getExternalFilesDir(Environment.getDataDirectory()
                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                fileHeaders.forEach(fileHeaderx -> {

                                    try {
                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                valueOf(context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                , ZIP_FILE_NAME4);
                                    } catch (ZipException e) {
                                        e.printStackTrace();
                                    }
                                    i("TAG", "file unzip completed");
                                });


                            }else if (substitleType !=null && !substitleType.isEmpty() && substitleType.equals("srt")) {


                                List<FileHeader> fileHeaders = new ZipFile(context.getExternalFilesDir(Environment.getDataDirectory()
                                        .getAbsolutePath()) + SUBSTITLE_SUB_FILENAME_ZIP).getFileHeaders();
                                fileHeaders.forEach(fileHeaderx -> {

                                    try {
                                        new ZipFile(file, null).extractFile(fileHeaderx.getFileName(),
                                                valueOf(context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()))
                                                , ZIP_FILE_NAME);
                                    } catch (ZipException e) {
                                        e.printStackTrace();
                                    }
                                    i("TAG", "file unzip completed");
                                });

                            }else {

                                Toast.makeText(context, R.string.cannot_load_subs, Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                    download.execute(mediaSubstitle.getLink());

                    Toast.makeText(context, "The " + mediaSubstitle.getLang() + context.getString(R.string.ready_5sec), Toast.LENGTH_LONG).show();

                    clickDetectListner.onSubstitleClicked(true);

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {

                        if (substitleType !=null && !substitleType.isEmpty() && substitleType.equals("ass")) {

                            subsExtracted = SUBSTITLE_LOCATION + context.getPackageName() + "/files/data/" + ZIP_FILE_NAME4;


                        } else  if (substitleType !=null && !substitleType.isEmpty() && substitleType.equals("vtt"))  {

                            subsExtracted = SUBSTITLE_LOCATION + context.getPackageName() + "/files/data/" + ZIP_FILE_NAME2;

                        }else {

                            subsExtracted = SUBSTITLE_LOCATION + context.getPackageName() + "/files/data/" + ZIP_FILE_NAME;

                        }


                        if (((EasyPlexMainPlayer) context).getPlayerController().getMediaType().equals("0") && substitleType != null) {

                            String id = ((EasyPlexMainPlayer) context).getPlayerController().getVideoID();
                            String type = ((EasyPlexMainPlayer) context).getPlayerController().getMediaType();
                            String currentQuality = ((EasyPlexMainPlayer) context).getPlayerController().getVideoCurrentQuality();
                            String artwork = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getMediaPoster()));
                            String name = ((EasyPlexMainPlayer) context).getPlayerController().getCurrentVideoName();
                            String videoUrl = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getVideoUrl()));
                            mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                    valueOf(Tools.convertToUTF(context, Uri.parse(subsExtracted))), null, null
                                    , null, null, null, null, null,
                                    null, ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim()
                                    , ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHlsFormat(), substitleType, ((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory(), ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHasRecap(), ((EasyPlexMainPlayer) context).getPlayerController().getCurrentStartRecapIn()
                                    , ((EasyPlexMainPlayer) context).getPlayerController().getMediaGenre(),
                                    null,
                                    ((EasyPlexMainPlayer) context).getPlayerController().getVoteAverage(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                            ((EasyPlexMainPlayer) context).update(mMediaModel);
                            ((EasyPlexMainPlayer) context).getPlayerController().isSubtitleEnabled(true);
                            clickDetectListner.onSubstitleClicked(true);
                            ((EasyPlexMainPlayer) context).getPlayerController().subtitleCurrentLang(substitleLanguage);

                        } else {


                            String id = ((EasyPlexMainPlayer) context).getPlayerController().getVideoID();
                            String externalId = ((EasyPlexMainPlayer) context).getPlayerController().getMediaSubstitleName();
                            String type = ((EasyPlexMainPlayer) context).getPlayerController().getMediaType();
                            String currentQuality = ((EasyPlexMainPlayer) context).getPlayerController().getVideoCurrentQuality();
                            String artwork = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getMediaPoster()));
                            String name = ((EasyPlexMainPlayer) context).getPlayerController().getCurrentVideoName();
                            mMediaModel = MediaModel.media(id, externalId, currentQuality, type, name, valueOf(((EasyPlexMainPlayer) context).getPlayerController().getVideoUrl()), artwork,
                                    valueOf(Tools.convertToUTF(context, Uri.parse(subsExtracted))), Integer.parseInt(((EasyPlexMainPlayer) context).getPlayerController().getEpID()), null
                                    , ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpTmdbNumber(), ((EasyPlexMainPlayer) context).getPlayerController().getSeaonNumber(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getEpName(), ((EasyPlexMainPlayer) context).getPlayerController().getSeaonNumber(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpisodePosition(), ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpTmdbNumber(), ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHlsFormat(),
                                    substitleType, ((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId()
                                    , ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHasRecap(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getCurrentStartRecapIn(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getMediaGenre(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getSerieName(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getVoteAverage(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                    ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                            ((EasyPlexMainPlayer) context).update(mMediaModel);
                            ((EasyPlexMainPlayer) context).getPlayerController().isSubtitleEnabled(true);
                            clickDetectListner.onSubstitleClicked(true);
                            ((EasyPlexMainPlayer) context).getPlayerController().subtitleCurrentLang(substitleLanguage);

                        }


                    }, 4000);
                } else {


                    if (((EasyPlexMainPlayer) context).getPlayerController().getMediaType().equals("0") && substitleType != null) {

                        String id = ((EasyPlexMainPlayer) context).getPlayerController().getVideoID();
                        String type = ((EasyPlexMainPlayer) context).getPlayerController().getMediaType();
                        String currentQuality = ((EasyPlexMainPlayer) context).getPlayerController().getVideoCurrentQuality();
                        String artwork = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getMediaPoster()));
                        String name = ((EasyPlexMainPlayer) context).getPlayerController().getCurrentVideoName();
                        String videoUrl = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getVideoUrl()));
                        mMediaModel = MediaModel.media(id, substitleLanguage, currentQuality, type, name, videoUrl, artwork,
                                valueOf(Tools.convertToUTF(context, Uri.parse(subs))), null, null
                                , null, null, null, null, null,
                                null, ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim()
                                , ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHlsFormat(),
                                substitleType, ((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHasRecap(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentStartRecapIn()
                                , ((EasyPlexMainPlayer) context).getPlayerController().getMediaGenre(),
                                null, ((EasyPlexMainPlayer) context).getPlayerController().getVoteAverage(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                        ((EasyPlexMainPlayer) context).update(mMediaModel);
                        ((EasyPlexMainPlayer) context).getPlayerController().isSubtitleEnabled(true);
                        clickDetectListner.onSubstitleClicked(true);
                        ((EasyPlexMainPlayer) context).getPlayerController().subtitleCurrentLang(substitleLanguage);

                    } else {


                        String id = ((EasyPlexMainPlayer) context).getPlayerController().getVideoID();
                        String externalId = ((EasyPlexMainPlayer) context).getPlayerController().getMediaSubstitleName();
                        String type = ((EasyPlexMainPlayer) context).getPlayerController().getMediaType();
                        String currentQuality = ((EasyPlexMainPlayer) context).getPlayerController().getVideoCurrentQuality();
                        String artwork = (valueOf(((EasyPlexMainPlayer) context).getPlayerController().getMediaPoster()));
                        String name = ((EasyPlexMainPlayer) context).getPlayerController().getCurrentVideoName();
                        mMediaModel = MediaModel.media(id, externalId, currentQuality, type, name, valueOf(((EasyPlexMainPlayer) context).getPlayerController().getVideoUrl()), artwork,
                                valueOf(Tools.convertToUTF(context, Uri.parse(subs))), Integer.parseInt(((EasyPlexMainPlayer) context).getPlayerController().getEpID()), null
                                , ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpTmdbNumber(), ((EasyPlexMainPlayer) context).getPlayerController().getSeaonNumber(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getEpName(), ((EasyPlexMainPlayer) context).getPlayerController().getSeaonNumber(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpisodePosition(), ((EasyPlexMainPlayer) context).getPlayerController().getCurrentEpTmdbNumber(), ((EasyPlexMainPlayer) context).getPlayerController().isMediaPremuim(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHlsFormat(),
                                substitleType, ((EasyPlexMainPlayer) context).getPlayerController().getCurrentExternalId()
                                , ((EasyPlexMainPlayer) context).getPlayerController().getMediaCoverHistory(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentHasRecap(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getCurrentStartRecapIn(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getMediaGenre(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getSerieName(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getVoteAverage(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                        ((EasyPlexMainPlayer) context).update(mMediaModel);
                        ((EasyPlexMainPlayer) context).getPlayerController().isSubtitleEnabled(true);
                        clickDetectListner.onSubstitleClicked(true);
                        ((EasyPlexMainPlayer) context).getPlayerController().subtitleCurrentLang(substitleLanguage);

                    }
                }


            });

        }
    }


    public Serializable getFirstItem() {
        if (mediaSubstitles != null) {
            return mediaSubstitles.get(0).getLink();
        } else {
            return 0;
        }
    }

}
