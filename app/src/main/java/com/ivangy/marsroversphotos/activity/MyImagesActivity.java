package com.ivangy.marsroversphotos.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;
import com.ivangy.marsroversphotos.model.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MyImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_images);

        try {
            Helper.setRecyclerConfig(findViewById(R.id.recyclerFavoriteImages),
                    new GridLayoutManager(getApplicationContext(), 4),
                    new PhotosAdapter(getImages(getFileStreamPath(getApplicationContext().getResources().getString(R.string.file_favorite_images)))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Photo> getImages(File file) throws IOException {
        ArrayList<Photo> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Photo photo;

            while ((photo = (Photo) ois.readObject()) != null) {
                Log.d("AAA", "1");
                list.add(photo);
            }
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("AAAAA ruim", "2");
        }
        return list;
    }
}