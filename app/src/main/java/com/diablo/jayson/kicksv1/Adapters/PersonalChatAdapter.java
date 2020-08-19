package com.diablo.jayson.kicksv1.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.ChatItem;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.GroupChatAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;

public class PersonalChatAdapter extends FirestoreRecyclerAdapter<ChatItem, PersonalChatAdapter.PersonalChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_SELF = 1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PersonalChatAdapter(@NonNull FirestoreRecyclerOptions<ChatItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PersonalChatViewHolder holder, int position, @NonNull ChatItem model) {

        ChatItem chatItem = getItem(position);
        holder.bindTo(chatItem);
    }

    @NonNull
    @Override
    public PersonalChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_SELF) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_chat_item_self, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_chat_item_view, parent, false);
        }
        return new PersonalChatViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("user", user.getUid());
        if (getItem(position).getSenderUid().equals(user.getUid())) {
            return MSG_SELF;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    class PersonalChatViewHolder extends RecyclerView.ViewHolder{

        private ImageView senderPic;
        private TextView senderName;
        private TextView message;
        private TextView timeSend;

        public PersonalChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderPic = itemView.findViewById(R.id.senderProfilePic);
            senderName = itemView.findViewById(R.id.senderName);
            message = itemView.findViewById(R.id.chatMessage);
            timeSend = itemView.findViewById(R.id.sendTime);
        }

        void bindTo(ChatItem chatItem){
            Date date = chatItem.getTimestamp().toDate();
//            DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

            timeSend.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
            Glide.with(itemView.getContext())
                    .load(chatItem.getSenderPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(senderPic);
            senderName.setText(chatItem.getSenderName());
            message.setText(chatItem.getMessage());
        }
    }
}
