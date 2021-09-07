package com.diablo.jayson.kicksv1.UI.Home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Tag;

import java.util.ArrayList;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<Boolean> shareLocationMutableLiveData;
    private final MutableLiveData<Boolean> shareLocationPubliclyMutableLiveData;
    private MutableLiveData<ArrayList<Contact>> selectedContactsMutableLiveData;
    private MutableLiveData<ArrayList<Tag>> selectedBroadcastTagsMutableLiveData;


    public MapViewModel() {
        shareLocationMutableLiveData = new MutableLiveData<>();
        shareLocationPubliclyMutableLiveData = new MutableLiveData<>();
        selectedContactsMutableLiveData = new MutableLiveData<>();
        selectedBroadcastTagsMutableLiveData = new MutableLiveData<>();
        init();
    }

    private void init() {

    }

    public MutableLiveData<Boolean> getShareLocationMutableLiveData() {
        return shareLocationMutableLiveData;
    }

    public void setShareLocationMutableLiveData(boolean shareLocation) {
        shareLocationMutableLiveData.postValue(shareLocation);
    }

    public MutableLiveData<Boolean> getShareLocationPubliclyMutableLiveData() {
        return shareLocationPubliclyMutableLiveData;
    }

    public void setShareLocationPubliclyMutableLiveData(boolean shareLocationPublicly) {
        shareLocationPubliclyMutableLiveData.postValue(shareLocationPublicly);
    }

    public MutableLiveData<ArrayList<Contact>> getSelectedContactsMutableLiveData() {
        return selectedContactsMutableLiveData;
    }

    public void setSelectedContactsMutableLiveData(ArrayList<Contact> selectedContacts) {
        selectedContactsMutableLiveData.postValue(selectedContacts);
    }

    public MutableLiveData<ArrayList<Tag>> getSelectedBroadcastTagsMutableLiveData() {
        return selectedBroadcastTagsMutableLiveData;
    }

    public void setSelectedBroadcastTagsMutableLiveData(ArrayList<Tag> selectedBroadcastTags) {
        selectedBroadcastTagsMutableLiveData.postValue(selectedBroadcastTags);
    }

//    public void startBroadcastingLocation(){
//        if(checkIfBroadcastIdsAreInPreferences()){
//
//        }
//    }
//
//    private boolean checkIfBroadcastIdsAreInPreferences() {
//        boolean isBroadcastIdPresent;
//        locationBroadcastId = sharedPreferencesUtil.getLocationBroadcastId();
//        assert locationBroadcastId != null;
//        isBroadcastIdPresent = !locationBroadcastId.equals("");
//        return isBroadcastIdPresent;
//    }


}
