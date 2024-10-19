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

import com.easyplex.easyplexsupportedhosts.EasyPlexSupportedHosts;
import com.easyplex.easyplexsupportedhosts.Model.EasyPlexSupportedHostsModel;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.media.MediaModel;
import com.easyplexdemoapp.data.model.stream.MediaStream;
import com.easyplexdemoapp.databinding.RowSubstitleBinding;
import com.easyplexdemoapp.ui.manager.SettingsManager;
import com.easyplexdemoapp.ui.player.activities.EasyPlexMainPlayer;
import com.easyplexdemoapp.ui.player.activities.EmbedActivity;
import com.easyplexdemoapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for Movie Qualities.
 *
 * @author Yobex.
 */
public class MovieQualitiesAdapter extends RecyclerView.Adapter<MovieQualitiesAdapter.CastViewHolder> {

    private List<MediaStream> mediaStreams;
    private MediaModel mMediaModel;
    ClickDetectListner clickDetectListner;
    private SettingsManager settingsManager;
    private Context context;


    @SuppressLint("NotifyDataSetChanged")
    public void addSeasons(List<MediaStream> castList, ClickDetectListner clickDetectListner, SettingsManager settingsManager, Context context) {
        this.mediaStreams = castList;
        this.settingsManager = settingsManager;
        this.context = context;
        notifyDataSetChanged();
        this.clickDetectListner = clickDetectListner;

    }

    @NonNull
    @Override
    public MovieQualitiesAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        RowSubstitleBinding binding = RowSubstitleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MovieQualitiesAdapter.CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieQualitiesAdapter.CastViewHolder holder, int position) {
        holder.onBind(position);
    }



    @Override
    public int getItemCount() {
        if (mediaStreams != null) {
            return mediaStreams.size();
        } else {
            return 0;
        }
    }

    class CastViewHolder extends RecyclerView.ViewHolder {

        private final RowSubstitleBinding binding;

        CastViewHolder (@NonNull RowSubstitleBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
        void onBind(final int position) {

            final MediaStream mediaStream = mediaStreams.get(position);

            binding.eptitle.setText(mediaStream.getLang() + " - "+mediaStream.getServer());


            binding.eptitle.setOnClickListener(v -> {


                if (mediaStream.getEmbed() == 1) {


                    startStreamFromEmbed(mediaStream.getLink());



                }else if (mediaStream.getSupportedHosts() == 1 ) {

                    startStreamFromSupportedHosts(mediaStream);


                }else {


                    startStreamFromPlayer(mediaStream);
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
                                        ,null,null,null,
                                        null,
                                        null,null,((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                                        mediaStream.getHls(),null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                                        ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap()
                                        ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                                        ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre(),
                                        null,((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage()
                                        ,((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),
                                        ((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
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
                                ,null,null,null,
                                null,
                                null,null,((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                                mediaStream.getHls(),null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                                ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap()
                                ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                                ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre(),
                                null,((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage()
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

        private void startStreamFromPlayer(MediaStream mediaStream) {

            String id = ((EasyPlexMainPlayer)context).getPlayerController().getVideoID();
            String externalId = ((EasyPlexMainPlayer)context).getPlayerController().getMediaSubstitleName();
            String type = ((EasyPlexMainPlayer)context).getPlayerController().getMediaType();
            String currentQuality = ((EasyPlexMainPlayer)context).getPlayerController().getVideoCurrentQuality();
            String artwork = (String.valueOf(((EasyPlexMainPlayer)context).getPlayerController().getMediaPoster())) ;
            String name = ((EasyPlexMainPlayer)context).getPlayerController().getCurrentVideoName();
            mMediaModel = MediaModel.media(id,externalId,currentQuality,type,name, mediaStream.getLink(), artwork, null,null,null
                    ,null,null,null,
                    null,
                    null,null,((EasyPlexMainPlayer)context).getPlayerController().isMediaPremuim(),
                    mediaStream.getHls(),null,((EasyPlexMainPlayer)context).getPlayerController().getCurrentExternalId()
                    ,((EasyPlexMainPlayer)context).getPlayerController().getMediaCoverHistory(),
                    ((EasyPlexMainPlayer)context).getPlayerController().getCurrentHasRecap()
                    ,((EasyPlexMainPlayer)context).getPlayerController().getCurrentStartRecapIn(),
                    ((EasyPlexMainPlayer)context).getPlayerController().getMediaGenre(),
                    null,((EasyPlexMainPlayer)context).getPlayerController().getVoteAverage()
            ,((EasyPlexMainPlayer) context).getPlayerController().getDrmuuid(),((EasyPlexMainPlayer) context).getPlayerController().getDrmlicenceuri(),
                    ((EasyPlexMainPlayer) context).getPlayerController().getDrm());
            ((EasyPlexMainPlayer)context).update(mMediaModel);
            clickDetectListner.onQualityClicked(true);
        }

        private void startStreamFromEmbed(String link) {

            Intent intent = new Intent(context, EmbedActivity.class);
            intent.putExtra(Constants.MOVIE_LINK, link);
            context.startActivity(intent);
        }
    }


}
