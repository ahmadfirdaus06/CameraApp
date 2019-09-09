package com.ahmad.cameraapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.bumptech.glide.Glide;

public class EnlargeImageFragment extends Fragment {

    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enlarge_image_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        imageView = view.findViewById(R.id.imageView);
        setup();
    }

    public void setup(){
        Bundle bundle = getArguments();
        if (bundle != null){
            String imagePath = bundle.getString("imagePath");
            Glide.with(getActivity()).load(imagePath).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 1;
//            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath, options));
        }
        else{
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        }
    }
}
