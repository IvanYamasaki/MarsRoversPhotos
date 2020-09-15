package com.ivangy.marsroversphotos.model;

import android.content.Context;

import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.activity.MainActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.ivangy.marsroversphotos.Helper.toast;

public class FavoritedPhotos implements Serializable {

    private ArrayList<Photo> listPhotos;

    public FavoritedPhotos(Context context) {
        listPhotos = getImages(context);
    }

    public ArrayList<Photo> getListPhotos() {
        return listPhotos;
    }

    public void removeImage(Context context, int id) {
        listPhotos.remove(listPhotos.stream().filter(n -> n.getId() == id).collect(Collectors.toList()).get(0));
        saveImage(context);
        toast(context, "Image Removed from favorites");
    }

    public ArrayList<Photo> getImages(Context context) {
        try {
            FileInputStream fis = new FileInputStream(context.getFileStreamPath(context.getResources().getString(R.string.file_favorite_images)));
            ObjectInputStream ois = new ObjectInputStream(fis);

            FavoritedPhotos favoritedPhotos = (FavoritedPhotos) ois.readObject();

            ois.close();
            fis.close();
            return favoritedPhotos.getListPhotos();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void addImage(Context context, Photo photo) {
        MainActivity.favoritedPhotos.getListPhotos().add(photo);
        saveImage(context);
    }

    public void saveImage(Context context) {
        /*
                File file = new File(this.getFilesDir(), FILENAME);
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write("asd".getBytes());
                fos.close();
        */
        try {
            FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(context.getResources().getString(R.string.file_favorite_images)));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(MainActivity.favoritedPhotos);

            oos.close();
            fos.close();
            toast(context, "Image Saved");
        } catch (IOException e) {
            e.printStackTrace();
            toast(context, "Image couldn't have been saved");
        }
    }

    public boolean isFavorited(int id) {
        return listPhotos.stream().anyMatch(n -> n.getId() == id);
    }

}
