package com.ivangy.marsroversphotos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ivangy.marsroversphotos.R;

public class NasaWebsiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_website);
    }

    public void callback(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        NasaWebsiteActivity.this.finish();
    }

    public void openCuriosity(View view) {
        Uri uri = Uri.parse("https://mars.nasa.gov/msl/multimedia/images/?page=0&per_page=25&order=pub_date+desc&search=&category=51%3A176&fancybox=true&url_suffix=%3Fsite%3Dmsl");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    public void openSpirit(View view) {
        Uri uri = Uri.parse("https://mars.nasa.gov/mer/gallery/all/spirit.html");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    public void openOpportunity(View view) {
        Uri uri = Uri.parse("https://mars.nasa.gov/mer/gallery/all/opportunity.html");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

}