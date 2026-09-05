package edu.facom.avaliacao1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegionViewModel extends ViewModel {

    private final MutableLiveData<String> regionSelection = new MutableLiveData<>();

    //Seleciona a região no spinner
    public void setRegion(String region) {
        regionSelection.setValue(region);
    }

    //Retorna a região para os fragmentos 2 e 3
    public LiveData<String> getRegion() {
        return regionSelection;
    }
}
