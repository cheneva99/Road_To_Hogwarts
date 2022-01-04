package com.isep.roadtohogwarts;

import org.json.JSONObject;

public class Spell {
    private int id;
    private String name;
    private String type;
    private String description;

    public Spell(JSONObject spellJson) {
        try {
            this.id = spellJson.getInt("id");
            this.name = spellJson.getString("name");

            this.type = spellJson.getString("spell_type");
            this.description = spellJson.getString("description");
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

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
