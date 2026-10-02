package edu.facom.avaliacao1.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "birds",
        foreignKeys = @ForeignKey(entity = Region.class,
                parentColumns = "id", // Corrigido de "idRegiao" para "id"
                childColumns = "regiaoId",
                onDelete = ForeignKey.CASCADE))
public class Bird {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;

    public int regiaoId;
    public String imageUri;
    public String soundUri;

    public Bird(String name, int regiaoId, String imageUri, String soundUri) {
        this.name = name;
        this.regiaoId = regiaoId;
        this.imageUri = imageUri;
        this.soundUri = soundUri;
    }
}