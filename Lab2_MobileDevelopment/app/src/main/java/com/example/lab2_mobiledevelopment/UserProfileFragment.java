package com.example.lab2_mobiledevelopment;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UserProfileFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        ImageView imageView = view.findViewById(R.id.user_Image);
        Bitmap batmapBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), batmapBitmap);
        circularBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(circularBitmapDrawable);
        return view;
    }
}
