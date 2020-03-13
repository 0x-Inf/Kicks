package com.diablo.jayson.kicksv1.Utils;

import com.diablo.jayson.kicksv1.Models.Host;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUtil {

    public static Host getHost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new Host(user.getDisplayName(), user.getUid());
    }
}
