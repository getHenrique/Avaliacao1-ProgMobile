package edu.facom.avaliacao1.model;

public class Attraction {
    private String name;
    private String description;
    private String region; // Para sabermos a qual região do Spinner ele pertence
    private int imageResId; // Guarda o ID da image na pasta drawable (ex: R.drawable.img_pantanal)

    public Attraction(String name, String description, String region, int imageResId) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getRegion() { return region; }
    public int getImageResId() { return imageResId; }
}