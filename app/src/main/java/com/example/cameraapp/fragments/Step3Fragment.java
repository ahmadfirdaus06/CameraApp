package com.example.cameraapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cameraapp.R;
import com.example.cameraapp.adapters.ImageSliderAdapter;
import com.example.cameraapp.config.Cache;
import com.example.cameraapp.models.Evidence;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class Step3Fragment extends Fragment {

    Cache cache;
    ImageSliderAdapter adapter;
    ViewPager imageSlider;
    TextView imageCounter, textEnlarge;
    FloatingActionButton fabAddPic, fabDeletePic;
    Button buttonNext;
    ImageButton buttonBack;
    GridLayout layoutButtonnext;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step3_fragment,
                container, false);
    }

    @SuppressLint("RestrictedApi")
    public void onViewCreated (View view, Bundle savedInstanceState){
        cache = new Cache(getActivity());
        buttonNext = view.findViewById(R.id.button_next);
        buttonBack = view.findViewById(R.id.button_back);
        fabDeletePic = view.findViewById(R.id.fab_delete_picture);
        fabAddPic = view.findViewById(R.id.fab_add_picture);
        imageSlider = view.findViewById(R.id.scrollView);
        imageCounter = view.findViewById(R.id.image_counter);
        textEnlarge = view.findViewById(R.id.text_enlarge);
        layoutButtonnext = view.findViewById(R.id.layout_button_next);
        setup();
    }

    @SuppressLint("RestrictedApi")
    public void setup(){
        buttonNext.setOnClickListener(next);
        buttonBack.setOnClickListener(back);
        fabAddPic.setOnClickListener(addPic);
        fabDeletePic.setOnClickListener(deletePic);
        if (cache.getEvidenceDetailsCache() != null){
            ArrayList<String> imagePaths = cache.getEvidenceDetailsCache().getImagePaths();
            adapter = new ImageSliderAdapter(getActivity(), imagePaths);
            imageSlider.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            imageSlider.setOnScrollChangeListener(slideListener);
            if (adapter.getCount() > 0){
                buttonNext.setBackgroundResource(R.drawable.blue_button);
                imageCounter.setVisibility(View.VISIBLE);
                imageCounter.setVisibility(View.VISIBLE);
                layoutButtonnext.setBackgroundResource(R.color.blue);
                fabDeletePic.setVisibility(View.VISIBLE);
                textEnlarge.setVisibility(View.VISIBLE);
                imageCounter.setText((imageSlider.getCurrentItem() + 1) + " of " + imageSlider.getAdapter().getCount());
            }
            else if (adapter.getCount() == 0){
                layoutButtonnext.setBackgroundResource(R.color.darker_grey);
                imageCounter.setVisibility(View.INVISIBLE);
                textEnlarge.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Please add at least 1 image evidence!", Toast.LENGTH_SHORT).show();
            }

            if (adapter.getCount() == 5){
                fabAddPic.setVisibility(View.INVISIBLE);
            }
            else{
                fabAddPic.setVisibility(View.VISIBLE);
            }
        }
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    private View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (adapter != null){
                if (adapter.getCount() > 0){
                    Step4Fragment step4Fragment = new Step4Fragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.container, step4Fragment, "step4Fragment")
                            .addToBackStack(null)
                            .commit();
                }
                else if (adapter.getCount() == 0){
                    Toast.makeText(getActivity(), "Please add at least 1 image evidence!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "Please add at least 1 image evidence!",
                        Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener addPic = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String[] options = {"From gallery", "Take a picture"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Upload an image");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int option) {
                    if (option == 0){
                        if(ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                                .PERMISSION_GRANTED)
                        {
                            requestPermissions(
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    2000);
                        }
                        else {
                            Intent galleryPick = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryPick.setType("image/*");
                            String[] mimeTypes = {"image/jpeg", "image/jpg"};
                            galleryPick.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                            startActivityForResult(galleryPick, 1000);
                        }
                    }
                    else if (option == 1){
                        Camera2BasicFragment camera = new Camera2BasicFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, camera).addToBackStack(null).commit();
                    }
                }
            });
            builder.show();
        }
    };

    private View.OnClickListener deletePic = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Remove an image");
            builder.setMessage("Are you sure to remove current image?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<String> imagePaths = cache.getEvidenceDetailsCache().getImagePaths();
                            imagePaths.remove(imagePaths.get(imageSlider.getCurrentItem()));
                            Evidence evidence = new Evidence();
                            evidence.setImagePaths(imagePaths);
                            cache.setEvidenceDetailsCache(evidence);
                            adapter.notifyDataSetChanged();
                            refreshUI();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
    };

    private View.OnScrollChangeListener slideListener = new View.OnScrollChangeListener() {
        @SuppressLint("RestrictedApi")
        @Override
        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
            imageCounter.setText((imageSlider.getCurrentItem() + 1) + " of " + imageSlider.getAdapter().getCount());
        }
    };

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void refreshUI(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri imagePathUri = data.getData();
                String imagePath = getRealPathFromURI(getActivity(), imagePathUri);
                if (cache.getEvidenceDetailsCache() != null) {
                    Evidence evidence = cache.getEvidenceDetailsCache();
                    evidence.getImagePaths().add(imagePath);
                    cache.setEvidenceDetailsCache(evidence);
                } else {
                    Evidence evidence = new Evidence();
                    ArrayList<String> imagePaths = new ArrayList<>();
                    imagePaths.add(imagePath);
                    evidence.setImagePaths(imagePaths);
                    cache.setEvidenceDetailsCache(evidence);
                }
            }
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
            refreshUI();
        }
    }


}
