package com.diablo.jayson.kicksv1.UI.UserProfile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.ContactRequest;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.RequestViewHolder> {


    private OnAddSelectedListener addSelectedListener;
    private OnRejectSelectedListener rejectSelectedListener;
    private OnRequestSelectedListener requestSelectedListener;

    private ArrayList<ContactRequest> requestsData;

    public RequestsListAdapter(ArrayList<ContactRequest> requestsData, OnAddSelectedListener addSelectedListener,
                               OnRejectSelectedListener rejectSelectedListener, OnRequestSelectedListener requestSelectedListener) {
        this.requestsData = requestsData;
        this.addSelectedListener = addSelectedListener;
        this.rejectSelectedListener = rejectSelectedListener;
        this.requestSelectedListener = requestSelectedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        ContactRequest contactRequest = requestsData.get(position);
        holder.bindTo(contactRequest, requestSelectedListener, addSelectedListener, rejectSelectedListener);
    }

    public interface OnAddSelectedListener {
        void onAddSelected(ContactRequest contactRequest);
    }

    public interface OnRejectSelectedListener {
        void onRejectSelected(ContactRequest contactRequest);
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_request_item, parent, false));
    }

    public interface OnRequestSelectedListener {
        void onRequestSelected(ContactRequest contactRequest);
    }

    @Override
    public int getItemCount() {
        return requestsData.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        private ImageView senderPicImageView;
        private TextView senderNameTextView;
        private Button addButton, rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            senderPicImageView = itemView.findViewById(R.id.senderPicImageView);
            senderNameTextView = itemView.findViewById(R.id.senderNameTextView);
            addButton = itemView.findViewById(R.id.addButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }

        void bindTo(ContactRequest contactRequest, OnRequestSelectedListener onRequestSelectedListener,
                    OnAddSelectedListener onAddSelectedListener, OnRejectSelectedListener onRejectSelectedListener) {
            senderNameTextView.setText(contactRequest.getSenderName());
            Glide.with(itemView.getContext())
                    .load(contactRequest.getSenderPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(senderPicImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRequestSelectedListener.onRequestSelected(contactRequest);
                }
            });
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddSelectedListener.onAddSelected(contactRequest);
                }
            });
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRejectSelectedListener.onRejectSelected(contactRequest);
                }
            });
        }
    }
}
