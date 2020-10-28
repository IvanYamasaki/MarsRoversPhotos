package com.ivangy.marsroversphotos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ivangy.marsroversphotos.model.Photo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MarsPhotos.db",
            PHOTOS_TABLE_NAME = "photos",
            PHOTOS_COLUMN_ID = "id",
            PHOTOS_COLUMN_CAMERA = "camera",
            PHOTOS_COLUMN_CAMERA_FULL = "cameraFullName",
            PHOTOS_COLUMN_IMAGE = "image",
            PHOTOS_COLUMN_SUN = "sun",
            PHOTOS_COLUMN_EARTH_DATE = "earthDate",
            PHOTOS_COLUMN_ROVER = "rover",
            PHOTOS_COLUMN_ROVER_STATUS = "roverStatus",
            PHOTOS_COLUMN_ROVER_LAUNCH_DATE = "roverLaunchDate",
            PHOTOS_COLUMN_ROVER_LAND_DATE = "roverLandingDate";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(String.format(
                "create table %s (%s integer primary key, %s text, %s text, %s text, " +
                        "%s integer, %s text, %s text, %s text, %s text, %s text)",
                PHOTOS_TABLE_NAME, PHOTOS_COLUMN_ID, PHOTOS_COLUMN_CAMERA,
                PHOTOS_COLUMN_CAMERA_FULL, PHOTOS_COLUMN_IMAGE, PHOTOS_COLUMN_SUN,
                PHOTOS_COLUMN_EARTH_DATE, PHOTOS_COLUMN_ROVER, PHOTOS_COLUMN_ROVER_STATUS,
                PHOTOS_COLUMN_ROVER_LAUNCH_DATE, PHOTOS_COLUMN_ROVER_LAND_DATE)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPhoto(Photo p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHOTOS_COLUMN_CAMERA, p.getCamera());
        contentValues.put(PHOTOS_COLUMN_CAMERA_FULL, p.getCameraFullName());
        contentValues.put(PHOTOS_COLUMN_IMAGE, p.getImage());
        contentValues.put(PHOTOS_COLUMN_SUN, p.getQuerySun());
        contentValues.put(PHOTOS_COLUMN_EARTH_DATE, p.getEarthDate());
        contentValues.put(PHOTOS_COLUMN_ROVER, p.getQueryRover());
        contentValues.put(PHOTOS_COLUMN_ROVER_STATUS, p.getRoverStatus());
        contentValues.put(PHOTOS_COLUMN_ROVER_LAUNCH_DATE, p.getRoverLaunchDate());
        contentValues.put(PHOTOS_COLUMN_ROVER_LAND_DATE, p.getRoverLandingDate());

        db.insert(PHOTOS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PHOTOS_TABLE_NAME + " where " + PHOTOS_COLUMN_ID + "=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PHOTOS_TABLE_NAME);
        return numRows;
    }

    public Integer deletePhoto(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PHOTOS_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PHOTOS_TABLE_NAME,
                null,
                null);
    }

    public ArrayList<Photo> getPhotoList() {
        ArrayList<Photo> lista = new ArrayList<Photo>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PHOTOS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Photo p = new Photo(
                    Integer.parseInt(res.getString(res.getColumnIndex(PHOTOS_COLUMN_ID))),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_CAMERA)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_CAMERA_FULL)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_IMAGE)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_EARTH_DATE)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_ROVER)),
                    Integer.parseInt(res.getString(res.getColumnIndex(PHOTOS_COLUMN_SUN))),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_ROVER_STATUS)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_ROVER_LAND_DATE)),
                    res.getString(res.getColumnIndex(PHOTOS_COLUMN_ROVER_LAUNCH_DATE))
            );

            lista.add(p);
            res.moveToNext();
        }

        res.close();
        return lista;
    }
}
