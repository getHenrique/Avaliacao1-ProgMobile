package edu.facom.avaliacao1.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BirdDao {
    @Insert
    void insertBird(Bird bird);

    // Retorna as aves filtradas por região
    @Query("SELECT * FROM birds WHERE regiaoId = :regiaoId")
    LiveData<List<Bird>> getBirdsByRegion(int regiaoId);
}