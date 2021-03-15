package com.diablo.jayson.kicksv1.Utils;

import androidx.annotation.NonNull;

import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FirebaseUtil {
    private String userName;
    private User userMain;

    public static Host getHost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new Host(user.getDisplayName(), user.getUid(), Objects.requireNonNull(user.getPhotoUrl()).toString());
    }

    public User getUser() {
        userMain = new User();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert user != null;
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    userMain = documentSnapshot.toObject(User.class);
                }
            }
        });

        return userMain;
    }

    public static AttendingUser getAttendingUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        return new AttendingUser(user.getDisplayName(), user.getUid(), Objects.requireNonNull(Objects.requireNonNull(user.getPhotoUrl()).toString()));
    }

}
