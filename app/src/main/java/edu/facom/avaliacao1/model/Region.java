package edu.facom.avaliacao1.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "regions")
public class Region {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String imageUri;

    public Region(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }
}