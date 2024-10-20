package com.easyplexdemoapp.ui.downloadmanager.ui;

public interface Selectable<T>
{
    T getItemKey(int position);

    int getItemPosition(T key);
}
