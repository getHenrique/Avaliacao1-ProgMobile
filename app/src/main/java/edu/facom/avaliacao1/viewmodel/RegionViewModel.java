package edu.facom.avaliacao1.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import edu.facom.avaliacao1.model.AppDatabase;
import edu.facom.avaliacao1.model.Attraction;
import edu.facom.avaliacao1.model.AttractionDao;
import edu.facom.avaliacao1.model.Bird;
import edu.facom.avaliacao1.model.BirdDao;
import edu.facom.avaliacao1.model.Region;
import edu.facom.avaliacao1.model.RegionDao;

public class RegionViewModel extends AndroidViewModel {

    private final RegionDao regionDao;
    private final AttractionDao attractionDao;
    private final BirdDao birdDao;

    // Retorna todas as regiões para o Spinner
    private final LiveData<List<Region>> allRegions;

    // Guarda o ID da região selecionada no momento
    private final MutableLiveData<Integer> selectedRegionId = new MutableLiveData<>();

    // Listas reativas que mudam automaticamente quando o selectedRegionId muda
    private final LiveData<List<Attraction>> attractionsForRegion;
    private final LiveData<List<Bird>> birdsForRegion;

    public RegionViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        regionDao = db.regionDao();
        attractionDao = db.attractionDao();
        birdDao = db.birdDao();

        allRegions = regionDao.getAllRegions();

        // O Transformations.switchMap observa o selectedRegionId. Toda vez que ele muda,
        // executa a consulta no banco novamente e atualiza as listas dos fragmentos 2 e 3.
        attractionsForRegion = Transformations.switchMap(selectedRegionId,
                attractionDao::getAttractionsByRegion);

        birdsForRegion = Transformations.switchMap(selectedRegionId,
                birdDao::getBirdsByRegion);
    }

    public LiveData<List<Region>> getAllRegions() { return allRegions; }

    public void setRegionId(int id) { selectedRegionId.setValue(id); }

    public LiveData<List<Attraction>> getAttractionsForRegion() { return attractionsForRegion; }

    public LiveData<List<Bird>> getBirdsForRegion() { return birdsForRegion; }
}