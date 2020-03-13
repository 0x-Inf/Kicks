package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.Observable.OnPropertyChangedCallback;
import androidx.lifecycle.MutableLiveData;

public class CustomMutableLiveData<T extends BaseObservable> extends MutableLiveData<T> {
    @Override
    public void setValue(T value) {
        super.setValue(value);

        //listen to property changes
        value.addOnPropertyChangedCallback(callback);
    }

    private Object callback1;
    OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            setValue(getValue());
        }
    };

}
