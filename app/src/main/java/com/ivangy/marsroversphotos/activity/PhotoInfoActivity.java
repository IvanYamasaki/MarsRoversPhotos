package com.ivangy.marsroversphotos.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.model.Photo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.ivangy.marsroversphotos.Helper.toast;

public class PhotoInfoActivity extends AppCompatActivity {

    private LinearLayout layoutInfo;
    private ImageButton btnBack, btnDownload, btnFavorite;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);

        layoutInfo = findViewById(R.id.layoutInfo);
        btnBack = findViewById(R.id.btnBack);
        btnDownload = findViewById(R.id.btnDownload);
        btnFavorite = findViewById(R.id.btnFavorite);

        TextView lblEarthDate = findViewById(R.id.lblEarthDate), lblRover = findViewById(R.id.lblRover), lblCam = findViewById(R.id.lblCam), lblLaunchDate = findViewById(R.id.lblLaunchDate), lblLandDate = findViewById(R.id.lblLandDate);
        ImageView imgPhoto = findViewById(R.id.imgPhoto);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        photo = MainActivity.listPhotos.get(bundle.getInt("position"));

        Glide.with(getApplicationContext()).load(Uri.parse(photo.getImage())).into(imgPhoto);
        lblRover.append(" " + photo.getQueryRover() + " (" + photo.getRoverStatus() + ")");
        lblCam.append(" " + photo.getCameraFullName() + " (" + photo.getCamera() + ")");

        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        try {
            lblEarthDate.append(" " + outputFormat.format(Objects.requireNonNull(inputFormat.parse(photo.getEarthDate()))));
            lblLaunchDate.append(" " + outputFormat.format(Objects.requireNonNull(inputFormat.parse(photo.getRoverLaunchDate()))));
            lblLandDate.append(" " + outputFormat.format(Objects.requireNonNull(inputFormat.parse(photo.getRoverLandingDate()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnBack.setOnClickListener(v -> onBackPressed());

        btnFavorite.setOnClickListener(v -> saveImage(photo));

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void toggleAnimations(View v) {
        if (layoutInfo.getVisibility() == VISIBLE)
            animation(false, AnimationUtils.loadAnimation(this, R.anim.fadeout_layout_infos), btnBack, btnDownload, btnFavorite, layoutInfo);
        else
            animation(true, AnimationUtils.loadAnimation(this, R.anim.fadein_layout_infos), btnBack, btnDownload, btnFavorite, layoutInfo);
    }

    private void animation(boolean visible, Animation anim, View... btns) {
        Arrays.asList(btns).forEach(v -> {
            v.startAnimation(anim);
            if (visible) {
                v.setVisibility(VISIBLE);
                v.setClickable(true);
            } else {
                v.setVisibility(INVISIBLE);
                v.setClickable(false);
            }
        });
    }

    public void saveImage(Photo photo) {
        try {
            /*
                File file = new File(this.getFilesDir(), FILENAME);
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write("asd".getBytes());
                fos.close();
            */
            String FileName = getApplicationContext().getResources().getString(R.string.file_favorite_images);
            FileOutputStream fos = new FileOutputStream(getFileStreamPath(FileName));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(photo);
            oos.close();
            fos.close();
            toast(getApplicationContext(), "Image Saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}