package com.psc.beermate.presentation.view.adapter;

import android.content.Context;
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
            holder.bindTo(beerInfo);
        } else {
            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
            // this row when the actual object is loaded from the database
            holder.clear();
        }
    }
//
//    public static class ImageClick implements View.OnClickListener {
//        private String imageUrl;
//        private Context context;
//
//        public ImageClick(Context context) {
//            this.context = context;
//        }
//
//        public void onClick(View view) {
//            Intent intent = new Intent(context, ImageActivity.class);
//            intent.putExtra(ImageActivity.URL_KEY, imageUrl);
//            context.startActivity(intent);
//        }
//
//        public void setImageUrl(String imageUrl) {
//            this.imageUrl = imageUrl;
//        }
//
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(final List<BeerInfo> listToAppend) {
        final int previousItemCount = getItemCount();
        this.list.addAll(listToAppend);
        notifyItemRangeInserted(previousItemCount, listToAppend.size());
    }

    public void resetData() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setData(final List<BeerInfo> listToReplace) {
        this.list.clear();
        this.list.addAll(listToReplace);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView description;

        public ViewHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);

        }

        public void bindTo(final BeerInfo beerInfo) {
            name.setText(beerInfo.getName());
            description.setText(beerInfo.getDescription());
        }


        public void clear() {
        }
    }

}
