package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatItem, ChatAdapter.ChatViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_SELF = 1;

    private Context context;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatItem> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_SELF) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_chat_item_self, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_chat_item_view, parent, false);
        }
        return new ChatViewHolder(view);
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

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatItem model) {
        ChatItem chatItem = getItem(position);
        holder.bindTo(chatItem);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private ImageView senderPic;
        private TextView senderName;
        private TextView message;
        private TextView timeSend;
        private RelativeLayout chatItemLayout;
        private LinearLayout chatMessageLayout;
        private LinearLayout chatMessageSelfLayout;
        private TextView messageSelf;
        private TextView timeSendSelf;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderPic = itemView.findViewById(R.id.senderProfilePic);
            senderName = itemView.findViewById(R.id.senderName);
            message = itemView.findViewById(R.id.chatMessage);
            timeSend = itemView.findViewById(R.id.sendTime);
            chatItemLayout = itemView.findViewById(R.id.chatContent);
            chatMessageLayout = itemView.findViewById(R.id.chatMessageLinearLayout);
            chatMessageSelfLayout = itemView.findViewById(R.id.chatMessageSelfLinearLayout);
        }

        void bindTo(ChatItem chatItem) {
//
//            messageSelf.setText(chatItem.getMessage());
            Date date = chatItem.getTimestamp().toDate();
//            DateFormat.getTimeInstance(DateFormat.SHORT).format(date);

            timeSend.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
            Glide.with(context)
                    .load(chatItem.getSenderPicUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(senderPic);
            senderName.setText(chatItem.getSenderName());
            message.setText(chatItem.getMessage());
        }
//
//            if (chatItem.isSender()) {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) senderPic.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                senderPic.setLayoutParams(params);
//                RelativeLayout.LayoutParams paramsLayout = (RelativeLayout.LayoutParams) chatMessageLayout.getLayoutParams();
//                paramsLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                chatMessageLayout.setLayoutParams(paramsLayout);
//                senderName.setText("");
//            } else {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) senderPic.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                senderPic.setLayoutParams(params);
//            }

//            timeSend.setText(chatItem.getTimestamp().toDate().toString());
    }
}
