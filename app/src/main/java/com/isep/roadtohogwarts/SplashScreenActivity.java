package com.isep.roadtohogwarts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // startActivity(new Intent(SplashScreenActivity.this, BookActivity.class));
        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
        finish();
    }
}