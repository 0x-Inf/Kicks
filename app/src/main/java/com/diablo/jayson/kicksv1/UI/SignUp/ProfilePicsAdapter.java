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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProfilePicsAdapter extends FirestoreRecyclerAdapter<ProfilePicExample, ProfilePicsAdapter.ProfilePicViewHolder> {


    public interface OnPicSelectedListener {
        void onPicSelected(ProfilePicExample pic);
    }

    private OnPicSelectedListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProfilePicsAdapter(@NonNull FirestoreRecyclerOptions<ProfilePicExample> options, OnPicSelectedListener listener) {
        super(options);
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProfilePicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfilePicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_pic_example_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ProfilePicViewHolder holder, int position, @NonNull ProfilePicExample model) {
        ProfilePicExample picExample = getItem(position);
        holder.bindTo(picExample, listener);
    }

    static class ProfilePicViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilepicex;

        public ProfilePicViewHolder(@NonNull View itemView) {
            super(itemView);
            profilepicex = itemView.findViewById(R.id.profileitemimage);
        }

        void bindTo(ProfilePicExample currentPic, OnPicSelectedListener listener) {
            Glide.with(itemView.getContext())
                    .load(currentPic.getPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilepicex);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPicSelected(currentPic);
                    }
                }
            });
        }
    }
}
