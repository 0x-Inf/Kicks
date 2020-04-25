package com.diablo.jayson.kicksv1.UI.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeaturedViewModel extends ViewModel {
    private final MutableLiveData<String> featuredId = new MutableLiveData<String>();


    public void setFeaturedId(String name) {
        featuredId.setValue(name);
    }

    public LiveData<String> getFeaturedId() {
        return featuredId;
    }
}
