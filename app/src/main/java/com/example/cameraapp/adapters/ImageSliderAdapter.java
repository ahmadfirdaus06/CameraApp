package com.example.cameraapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cameraapp.R;
import com.example.cameraapp.config.DataSource;
import com.example.cameraapp.fragments.EnlargeImageFragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imagePaths;
    private JSONArray jsonImagePaths;
    private DataSource dataSource = new DataSource();

    public ImageSliderAdapter(Context context, ArrayList<String> imagePaths, JSONArray jsonImagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.jsonImagePaths = jsonImagePaths;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        int size = 0;

        if (imagePaths != null){
            size = imagePaths.size();
        }
        else if (jsonImagePaths != null){
            size = jsonImagePaths.length();
        }

        return size;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final ImageView imageView = new ImageView(context);
        String imageUrl = null;

        if (imagePaths != null){
            Glide.with(context).load(imagePaths.get(position)).into(imageView);
        }
        else if (jsonImagePaths != null){
            try {
                imageUrl = dataSource.getImagePath() + jsonImagePaths.getString(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Glide.with(context).load(imageUrl).into(imageView);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(position), options));
        container.addView(imageView, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnlargeImageFragment enlargeImageFragment = new EnlargeImageFragment();
                Bundle bundle = new Bundle();
                if (imagePaths != null){
                    bundle.putString("imagePath", imagePaths.get(position));
                }else if (jsonImagePaths != null){
                    try {
                        bundle.putString("imagePath", dataSource.getImagePath() + jsonImagePaths.getString(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                enlargeImageFragment.setArguments(bundle);
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.container, enlargeImageFragment).addToBackStack(null).commit();
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
