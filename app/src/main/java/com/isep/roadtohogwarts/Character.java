package com.isep.roadtohogwarts;

import org.json.JSONObject;

import java.util.Locale;

public class Character {
    private String name;
    private boolean isStudent;
    private boolean isStaff;
    private String species;
    private String house;
    private String actorName;
    private String patronus;
    private String imageUrl;
    private String gender;


    public Character(JSONObject characterJson) {
        try {
            this.name = characterJson.getString("name");
            this.isStudent = characterJson.getBoolean("hogwartsStudent");
            this.isStaff = characterJson.getBoolean("hogwartsStaff");
            this.species = characterJson.getString("species");
            this.actorName = characterJson.getString("actor");
            if(characterJson.getString("house").equalsIgnoreCase("Huffleluff")){
                this.house = "Hufflepuff";
            }else if(characterJson.getString("house").equalsIgnoreCase("Slythetin")){
                this.house = "Slytherin";
            }else{
                this.house = characterJson.getString("house");
            }
            if(characterJson.getString("patronus").equalsIgnoreCase("")){
                this.patronus = "None";
            }
            else {
                this.patronus = characterJson.getString("patronus");
            }
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
        return isStudent;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public String getSpecies() {
        return formatString(species);
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
        return formatString(patronus);
    }


    public String getImageUrl() {
        return imageUrl;
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

    public String getGender() {

        return formatString(gender); }

    public String formatString(String str){
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}