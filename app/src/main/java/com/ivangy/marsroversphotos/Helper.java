package com.ivangy.marsroversphotos;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class Helper {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setRecyclerConfig(RecyclerView recyclerMarsPhotos, RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        recyclerMarsPhotos.setHasFixedSize(true);
        recyclerMarsPhotos.setLayoutManager(layoutManager);
        recyclerMarsPhotos.setAdapter(adapter);
    }
}
