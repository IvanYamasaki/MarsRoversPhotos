package com.ivangy.marsroversphotos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.activity.MainActivity;
import com.ivangy.marsroversphotos.activity.PhotoInfoActivity;

import java.util.ArrayList;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    public Context mContext;
    public static ProgressBar pgBarPhoto;

    public PhotosAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.data_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).load(Uri.parse(MainActivity.ListImageURL.get(position)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pgBarPhoto.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pgBarPhoto.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return MainActivity.ListImageURL.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pgBarPhoto = itemView.findViewById(R.id.pgBarPhoto);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            //pgBarPhoto.setVisibility(View.VISIBLE);

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoInfoActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
