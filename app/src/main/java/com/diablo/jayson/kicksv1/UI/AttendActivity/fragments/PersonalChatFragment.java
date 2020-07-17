package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.diablo.jayson.kicksv1.Adapters.PersonalChatAdapter;
import com.diablo.jayson.kicksv1.Models.ChatItem;
import com.diablo.jayson.kicksv1.Models.User;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String chatId;
    private String chatCounterpartId;
    private User counterPartUser;

    private PersonalChatAdapter personalChatAdapter;

    private RecyclerView personalChatRecycler;
    private ImageButton sendMessageImageButton;
    private EditText messageEditText;

    public PersonalChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalChatFragment newInstance(String param1, String param2) {
        PersonalChatFragment fragment = new PersonalChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_personal_chat, container, false);
        chatCounterpartId = getArguments().getString("chatCounterpartId").toString();
        personalChatRecycler = root.findViewById(R.id.personalChatRecycler);
        sendMessageImageButton = root.findViewById(R.id.personalSendImageButton);
        messageEditText = root.findViewById(R.id.personalMessageEditText);
        getAvailableUserChats();

        sendMessageImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String message = messageEditText.getText().toString();
                if (!message.isEmpty()){
                    ChatItem messageItem = new ChatItem();
                    messageItem.setMessage(message);
                    assert user != null;
                    messageItem.setSenderName(user.getDisplayName());
                    messageItem.setSenderPicUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());
                    messageItem.setSenderUid(user.getUid());
                    messageItem.setTimestamp(Timestamp.now());

                    FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                            .collection("personalchats")
                            .document(chatCounterpartId)
                            .collection("messages")
                            .add(messageItem)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.e("Yellow", "Added Message")    ;
                                    messageEditText.getText().clear();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Try Sending Message Again", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        return root;
    }

    private void getCounterPartUserInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(chatCounterpartId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        counterPartUser = documentSnapshot.toObject(User.class);
                    }
                });
    }

    private void getAvailableUserChats() {
        getCounterPartUserInfo();
        Map<String, Object> personalchat = new HashMap<>();
        personalchat.put("chatCounterpartId",chatCounterpartId);
        ArrayList<String> availableChats = new ArrayList<String>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(user.getUid()).collection("personalchats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                availableChats.add(documentSnapshot.getId());
                            }
                            if (availableChats.isEmpty()) {
                                Log.e("this", "this one is empty");
                            } else if (!availableChats.contains(chatCounterpartId)) {
                                Log.e("add", "Someone was added ");
                                db.collection("users").document(user.getUid()).collection("personalchats")
                                        .document(chatCounterpartId)
                                        .set(counterPartUser,SetOptions.merge());
                            } else if (availableChats.contains(chatCounterpartId)) {
                                Query query = db.collection("users")
                                        .document(user.getUid())
                                        .collection("personalchats")
                                        .document(chatCounterpartId)
                                        .collection("messages")
                                        .orderBy("timestamp", Query.Direction.ASCENDING);


                                FirestoreRecyclerOptions<ChatItem> options = new FirestoreRecyclerOptions.Builder<ChatItem>()
                                        .setQuery(query,ChatItem.class)
                                        .build();

                                personalChatAdapter = new PersonalChatAdapter(options);
                                personalChatRecycler.setAdapter(personalChatAdapter);
                                personalChatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                    @Override
                                    public void onItemRangeChanged(int positionStart, int itemCount) {
                                        super.onItemRangeChanged(positionStart, itemCount);
                                        personalChatRecycler.scrollToPosition(personalChatAdapter.getItemCount() - 1);
                                    }
                                });

                            }
                        }
                    }
                });


    }
}