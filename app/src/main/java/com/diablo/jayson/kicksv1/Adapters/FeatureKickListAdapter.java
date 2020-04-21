package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Models.KickInFeaturedList;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class FeatureKickListAdapter extends FirestoreRecyclerAdapter<KickInFeaturedList, FeatureKickListAdapter.KickViewHolder> {

    private Context mContext;
    private List<KickInFeaturedList> mKicksData;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeatureKickListAdapter(@NonNull FirestoreRecyclerOptions<KickInFeaturedList> options) {
        super(options);
    }


    @NonNull
    @Override
    public FeatureKickListAdapter.KickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeatureKickListAdapter.KickViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_featured_list_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull KickViewHolder holder, int position, @NonNull KickInFeaturedList model) {
        KickInFeaturedList featuredKickList = getItem(position);
        holder.bindTo(featuredKickList);
    }


    class KickViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickImage;
        private TextView mKickName,mKickShortDescription;

        public KickViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickImage = itemView.findViewById(R.id.featuredKickInlistimageView);
            mKickName = itemView.findViewById(R.id.featuredKickInlistName);
            mKickShortDescription = itemView.findViewById(R.id.featuredKickInlistShortDescription);
        }

        void bindTo(KickInFeaturedList currentKick){
            mKickName.setText(currentKick.getKickName());
            mKickShortDescription.setText(currentKick.getKickShortDescription());
            Glide.with(itemView.getContext()).load(currentKick.getKickImage()).into(mKickImage);
        }
    }
}
