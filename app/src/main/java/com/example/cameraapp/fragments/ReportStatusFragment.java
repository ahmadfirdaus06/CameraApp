package com.example.cameraapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cameraapp.R;

public class ReportStatusFragment extends Fragment {

    Toolbar toolbar;
    TextView textStatus, textTitle;
    ImageButton buttonBack;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_status_fragment,
                container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        toolbar = view.findViewById(R.id.toolbar);
        textStatus = view.findViewById(R.id.text_status);
        textTitle = view.findViewById(R.id.text_title);
        buttonBack = view.findViewById(R.id.button_back);
        setup();
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    public void setup(){
        buttonBack.setOnClickListener(back);
    }

    public void getData(){
        Bundle data = getArguments();

        if (data != null){
            textStatus.setText("Report #" + data.getString("report"));
            textTitle.setText("Report #" + data.getString("report"));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }
}
