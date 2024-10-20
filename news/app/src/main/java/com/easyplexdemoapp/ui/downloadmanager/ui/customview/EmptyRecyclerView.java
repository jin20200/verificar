/**
 * EasyPlex - Movies - Live Streaming - TV Series, Anime
 *
 * @author @Y0bEX
 * @package  EasyPlex - Movies - Live Streaming - TV Series, Anime
 * @copyright Copyright (c) 2021 Y0bEX,
 * @license http://codecanyon.net/wiki/support/legal-terms/licensing-terms/
 * @profile https://codecanyon.net/user/yobex
 * @link yobexd@gmail.com
 * @skype yobexd@gmail.com
 **/

package com.easyplexdemoapp.ui.downloadmanager.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/*
 * A RecyclerView with empty view.
 */

public class EmptyRecyclerView extends RecyclerView
{
    private View emptyView;

    final private AdapterDataObserver observer = new AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            checkEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount)
        {
            checkEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount)
        {
            checkEmpty();
        }
    };

    public EmptyRecyclerView(Context context)
    {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    void checkEmpty()
    {
        if (emptyView != null && getAdapter() != null) {
            boolean isEmptyViewVisible = getAdapter().getItemCount() == 0;

            emptyView.setVisibility((isEmptyViewVisible ? VISIBLE : GONE));
            setVisibility((isEmptyViewVisible ? GONE : VISIBLE));
        }
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null)
            oldAdapter.unregisterAdapterDataObserver(observer);

        super.setAdapter(adapter);

        if (adapter != null)
            adapter.registerAdapterDataObserver(observer);

        checkEmpty();
    }

    public void setEmptyView(View emptyView)
    {
        this.emptyView = emptyView;
        checkEmpty();
    }
}
