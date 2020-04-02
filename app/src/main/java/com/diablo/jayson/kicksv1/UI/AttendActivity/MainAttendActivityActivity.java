package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.ChatItem;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class MainAttendActivityActivity extends AppCompatActivity {

    private ChatAdapter chatAdapter;

    private RelativeLayout dashItemsRelativeLayout;
    private RelativeLayout chatActualRelativeLayout;
    private FrameLayout dashItemDetailsFramelayout;
    private RecyclerView attendeesRecycler, chatRecycler, chatActualRecycler;
    private ProgressBar attendeesProress;
    private ArrayList<AttendingUser> attendingUsersData;
    private CardView chatCard;
    private RelativeLayout chatCardOverlay;
    private ImageView sendMessageButton;
    private EditText messageEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_attend_activity);
        attendeesRecycler = findViewById(R.id.attendeesRecycler);
        chatRecycler = findViewById(R.id.chatRecycler);
        chatActualRecycler = findViewById(R.id.chatActualRecycler);
        dashItemsRelativeLayout = findViewById(R.id.dash_items_relative_Layout);
        chatActualRelativeLayout = findViewById(R.id.chatActualRelativeLayout);
//        dashItemDetailsFramelayout = findViewById(R.id.dashItems_fragment_container);
//        dashItemDetailsFramelayout.setVisibility(View.GONE);
        chatCardOverlay = findViewById(R.id.chatCardOverlay);
        chatCard = findViewById(R.id.activityChatCard);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messageEdit = findViewById(R.id.messageEditText);



        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String activityId = bundle.getString("activityId");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        assert activityId != null;
        db.collection("activities").document(activityId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    attendingUsersData = new ArrayList<AttendingUser>();
                    attendingUsersData = Objects.requireNonNull(documentSnapshot.toObject(Activity.class)).getMattendees();
//                            Log.e("yooooo", String.valueOf(attendingUsersData.size()));
//                            Log.e("yooooo", attendingUsersData.get(0).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(1).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(2).getUserName());
//                            Log.e("yooooo", attendingUsersData.get(3).getUserName());


                }
//                        initializeRecyclerWithAttendees();
//                        Log.e("skkdnskn", attendingUsersData.get(3).getUserName());
                AttendeesAdapter attendeesAdapter = new AttendeesAdapter(MainAttendActivityActivity.this, attendingUsersData);
                attendeesRecycler.setLayoutManager(new GridLayoutManager(MainAttendActivityActivity.this, 2, GridLayoutManager.HORIZONTAL, false));
                attendeesRecycler.setAdapter(attendeesAdapter);
            }
        });

        Query query = FirebaseFirestore.getInstance()
                .collection("activities")
                .document(activityId)
                .collection("chatsession")
                .orderBy("timestamp", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<ChatItem> options = new FirestoreRecyclerOptions.Builder<ChatItem>()
                .setQuery(query, ChatItem.class)
                .build();
        chatAdapter = new ChatAdapter(options, getApplicationContext());
        int gridColumnCount = 1;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecycler.setLayoutManager(layoutManager);

//                layoutManager.setStackFromEnd(true);
//                layoutManager.setReverseLayout(true);


        chatRecycler.post(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(Integer.MAX_VALUE, 20) {
                    public void onTick(long millis) {
                        chatRecycler.scrollBy(0, 1);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });
        chatRecycler.setAdapter(chatAdapter);
//                chatAdapter.notifyDataSetChanged();
        LinearLayoutManager chatActuallayoutManager = new LinearLayoutManager(this);

        chatActualRecycler.setLayoutManager(chatActuallayoutManager);
        chatActualRecycler.setAdapter(chatAdapter);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatActualRecycler.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });

        chatAdapter.startListening();

        chatCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), activityId, Toast.LENGTH_SHORT).show();
                chatActualRelativeLayout.setVisibility(View.VISIBLE);
                dashItemsRelativeLayout.setVisibility(View.GONE);
            }
        });
        Toast.makeText(this, activityId, Toast.LENGTH_SHORT).show();

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String message = messageEdit.getText().toString();
                if (!message.isEmpty()) {
                    ChatItem messageItem = new ChatItem();
                    messageItem.setMessage(message);
                    assert user != null;
                    messageItem.setSenderName(user.getDisplayName());
                    messageItem.setSenderPicUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());
                    messageItem.setSenderUid(user.getUid());
                    messageItem.setSender(true);
                    messageItem.setTimestamp(Timestamp.now());

                    FirebaseFirestore.getInstance().collection("activities").document(activityId)
                            .collection("chatsession")
                            .add(messageItem)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.e("Yellow", "Added Mesage");
                                    messageEdit.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Try Sending Message Again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (chatActualRelativeLayout.getVisibility() == View.VISIBLE) {
            chatActualRelativeLayout.setVisibility(View.GONE);
            dashItemsRelativeLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
//

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
