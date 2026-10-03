package edu.facom.avaliacao1.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "attractions",
        foreignKeys = @ForeignKey(entity = Region.class,
                parentColumns = "id", // Aponta para o 'id' em Region
                childColumns = "regiaoId",
                onDelete = ForeignKey.CASCADE))
public class Attraction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;

    // Nova chave estrangeira
    private int regiaoId;

    // Substitui o int imageResId
    private String imageUri;

    public Attraction(String name, String description, int regiaoId, String imageUri) {
        this.name = name;
        this.description = description;
        this.regiaoId = regiaoId;
        this.imageUri = imageUri;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getRegiaoId() { return regiaoId; }
    public String getImageUri() { return imageUri; }
}