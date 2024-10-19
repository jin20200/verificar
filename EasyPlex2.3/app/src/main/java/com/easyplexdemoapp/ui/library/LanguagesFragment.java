package com.easyplexdemoapp.ui.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.languages.Languages;
import com.easyplexdemoapp.data.repository.MediaRepository;
import com.easyplexdemoapp.databinding.LayoutLanguagesBinding;
import com.easyplexdemoapp.di.Injectable;
import com.easyplexdemoapp.ui.languages.LanguagesAdapter;
import com.easyplexdemoapp.util.SpacingItemDecoration;
import com.easyplexdemoapp.util.Tools;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class LanguagesFragment extends Fragment implements Injectable {

    LayoutLanguagesBinding binding;

    @Inject
    ViewModelProvider.Factory viewModelFactory;



    @Inject
    ItemAdapter itemAdapter;


    @Inject
    LanguagesAdapter languagesAdapter;


    @Inject
    MediaRepository mediaRepository;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.layout_languages, container, false);


        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        binding.recyclerView.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(requireActivity(), 0), true));

        binding.recyclerView.setAdapter(languagesAdapter);


        onLoadLanguages();


        return binding.getRoot();


    }

    private void onLoadLanguages() {
        mediaRepository.getLanguagesListLibrary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {


                        //


                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Languages> languages) {

                        languagesAdapter.addMain(languages,requireActivity());
                    }


                    @Override
                    public void onError(@NotNull Throwable e) {

                        //

                    }

                    @Override
                    public void onComplete() {

                        //

                    }
                });
    }


}
