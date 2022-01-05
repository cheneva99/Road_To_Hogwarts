package com.isep.roadtohogwarts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClick(View view) {
        if(view.getId() ==R.id.characterCard){
            Intent intent = new Intent(this,CharacterActivity.class);
            startActivity(intent);
        }
        if(view.getId() ==R.id.spellCard){
            Intent intent = new Intent(this,SpellActivity.class);
            startActivity(intent);
        }
        if(view.getId() ==R.id.potionCard){
            Intent intent = new Intent(this,PotionActivity.class);
            startActivity(intent);
        }

        if(view.getId() ==R.id.bookCard){
            Intent intent = new Intent(this,BookActivity.class);
            startActivity(intent);
        }
    }
}