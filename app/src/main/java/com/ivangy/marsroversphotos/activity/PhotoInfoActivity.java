package com.ivangy.marsroversphotos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ivangy.marsroversphotos.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class PhotoInfoActivity extends AppCompatActivity {

    private LinearLayout layoutInfo;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);

        layoutInfo = findViewById(R.id.layoutInfo);
        btnBack = findViewById(R.id.btnBack);

        TextView lblRover = findViewById(R.id.lblRover), lblCam = findViewById(R.id.lblCam), lblLaunchDate = findViewById(R.id.lblLaunchDate), lblLandDate = findViewById(R.id.lblLandDate);
        ImageView imgPhoto = findViewById(R.id.imgPhoto);
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");

        String LaunchDate = MainActivity.RoverLaunchDate,
                LandDate = MainActivity.RoverLandingDate;

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        Glide.with(getApplicationContext()).load(Uri.parse(MainActivity.ListImageURL.get(position))).into(imgPhoto);
        lblRover.append(" " + MainActivity.queryRover + " (" + MainActivity.RoverStatus + ")");
        lblCam.append(" " + MainActivity.ListCameraFullName.get(position) + " (" + MainActivity.ListCameraName.get(position) + ")");
        try {
            lblLaunchDate.append(" " + outputFormat.format(inputFormat.parse(LaunchDate)));
            lblLandDate.append(" " + outputFormat.format(inputFormat.parse(LandDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fadein_layout_infos);
        final Animation fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout_layout_infos);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecyclerPhotosActivity.class));
                PhotoInfoActivity.this.finish();
            }
        });

        layoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.setVisibility(INVISIBLE);
                layoutInfo.setClickable(false);
                layoutInfo.startAnimation(fadeout);
                btnBack.startAnimation(fadeout);
                layoutInfo.setVisibility(INVISIBLE);
                btnBack.setVisibility(INVISIBLE);
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutInfo.getVisibility() == VISIBLE) {
                    btnBack.setVisibility(INVISIBLE);
                    layoutInfo.setClickable(false);
                    layoutInfo.startAnimation(fadeout);
                    btnBack.startAnimation(fadeout);
                    layoutInfo.setVisibility(INVISIBLE);
                    btnBack.setVisibility(INVISIBLE);
                } else {
                    btnBack.setVisibility(VISIBLE);
                    layoutInfo.setClickable(true);
                    layoutInfo.startAnimation(fadein);
                    btnBack.startAnimation(fadein);
                    layoutInfo.setVisibility(VISIBLE);
                    btnBack.setVisibility(VISIBLE);
                }
            }
        });
    }
}