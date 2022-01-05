package com.isep.roadtohogwarts;

import org.json.JSONObject;

public class Character {
    private String name;
    private boolean student;
    private boolean staff;
    private String species;
    private String house;
    private String actorName;
    private String patronus;
    private String imageUrl;


    public Character(JSONObject characterJson) {
        try {
            this.name = characterJson.getString("name");
            this.student = characterJson.getBoolean("hogwartsStudent");
            this.staff = characterJson.getBoolean("hogwartsStaff");
            this.species = characterJson.getString("species");
            this.actorName = characterJson.getString("actor");
            this.house = characterJson.getString("house");
            this.patronus = characterJson.getString("patronus");
            this.imageUrl = characterJson.getString("image");
        } catch (Exception err) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStudent() {
        return student;
    }


    public boolean isStaff() {
        return staff;
    }

    public String getSpecies() {
        return species;
    }


    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
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