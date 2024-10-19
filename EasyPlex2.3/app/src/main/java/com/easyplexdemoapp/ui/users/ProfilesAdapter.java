package com.easyplexdemoapp.ui.users;

import static com.easyplexdemoapp.util.Constants.ISUSER_MAIN_ACCOUNT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.auth.Profile;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.databinding.ItemProfilesBinding;
import com.easyplexdemoapp.ui.base.BaseActivity;
import com.easyplexdemoapp.ui.manager.AuthManager;
import com.easyplexdemoapp.util.Tools;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Adapter for Movie.
 *
 * @author Yobex.
 */
public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.MainViewHolder>{


    private static final int GALLERY_IMAGE_REQ_CODE = 102;

    private List<Profile> castList;
    private Context context;
    AuthManager authManager;

    SharedPreferences.Editor sharedPreferencesEditor;

    private final MenuHandler menuHandler;

    private AuthRepository authRepository;

    public ProfilesAdapter(MenuHandler menuHandler) {

        this.menuHandler = menuHandler;
    }

    private OnItemClickListener onItemClickListener;
    private onDeleteCommentListner onDeleteCommentListner;

    public void setEditMode(boolean setmode){

        menuHandler.isUserEditMode.set(setmode);

    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setonDeleteCommentListner(onDeleteCommentListner onDeleteCommentListner) {
        this.onDeleteCommentListner = onDeleteCommentListner;
    }



    public interface OnItemClickListener {
        void onItemClick(View view, Profile profile, int pos);
    }


    public interface onDeleteCommentListner {

        void onItemClick(boolean isDeleted);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void addMain(List<Profile> castList, Context context,AuthManager authManager,AuthRepository authRepository, SharedPreferences.Editor sharedPreferencesEditor) {
        this.castList = castList;
        this.context = context;
        this.authManager = authManager;
        this.authRepository = authRepository;
        this.sharedPreferencesEditor= sharedPreferencesEditor;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProfilesAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemProfilesBinding binding = ItemProfilesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        binding.setController(menuHandler);

        return new ProfilesAdapter.MainViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

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

        private final ItemProfilesBinding binding;

        MainViewHolder(@NonNull ItemProfilesBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Profile profile = castList.get(position);

            binding.profileTitle.setText(profile.getName());

            Tools.loadUserAvatar(context,binding.itemMovieImage,profile.getAvatar());


            binding.rootLayout.setOnClickListener(v -> {


                if (Boolean.TRUE.equals(menuHandler.isUserEditMode.get())){


                    ImagePicker.with(((UserProfiles)context))
                            // Crop Image(User can choose Aspect Ratio)
                            .crop()
                            // User can only select image from Gallery
                            .galleryOnly()

                            .galleryMimeTypes(new String[]{"image/png",
                                    "image/jpg",
                                    "image/jpeg"
                            })
                            // Image resolution will be less than 1080 x 1920
                            .maxResultSize(1080, 1920)
                            .cropSquare()
                            .start(GALLERY_IMAGE_REQ_CODE);


                }else {


                    authManager.saveSettingsProfile(profile);
                    sharedPreferencesEditor.putBoolean(ISUSER_MAIN_ACCOUNT,false).apply();
                    context.startActivity(new Intent(context, BaseActivity.class));
                    ((UserProfiles)context).finish();
                }

            });


            binding.editProfile.setOnClickListener(view -> {

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, profile, position);
                }
            });

            binding.deleteProfile.setOnClickListener(v -> authRepository.deleteUserProfile(String.valueOf(profile.getId()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Profile profile) {

                            Toast.makeText(context, R.string.profile_deleted, Toast.LENGTH_SHORT).show();

                            if (onDeleteCommentListner != null) {
                                onDeleteCommentListner.onItemClick(true);
                            }
                        }


                        @Override
                        public void onError(@NotNull Throwable e) {

                            Toast.makeText(context, "Error !", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onComplete() {

                            //

                        }
                    }));



        }


    }


}
