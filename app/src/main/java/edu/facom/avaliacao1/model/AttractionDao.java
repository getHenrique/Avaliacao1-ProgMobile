package edu.facom.avaliacao1.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AttractionDao {
    @Insert
    void insertAttraction(Attraction attraction);

    // Retorna as atrações filtradas por região de forma reativa
    @Query("SELECT * FROM attractions WHERE regiaoId = :regiaoId")
    LiveData<List<Attraction>> getAttractionsByRegion(int regiaoId);
}