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
    private String status;
    private String gender;


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
            this.gender = characterJson.getString("gender");
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

    public void setStudent(boolean student) {
        this.student = student;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
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

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getPatronus() {
        return patronus;
    }

    public void setPatronus(String patronus) {
        this.patronus = patronus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        if (isStudent()) {
            return "Hogwarts Student";
        }
        if (isStaff()) {
            return "Hogwarts Staff";
        }
        return "Other";
    }

    public void setStatus(String status) { this.status = status; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }
}