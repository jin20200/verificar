package com.easyplexdemoapp.ui.casts;

import static com.easyplexdemoapp.util.Constants.ARG_CAST;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.easyplexdemoapp.R;
import com.easyplexdemoapp.data.model.credits.Cast;
import com.easyplexdemoapp.util.ItemAnimation;
import com.easyplexdemoapp.util.Tools;
import org.jetbrains.annotations.NotNull;

public class AllCastersAdapter extends PagedListAdapter<Cast, AllCastersAdapter.ItemViewHolder> {

    private final Context context;
    private final int animationType;

    public AllCastersAdapter(Context context, int animationType) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.animationType = animationType;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular_casters, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Cast cast = getItem(position);


        if (cast != null) {

            Tools.onLoadMediaCoverAdapters(context,holder.imageView,cast.getProfilePath());

            setAnimation(holder.itemView, position);

            holder.casttitle.setText(cast.getName());


            holder.movietype.setOnClickListener(v -> {

                Intent intent = new Intent(context, CastDetailsActivity.class);
                intent.putExtra(ARG_CAST, cast);
                context.startActivity(intent);


           });


        }

        }


    private static final DiffUtil.ItemCallback<Cast> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(Cast oldItem, Cast newItem) {
                    return String.valueOf(oldItem.getId()).equals(newItem.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Cast oldItem, @NotNull Cast newItem) {
                    return oldItem.equals(newItem);
                }
            };


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                onAttach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }



    private int lastPosition = -1;
    private boolean onAttach = true;


    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, onAttach ? position : -1, animationType);
            lastPosition = position;
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final ImageView imageView;
        final LinearLayout movietype;
        final TextView casttitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            casttitle = itemView.findViewById(R.id.casttitle);
            imageView = itemView.findViewById(R.id.itemCastImage);
            movietype = itemView.findViewById(R.id.rootLayout);
        }
    }
}
