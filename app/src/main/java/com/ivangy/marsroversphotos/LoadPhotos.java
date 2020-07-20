package com.ivangy.marsroversphotos;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class LoadPhotos extends AsyncTaskLoader<String> {
    private String QueryRover, QuerySun;

    public LoadPhotos(Context context, String queryRover, String querySun) {
        super(context);
        QueryRover = queryRover;
        QuerySun = querySun;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.SearchPhoto(QueryRover, QuerySun);
    }
}
