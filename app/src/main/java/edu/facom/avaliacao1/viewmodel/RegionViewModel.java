package edu.facom.avaliacao1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegionViewModel extends ViewModel {

    private final MutableLiveData<String> regionSelection = new MutableLiveData<>();

    public void setRegion(String region) {
        regionSelection.setValue(region);
    }

    public LiveData<String> getRegion() {
        return regionSelection;
    }
}
