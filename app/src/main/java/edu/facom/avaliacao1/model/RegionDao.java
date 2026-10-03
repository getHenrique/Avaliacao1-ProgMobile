package edu.facom.avaliacao1.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RegionDao {
    @Insert
    void insertRegion(Region region);

    // Retorna todas as regiões reativamente para o Spinner
    @Query("SELECT * FROM regions")
    LiveData<List<Region>> getAllRegions();

    // Método síncrono útil para popular o banco de dados inicial
    @Query("SELECT id FROM regions WHERE name = :name LIMIT 1")
    int getRegionIdByName(String name);
}