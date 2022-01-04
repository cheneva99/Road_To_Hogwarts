package com.isep.roadtohogwarts;

import org.json.JSONObject;

public class Potion {
    private int id;
    private String name;
    private String description;

    public Potion(JSONObject potionsJson) {
        try {
            this.id = potionsJson.getInt("id");
            this.name = potionsJson.getString("name");
            this.description=potionsJson.getString("description");
        }catch(Exception err){
            err.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
