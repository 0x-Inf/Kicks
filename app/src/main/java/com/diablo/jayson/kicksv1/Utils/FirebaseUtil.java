package com.diablo.jayson.kicksv1.Utils;

import androidx.annotation.NonNull;

import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FirebaseUtil {
    private String userName;

    public static Host getHost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new Host(user.getDisplayName(), user.getUid());
    }

    public static User getUser() {
        final String[] userName = {""};
        final String[] uid = {""};
        final String[] firstName = {""};
        final String[] secondName = {""};
        final String[] userEmail = {""};
        final String[] passWord = {""};
        final String[] photoUrl = {""};
        final String[] idNumber = {""};
        final String[] phoneNumber = {""};
        final boolean[] isStudent = {false};
        final String[] schoolName = {""};

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert user != null;
        db.collection("users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                userName[0] = document.toObject(User.class).getUserName();
                                uid[0] = document.toObject(User.class).getUid();
                                firstName[0] = document.toObject(User.class).getFirstName();
                                secondName[0] = document.toObject(User.class).getSecondName();
                                userEmail[0] = document.toObject(User.class).getUserEmail();
                                passWord[0] = document.toObject(User.class).getPassWord();
                                photoUrl[0] = document.toObject(User.class).getPhotoUrl();
                                idNumber[0] = document.toObject(User.class).getIdNumber();
                                phoneNumber[0] = document.toObject(User.class).getPhoneNumber();
                                isStudent[0] = document.toObject(User.class).isStudent();
                                schoolName[0] = document.toObject(User.class).getSchoolName();

                            }
                        }
                    }
                });
        DocumentReference documentReference = db.collection("users").document(user.getUid());

        return new User(userName[0], firstName[0], secondName[0], userEmail[0], passWord[0], photoUrl[0],
                idNumber[0], phoneNumber[0], isStudent[0], schoolName[0], uid[0]);
    }

    public static AttendingUser getAttendingUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        User user1 = FirebaseUtil.getUser();
        assert user != null;
        return new AttendingUser(user.getDisplayName(), user.getUid(), "", "",
                user.getEmail(), Objects.requireNonNull(user.getPhotoUrl().toString()), "", user1.getPhoneNumber(),
                user1.isStudent());
    }

}
