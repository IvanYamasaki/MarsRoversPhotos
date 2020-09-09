package com.ivangy.marsroversphotos.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.activity.PhotoInfoActivity;
import com.ivangy.marsroversphotos.model.Photo;

import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Photo> list;

    public PhotosAdapter(ArrayList<Photo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.data_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(mContext).load(Uri.parse(list.get(position).getImage())).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);

            imgPhoto.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PhotoInfoActivity.class);
                intent.putExtra("position", getAdapterPosition());
                mContext.startActivity(intent);
            });
        }
    }
}
