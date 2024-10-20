package com.easyplexdemoapp.ui.player.adapters;

import static com.easyplexdemoapp.util.Constants.SERVER_BASE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.databinding.RowSubstitleBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.util.Constants;
import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Movie Qualities.
 *
 * @author Yobex.
 */
public class SerieQualitiesAdapter extends RecyclerView.Adapter<SerieQualitiesAdapter.SerieQualitiesViewHolder> {

    private List<MediaStream> episodeStreams;
    private MediaModel mMediaModel;
    ClickDetectListner clickDetectListner;
    private SettingsManager settingsManager;
    private Context context;


    public void addQuality(List<MediaStream> castList, ClickDetectListner clickDetectListner,SettingsManager settingsManager,Context context) {
        this.episodeStreams = castList;
        this.context = context;
        this.settingsManager = settingsManager;
        notifyDataSetChanged();
        this.clickDetectListner = clickDetectListner;

    }

    @NonNull
    @Override
    public SerieQualitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        RowSubstitleBinding binding = RowSubstitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new SerieQualitiesAdapter.SerieQualitiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SerieQualitiesViewHolder holder, int position) {
        holder.onBind(position);
    }



    @Override
    public int getItemCount() {
        if (episodeStreams != null) {
            return episodeStreams.size();
        } else {
            return 0;
        }
    }

    class SerieQualitiesViewHolder extends RecyclerView.ViewHolder {

        private final RowSubstitleBinding binding;

        SerieQualitiesViewHolder (@NonNull RowSubstitleBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }
        @SuppressLint("StaticFieldLeak")
        void onBind(final int position) {

            final MediaStream mediaStream = episodeStreams.get(position);

            binding.eptitle.setText(mediaStream.getServer());

            binding.eptitle.setOnClickListener(v -> {

                if (mediaStream.getEmbed() == 1) {

                    Intent intent = new Intent(context, EmbedActivity.class);
                    intent.putExtra(Constants.MOVIE_LINK, mediaStream.getLink());
                    context.startActivity(intent);


                }else if (mediaStream.getSupportedHosts() == 1) {


                    startStreamFromSupportedHosts(mediaStream);



                }else {


                    String id = ((EasyPlexMainPlayer)context).getPlayerController().getVideoID();
                    String externalId = ((EasyPlexMainPlayer)context).getPlayerController().getMediaSubstitleName();
                    String type = ((EasyPlexMainPlayer)context).getPlayerController().getMediaType();
                    String currentQuality = ((EasyPlexMainPlayer)context).getPlayerController().getVideoCurrentQuality();
                    String artwork = (String.valueOf(((EasyPlexMainPlayer)context).getPlayerController().getMediaPoster())) ;
                    String name = ((EasyPlexMainPlayer)context).getPlayerController().getCurrentVideoName();
                    mMediaModel = MediaModel.media(id,externalId,currentQuality,type,name, mediaStream.getLink(), artwork, null,null,null
                            ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().getSeaonNumber(),
                            ((EasyPlexMainPlayer)context).getPlayerController().getEpName(),null,
                            ((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpisodePosition(),((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                            mediaStream.getHls(),
                            null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                            ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                            ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap(),
                            ((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                            ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre()
                            ,((EasyPlexMainPlayer)context).getPlayerController().getSerieName(),
                            ((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage(),
                            ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),
                            ((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                            ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                    ((EasyPlexMainPlayer)context).update(mMediaModel);
                    clickDetectListner.onQualityClicked(true);

                }



            });

        }

        private void startStreamFromSupportedHosts(MediaStream mediaStream) {

            EasyPlexSupportedHosts easyPlexSupportedHosts = new EasyPlexSupportedHosts(context);

            if (settingsManager.getSettings().getHxfileApiKey() !=null && !settingsManager.getSettings().getHxfileApiKey().isEmpty())  {

                easyPlexSupportedHosts.setApikey(settingsManager.getSettings().getHxfileApiKey());
            }

            easyPlexSupportedHosts.setMainApiServer(SERVER_BASE_URL);

            easyPlexSupportedHosts.onFinish(new EasyPlexSupportedHosts.OnTaskCompleted() {

                @Override
                public void onTaskCompleted(ArrayList<EasyPlexSupportedHostsModel> vidURL, boolean multipleQuality) {

                    if (multipleQuality){
                        if (vidURL!=null) {

                            CharSequence[] names = new CharSequence[vidURL.size()];

                            for (int i = 0; i < vidURL.size(); i++) {
                                names[i] = vidURL.get(i).getQuality();
                            }

                            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme);
                            builder.setTitle(context.getString(R.string.select_qualities));
                            builder.setCancelable(true);
                            builder.setItems(names, (dialogInterface, i) -> {

                                String id = ((EasyPlexMainPlayer)context).getPlayerController().getVideoID();
                                String externalId = ((EasyPlexMainPlayer)context).getPlayerController().getMediaSubstitleName();
                                String type = ((EasyPlexMainPlayer)context).getPlayerController().getMediaType();
                                String currentQuality = ((EasyPlexMainPlayer)context).getPlayerController().getVideoCurrentQuality();
                                String artwork = (String.valueOf(((EasyPlexMainPlayer)context).getPlayerController().getMediaPoster())) ;
                                String name = ((EasyPlexMainPlayer)context).getPlayerController().getCurrentVideoName();
                                mMediaModel = MediaModel.media(id,externalId,currentQuality,type,name, vidURL.get(i).getUrl(), artwork, null,null,null
                                        ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().getSeaonNumber(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getEpName(),null,
                                        ((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpisodePosition(),((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                                        mediaStream.getHls(),
                                        null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                                        ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre()
                                        ,((EasyPlexMainPlayer)context).getPlayerController().getSerieName(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage(),
                                        ((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                        ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                                ((EasyPlexMainPlayer)context).update(mMediaModel);
                                clickDetectListner.onQualityClicked(true);

                            });

                            builder.show();



                        }else  Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show();

                    }else {


                        String id = ((EasyPlexMainPlayer)context).getPlayerController().getVideoID();
                        String externalId = ((EasyPlexMainPlayer)context).getPlayerController().getMediaSubstitleName();
                        String type = ((EasyPlexMainPlayer)context).getPlayerController().getMediaType();
                        String currentQuality = ((EasyPlexMainPlayer)context).getPlayerController().getVideoCurrentQuality();
                        String artwork = (String.valueOf(((EasyPlexMainPlayer)context).getPlayerController().getMediaPoster())) ;
                        String name = ((EasyPlexMainPlayer)context).getPlayerController().getCurrentVideoName();
                        mMediaModel = MediaModel.media(id,externalId,currentQuality,type,name, vidURL.get(0).getUrl(), artwork, null,null,null
                                ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().getSeaonNumber(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getEpName(),null,
                                ((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpisodePosition(),((EasyPlexMainPlayer)context).getPlayerController().getCurrentEpTmdbNumber(),((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                                mediaStream.getHls(),
                                null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                                ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre()
                                ,((EasyPlexMainPlayer)context).getPlayerController().getSerieName()
                                ,((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage()
                        ,((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                                ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
                        ((EasyPlexMainPlayer)context).update(mMediaModel);
                        clickDetectListner.onQualityClicked(true);
                    }

                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            easyPlexSupportedHosts.find(mediaStream.getLink());

        }
    }
}
