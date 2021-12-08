package com.isep.roadtohogwarts;

public class Character {
    private String name;
    private String status;
    private String species;
    private String house;
    private String actorName;
    private String patronus;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character(String name, String status, String species, String house, String actorName, String patronus, String imageUrl) {
        this.name = name;
        this.status = status;
        this.species = species;
        this.house = house;
        this.actorName = actorName;
        this.patronus = patronus;
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public void setPatronus(String patronus) {
        this.patronus = patronus;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getHouse() {
        return house;
    }

    public String getActorName() {
        return actorName;
    }

    public String getPatronus() {
        return patronus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
