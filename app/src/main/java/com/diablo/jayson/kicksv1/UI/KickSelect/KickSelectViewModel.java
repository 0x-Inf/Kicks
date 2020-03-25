package com.diablo.jayson.kicksv1.UI.KickSelect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KickSelectViewModel extends ViewModel {

    private final MutableLiveData<String> categoryIdData = new MutableLiveData<String>();
    private final MutableLiveData<String> categoryNameData = new MutableLiveData<String>();


    public void setCategoryId(String activityId) {
        categoryIdData.setValue(activityId);
    }

    public LiveData<String> getCategoryId() {
        return categoryIdData;
    }

    public void setCategoryName(String activityId) {
        categoryNameData.setValue(activityId);
    }

    public LiveData<String> getCategoryName() {
        return categoryNameData;
    }
}
