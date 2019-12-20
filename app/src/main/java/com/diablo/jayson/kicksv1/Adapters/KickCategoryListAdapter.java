package com.diablo.jayson.kicksv1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class KickCategoryListAdapter extends RecyclerView.Adapter<KickCategoryListAdapter.KickCategoryViewHolder> {

    private Context mContext;
    private ArrayList<KickCategory> mKickCategoryData;
    private LayoutInflater mInflater;
    private KickListAdapter mKicksAdapter;
    private ArrayList<Kick> mKicksData;

    public KickCategoryListAdapter(Context context, ArrayList<KickCategory> kickCategoryData) {
        this.mKickCategoryData = kickCategoryData;
        this.mContext = context;
//        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public  KickCategoryListAdapter.KickCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickCategoryViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.kick_category_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull KickCategoryViewHolder holder, int position) {
        KickCategory currentKickCategory = mKickCategoryData.get(position);
        holder.bindTo(currentKickCategory);
        mKicksData = new ArrayList<Kick>();
        mKicksAdapter = new KickListAdapter(mContext,mKicksData);
        holder.mkickRecyclerView.setAdapter(mKicksAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,1,GridLayoutManager.HORIZONTAL,false);
        holder.mkickRecyclerView.setLayoutManager(gridLayoutManager);
        initializeKickData();


    }

    @Override
    public int getItemCount() {
        return mKickCategoryData.size();
    }

    class KickCategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView mCategoryTextView;
        private RecyclerView mkickRecyclerView;


        public KickCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mCategoryTextView = itemView.findViewById(R.id.CategoryTextview);
            mkickRecyclerView = itemView.findViewById(R.id.kicksRecyclerView);


        }

        void bindTo(KickCategory currentKickCategory){
            mCategoryTextView.setText(currentKickCategory.getCategoryName());
        }
    }

    private void initializeKickData(){
        String [] kickNames = mContext.getResources().getStringArray(R.array.kicks_titles);
        String [] kickImages = mContext.getResources().getStringArray(R.array.kicks_image_urls);
        mKicksData.clear();

        for (int i = 0;i<kickNames.length;i++){
            mKicksData.add(new Kick(kickNames[i],kickImages[i]));
        }

        mKicksAdapter.notifyDataSetChanged();
    }
}
