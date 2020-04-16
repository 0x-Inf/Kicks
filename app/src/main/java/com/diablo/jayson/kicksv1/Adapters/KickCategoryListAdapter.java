package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class KickCategoryListAdapter extends FirestoreRecyclerAdapter<KickCategory, KickCategoryListAdapter.KickCategoryViewHolder> implements LifecycleOwner {

    private Context mContext;
    private LayoutInflater mInflater;
    private KickListAdapter adapter;
    //    private ArrayList<Kick> kicksData;
    private KicksAdapter.OnKickSelectedListener listener1;

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }


    public interface OnSeeAllClickedListener {
        void onSeeAllClicked(KickCategory kickCategory);
    }

    private OnSeeAllClickedListener listener;


    public KickCategoryListAdapter(@NonNull FirestoreRecyclerOptions<KickCategory> options, OnSeeAllClickedListener listener, KicksAdapter.OnKickSelectedListener listener1) {
        super(options);
        this.listener = listener;
        this.listener1 = listener1;
    }


    @NonNull
    @Override
    public KickCategoryListAdapter.KickCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickCategoryViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.kick_category_list_item, parent, false));
    }


    @Override
    protected void onBindViewHolder(@NonNull KickCategoryViewHolder holder, int position, @NonNull KickCategory model) {
        KickCategory currentCategory = getItem(position);
//        Query query = FirebaseFirestore.getInstance()
//                .collection("kickselects")
//                .document("groupa")
//                .collection("kickcategories")
//                .document(currentCategory.getCategoryId().trim())
//                .collection("kicksincategory");
//
//        FirestoreRecyclerOptions<Kick> options = new FirestoreRecyclerOptions.Builder<Kick>()
//                .setQuery(query, Kick.class)
//                .build();
//        adapter = new KickListAdapter(options, listener1);
//        holder.mkickRecyclerView.setAdapter(adapter);
//        holder.mkickRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
//        holder.mkickRecyclerView.setHasFixedSize(true);
//        adapter.startListening();
//        adapter.notifyDataSetChanged();
//        holder.bindTo(currentCategory, listener);
//        ArrayList<KickCategory> kickCategories = new ArrayList<KickCategory>();
//        for (int i = 0; i < getItemCount(); i++) {
//            kickCategories.add(i, currentCategory);
//        }
//        for (int j = 0; j < kickCategories.size(); j++) {
//            Log.e("ctsss", kickCategories.get(j).getCategoryName());
////
//        }
//
//
//
//

        ArrayList<Kick> kicksData = new ArrayList<Kick>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        adapter.stopListening();
        db.collection("kickselects")
                .document("groupa")
                .collection("kickcategories")
                .document(currentCategory.getCategoryId().trim())
                .collection("kicksincategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                Log.e("kicks", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                kicksData.add(new Kick(documentSnapshot.toObject(Kick.class).getKickName(),
                                        documentSnapshot.toObject(Kick.class).getKickCardImageUrl(),
                                        documentSnapshot.toObject(Kick.class).getKickLargeImageUrl(),
                                        documentSnapshot.toObject(Kick.class).getTags()));
                            }

                            KicksAdapter adapter = new KicksAdapter(holder.itemView.getContext(), kicksData, listener1);

                            holder.mkickRecyclerView.setAdapter(adapter);
                            holder.mkickRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 1, GridLayoutManager.HORIZONTAL, false));
                        }
                    }
                });
        holder.bindTo(currentCategory, listener);


    }


    static class KickCategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mCategoryTextView;
        private TextView seeAllText;
        private RecyclerView mkickRecyclerView;
        private KickListAdapter kickListAdapter;


        public KickCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mCategoryTextView = itemView.findViewById(R.id.CategoryTextview);
            seeAllText = itemView.findViewById(R.id.seeAllTextView);
            mkickRecyclerView = itemView.findViewById(R.id.kicksRecyclerView);

        }

        void bindTo(KickCategory currentKickCategory, OnSeeAllClickedListener listener) {
            mCategoryTextView.setText(currentKickCategory.getCategoryName());
            seeAllText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSeeAllClicked(currentKickCategory);
                    }
                }
            });


        }
    }

    private void initializeKickData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("kickselects").document("groupa")
//                .collection("kickcategories").document(kickCategory.getCategoryId()).collection("kicksincategory")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
//                                Log.e("kicks", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                            }
//                        }
//                    }
//                });
    }
}
