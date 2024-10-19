package com.easyplexdemoapp.util;


import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;


public class AppController extends BaseObservable {


    public final ObservableField<Boolean> isFavorite = new ObservableField<>(false);


    public final ObservableField<Boolean> isShadowEnabled = new ObservableField<>(false);


    public final ObservableField<Boolean> isLibraryStyleEnabled = new ObservableField<>(false);


    public final ObservableField<Boolean> isDownloadEnabled = new ObservableField<>(false);


}
