package com.psc.beermate.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psc.beermate.R;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.ArrayList;
import java.util.List;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder> {

    private List<BeerInfo> list = new ArrayList<>();
    private Context context;
    private ItemClickListener itemClickListener = null;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_beer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BeerInfo beerInfo = list.get(position);
        if (beerInfo != null) {
            holder.bindTo(beerInfo, itemClickListener);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(final List<BeerInfo> listToReplace) {
        this.list.clear();
        this.list.addAll(listToReplace);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
        }

        public void bindTo(final BeerInfo beerInfo, @Nullable ItemClickListener itemClickListener) {
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onClick(beerInfo);
                }
            });
            name.setText(beerInfo.getName());
            description.setText(beerInfo.getDescription());
        }

        public void clear() {
            itemView.setOnClickListener(null);
        }
    }

    public interface ItemClickListener {
        void onClick(BeerInfo item);
    }
}
