package com.easyplexdemoapp.util;

import android.database.Observable;
import android.util.Base64;

import androidx.databinding.ObservableField;

import java.nio.charset.StandardCharsets;

public class LoadingStateController extends Observable {



    public static final String PI = "UmVndWxhciBMaWNlbnNl";


    public final ObservableField<Boolean> isLoading = new ObservableField<>(true);

    public final ObservableField<Boolean> isSearchBarFocused = new ObservableField<>(true);

    public final ObservableField<Boolean> ToHide = new ObservableField<>(true);

    public final ObservableField<String > Type = new ObservableField<>("");

    public final ObservableField<Boolean> isSuggestionsExpanded = new ObservableField<>(false);




    public static String decodeString(){
        byte[] valueDecoded;
        valueDecoded = Base64.decode(PI.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        return new String(valueDecoded);
    }


}
