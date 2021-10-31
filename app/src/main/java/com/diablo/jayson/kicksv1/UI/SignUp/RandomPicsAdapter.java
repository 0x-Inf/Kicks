package com.diablo.jayson.kicksv1.UI.SignUp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.ProfilePicExample;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

//import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto

public class RandomPicsAdapter extends RecyclerView.Adapter<RandomPicsAdapter.RandomPicViewHolder> {

    private final ArrayList<ProfilePicExample> randomPictures;
    private final OnRandomPicSelectedListener listener;

    public RandomPicsAdapter(ArrayList<ProfilePicExample> randomPictures, OnRandomPicSelectedListener listener) {
        this.randomPictures = randomPictures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RandomPicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomPicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_profile_pic_example_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomPicViewHolder holder, int position) {
        ProfilePicExample randomPic = randomPictures.get(position);
        holder.bindTo(randomPic, listener);
    }

    @Override
    public int getItemCount() {
        return randomPictures.size();
    }

    public interface OnRandomPicSelectedListener {
        void onRandomPicSelected(ProfilePicExample pic);
    }

    static class RandomPicViewHolder extends RecyclerView.ViewHolder {

        private final ImageView randomPicImageView;

        public RandomPicViewHolder(@NonNull View itemView) {
            super(itemView);
            randomPicImageView = itemView.findViewById(R.id.profileitemimage);
        }

        void bindTo(ProfilePicExample randomPic, OnRandomPicSelectedListener listener) {
            Glide.with(itemView.getContext())
                    .load(randomPic.getPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(randomPicImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRandomPicSelected(randomPic);
                    }
                }
            });
        }
    }
}
