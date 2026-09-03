package edu.facom.avaliacao1.model;

public class Bird {
    private String name;
    private String region;
    private int imageResId;
    private int soundResId;

    public Bird(String name, String region, int imageResId, int soundResId) {
        this.name = name;
        this.region = region;
        this.imageResId = imageResId;
        this.soundResId = soundResId;
    }

    public String getName() { return name; }
    public String getRegion() { return region; }
    public int getImageResId() { return imageResId; }
    public int getSoundResId() { return soundResId; }
}