package com.ivangy.marsroversphotos.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.model.Photo;

import java.io.File;
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
        photo = (Photo) bundle.getSerializable("photo");
        toggleStar();

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

        btnFavorite.setOnClickListener(v -> {
            if (MainActivity.favoritedPhotos.isFavorited(photo.getId()))
                MainActivity.favoritedPhotos.removeImage(this, photo.getId());
            else
                MainActivity.favoritedPhotos.addImage(this, photo);
            toggleStar();
        });
        btnDownload.setOnClickListener(v -> {
/*            toast(getApplicationContext(), "Image asdadas");
            try {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(dir, photo.getImage());
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(file);
                toast(getApplicationContext(), "Image Downloaded");
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            createDir(photo.getImage());
        });
    }

    public void createDir(String img) {
        if (writeExternalGranted()) {
            final String fileName = "MarsImages";
            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + fileName + "/");

            if (!direct.exists())
                direct.mkdir();

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(img);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(fileName)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + fileName + File.separator + fileName);

            assert dm != null;
            dm.enqueue(request);
            toast(this, "Downloading...");
        }
    }

    public boolean writeExternalGranted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            return true;
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            toast(this, "permission");
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btnDownload.performClick();
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void toggleStar() {
        if (MainActivity.favoritedPhotos.isFavorited(photo.getId()))
            btnFavorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_24));
        else
            btnFavorite.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_border_24));
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

}