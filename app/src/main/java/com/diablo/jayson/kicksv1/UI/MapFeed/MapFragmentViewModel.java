package com.diablo.jayson.kicksv1.UI.MapFeed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapFragmentViewModel extends ViewModel {
    private final MutableLiveData<String> tagName = new MutableLiveData<String>();


    public void setTagName(String name) {
        tagName.setValue(name);
    }

    public LiveData<String> getTagName() {
        return tagName;
    }

}
