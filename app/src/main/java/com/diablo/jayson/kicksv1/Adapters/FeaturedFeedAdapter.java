package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.KickInFeaturedList;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class    FeaturedFeedAdapter extends FirestoreRecyclerAdapter<FeaturedKicks, RecyclerView.ViewHolder> {

    private static final String TAG = FeaturedFragment.class.getSimpleName();
    private static final int IMAGE_TEXT = 1;
    private static final int IMAGE_TEXT_LIST = 2;
    private Context mContext;
    private List<KickInFeaturedList> mKicksData = new ArrayList<>();
    private FeatureKickListAdapter mKicksAdapter;
    private List<FeaturedKicks> mTotalKicks;
    private DatabaseReference mDataBase;

    public interface OnFeaturedImageTextSelected {
        void onFeaturedImageTextSelected(FeaturedKicks featuredKick);
    }

    public interface OnFeaturedImageTextListSelected {
        void onFeaturedImageTextListSelected(FeaturedKicks featuredKick);
    }

    private OnFeaturedImageTextSelected imageTextListener;
    private OnFeaturedImageTextListSelected imageTextListListener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeaturedFeedAdapter(@NonNull FirestoreRecyclerOptions<FeaturedKicks> options,
                               OnFeaturedImageTextSelected imageTextListener,
                               OnFeaturedImageTextListSelected imageTextListListener) {
        super(options);
        this.imageTextListener = imageTextListener;
        this.imageTextListListener = imageTextListListener;
    }


    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (getItem(position).getFeaturedType().equals(Constants.IMAGE_AND_TEXT)) {
            return IMAGE_TEXT;
        } else if (getItem(position).getFeaturedType().equals(Constants.IMAGE_TEXT_AND_LIST)) {
            return IMAGE_TEXT_LIST;
        } else {
            return IMAGE_TEXT;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == IMAGE_TEXT) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_image_and_text_item, parent, false);
            return new ImageAndTextOnlyActivityViewHolder(view);
        } else if (viewType == IMAGE_TEXT_LIST) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_image_text_and_list_item, parent, false);
            return new ImageAndTextAndListActivityViewHolder(view);
        }
        assert view != null;
        return new ImageAndTextOnlyActivityViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull FeaturedKicks model) {
        FeaturedKicks featuredKick = getItem(position);
        holder.getAdapterPosition();
        switch (holder.getItemViewType()) {
            case IMAGE_TEXT:
                ((ImageAndTextOnlyActivityViewHolder) holder).bindTo(featuredKick, imageTextListener);
                break;
            case IMAGE_TEXT_LIST:
                Query query = FirebaseFirestore.getInstance()
                        .collection("featuredkicks")
                        .document(featuredKick.getFeaturedKickId().trim())
                        .collection("kicksinlist");

                FirestoreRecyclerOptions<KickInFeaturedList> options = new FirestoreRecyclerOptions.Builder<KickInFeaturedList>()
                        .setQuery(query, KickInFeaturedList.class)
                        .build();

                FeatureKickListAdapter adapter = new FeatureKickListAdapter(options);
                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.setLayoutManager(layoutManager);
                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.post(new Runnable() {
                    @Override
                    public void run() {
                        new CountDownTimer(Integer.MAX_VALUE, 20) {
                            public void onTick(long millis) {
                                ((ImageAndTextAndListActivityViewHolder) holder).mFeaturedListView.scrollBy(0, 1);
                            }

                            @Override
                            public void onFinish() {

                            }
                        }.start();
                    }
                });
                adapter.startListening();
                ((ImageAndTextAndListActivityViewHolder) holder).bindTo(featuredKick, imageTextListListener);
                break;
            default:

        }
    }


    static class ImageAndTextOnlyActivityViewHolder extends RecyclerView.ViewHolder {

        // TODO: Appropriately Name this stuff

        private ImageView featuredKickImage;
        private TextView featuredKickTitle, featuredKickSubtitle;


        ImageAndTextOnlyActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            featuredKickTitle = itemView.findViewById(R.id.kickFeaturedTitleTextView);
            featuredKickSubtitle = itemView.findViewById(R.id.kickFeaturedSubtitleTextView);
            featuredKickImage = itemView.findViewById(R.id.kickFeaturedImage);
        }

        void bindTo(FeaturedKicks currentFeaturedKick, OnFeaturedImageTextSelected imageTextListener) {

            featuredKickTitle.setText(currentFeaturedKick.getFeaturedTitle());
            featuredKickSubtitle.setText(currentFeaturedKick.getFeaturedSubTitle());
            Glide.with(itemView.getContext()).load(currentFeaturedKick.getFeaturedImageUrl()).into(featuredKickImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageTextListener != null) {
                        imageTextListener.onFeaturedImageTextSelected(currentFeaturedKick);
                    }
                }
            });

        }
    }

    static class ImageAndTextAndListActivityViewHolder extends RecyclerView.ViewHolder {

        private ImageView mKickFeaturedListImage;
        private TextView mFeaturedListTitle;
        private RecyclerView mFeaturedListView;


        ImageAndTextAndListActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            mKickFeaturedListImage = itemView.findViewById(R.id.kickFeaturedListImage);
            mFeaturedListTitle = itemView.findViewById(R.id.kickFeaturedListNameTextView);
            mFeaturedListView = itemView.findViewById(R.id.featuredList);

        }

        void bindTo(FeaturedKicks currentFeaturedKick, OnFeaturedImageTextListSelected imageTextListListener) {
            mFeaturedListTitle.setText(currentFeaturedKick.getFeaturedTitle());
            Glide.with(itemView.getContext()).load(currentFeaturedKick.getFeaturedImageUrl()).into(mKickFeaturedListImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageTextListListener != null) {
                        imageTextListListener.onFeaturedImageTextListSelected(currentFeaturedKick);
                    }
                }
            });
        }

    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    private void initializeKickData() {
//        String[] kickNames = mContext.getResources().getStringArray(R.array.kicks_titles);
//        String[] kickImages = mContext.getResources().getStringArray(R.array.kicks_image_urls);
//        mKicksData.clear();
//
//        for (int i = 0; i < kickImages.length; i++) {
//            mKicksData.add(new KickInFeaturedList(kickNames[i], kickImages[i],kickNames[i]));
//        }

    }

    private void loadFeaturedList1FromFirebase(){

    }

}

