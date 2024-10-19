package com.easyplexdemoapp.ui.devices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.Device;
import com.easyplexdemoapp.data.model.auth.Profile;
import com.easyplexdemoapp.data.repository.AuthRepository;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.ItemDeviceBinding;
import com.easyplexdemoapp.ui.manager.AuthManager;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class DevicesManagementAdapter extends RecyclerView.Adapter<DevicesManagementAdapter.MainViewHolder> {


    private List<Device> castList;
    private Context context;
    AuthManager authManager;
    final MediaRepository mediaRepository;

    private onDeleteDeviceListner onDeleteDeviceListner;

    private AuthRepository authRepository;


    public DevicesManagementAdapter(MediaRepository mediaRepository) {

        this.mediaRepository = mediaRepository;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void addMain(List<Device> castList, Context context,AuthManager authManager,AuthRepository authRepository) {
        this.castList = castList;
        this.context = context;
        this.authManager = authManager;
        this.authRepository = authRepository;
        notifyDataSetChanged();
    }

    public interface onDeleteDeviceListner {

        void onItemClick(boolean isDeleted);
    }

    public void setonDeleteDeviceListner(DevicesManagementAdapter.onDeleteDeviceListner onDeleteCommentListner) {
        this.onDeleteDeviceListner = onDeleteCommentListner;
    }



    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDeviceBinding binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);


        return new MainViewHolder(binding);

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

        private final ItemDeviceBinding binding;

        MainViewHolder(@NonNull ItemDeviceBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }


        void onBind(final int position) {

            final Device device = castList.get(position);

            binding.created.setText(device.getCreatedAt());
            binding.deviceName.setText(device.getName());
            binding.deviceModel.setText(device.getModel());
            binding.macadress.setText(device.getSerialNumber());


            binding.deleteDevice.setOnClickListener(v -> authRepository.deleteDevice(String.valueOf(device.getId()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {

                            //

                        }

                        @Override
                        public void onNext(@io.reactivex.rxjava3.annotations.NonNull Profile profile) {

                            Toast.makeText(context, R.string.device_deleted, Toast.LENGTH_SHORT).show();


                            if (onDeleteDeviceListner != null) {
                                onDeleteDeviceListner.onItemClick(true);
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
