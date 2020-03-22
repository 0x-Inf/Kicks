package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.ChatItem;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatItem, ChatAdapter.ChatViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatItem> options) {
        super(options);
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_view, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatItem model) {
        ChatItem chatItem = getItem(position);
        holder.bindTo(chatItem);
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {

        private ImageView senderPic;
        private TextView senderName;
        private TextView message;
        private TextView timeSend;
        private RelativeLayout chatItemLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderPic = itemView.findViewById(R.id.senderProfilePic);
            senderName = itemView.findViewById(R.id.senderName);
            message = itemView.findViewById(R.id.chatMessage);
            timeSend = itemView.findViewById(R.id.sendTime);
            chatItemLayout = itemView.findViewById(R.id.chatContent);
        }

        void bindTo(ChatItem chatItem) {
            Glide.with(itemView.getContext())
                    .load(chatItem.getSenderPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(senderPic);
            senderName.setText(chatItem.getSenderName());
            message.setText(chatItem.getMessage());
            if (chatItem.isSender()) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) senderPic.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                senderPic.setLayoutParams(params);
            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) senderPic.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                senderPic.setLayoutParams(params);
            }

//            timeSend.setText(chatItem.getTimestamp().toDate().toString());
        }
    }
}
