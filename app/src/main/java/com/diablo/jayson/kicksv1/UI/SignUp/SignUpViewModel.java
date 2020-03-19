package com.diablo.jayson.kicksv1.UI.SignUp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.User;

public class SignUpViewModel extends ViewModel {
    private final MutableLiveData<User> mutableLiveData = new MutableLiveData<User>();

    public void setUser(User user) {
        mutableLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return mutableLiveData;
    }
}
