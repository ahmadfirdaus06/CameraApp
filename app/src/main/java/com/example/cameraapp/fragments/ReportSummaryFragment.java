package com.example.cameraapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cameraapp.R;
import com.example.cameraapp.adapters.ImageSliderAdapter;
import com.example.cameraapp.miscellanous.GetReportSummaryAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportSummaryFragment extends Fragment implements GetReportSummaryAsync.AsyncResponse {

    private TextView textTitle, textApprovalStatus, textUploadedDate, textEvidenceCount, textMatricId, textName, textTypeOfMisconduct, textMisconductDescription, imageCounter;
    private ImageButton buttonBack;
    private GetReportSummaryAsync getReportSummaryAsync;
    private ProgressDialog progressDialog;
    private String reportId, approvalStatus, uploadedDate, evidenceCount, matricId, name, typeOfMisconduct, misconductDescription;
    private JSONArray arrayMisconduct, arrayAttachment;
    private ViewPager imageSlider;
    private ConstraintLayout sliderLayout;
    private ImageSliderAdapter adapter;
    private GridLayout expandImages;
    private ImageView showOrHideIcon;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_summary_fragment,
                container, false);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        textTitle = view.findViewById(R.id.text_title);
        textApprovalStatus = view.findViewById(R.id.text_approval_status);
        textUploadedDate = view.findViewById(R.id.text_uploaded_date);
        textEvidenceCount = view.findViewById(R.id.text_evidence_count);
        textMatricId = view.findViewById(R.id.text_matric_id);
        textName = view.findViewById(R.id.text_name);
        textTypeOfMisconduct = view.findViewById(R.id.text_type_of_misconduct);
        textMisconductDescription = view.findViewById(R.id.text_misconduct_description);
        buttonBack = view.findViewById(R.id.button_back);
        imageCounter = view.findViewById(R.id.image_counter);
        imageSlider = view.findViewById(R.id.viewPager);
        sliderLayout = view.findViewById(R.id.layout_view_pager);
        expandImages = view.findViewById(R.id.expand_images);
        showOrHideIcon = view.findViewById(R.id.show_or_hide_icon);
        setup();
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    };

    private View.OnScrollChangeListener slideListener = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View view, int i, int i1, int i2, int i3) {
            imageCounter.setText((imageSlider.getCurrentItem() + 1) + " of " + imageSlider.getAdapter().getCount());
        }
    };

    private View.OnClickListener expand = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int iconTag = (Integer) showOrHideIcon.getTag();
            switch(iconTag) {
                case R.drawable.icon_drop_down:
                    showOrHideIcon.setImageResource(R.drawable.icon_drop_up);
                    showOrHideIcon.setTag(R.drawable.icon_drop_up);
                    sliderLayout.setVisibility(View.VISIBLE);
                    break;
                case R.drawable.icon_drop_up:
                    showOrHideIcon.setImageResource(R.drawable.icon_drop_down);
                    showOrHideIcon.setTag(R.drawable.icon_drop_down);
                    sliderLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public void setup(){
        buttonBack.setOnClickListener(back);
    }

    public void getData(){
        Bundle data = getArguments();

        if (data != null){
            getReportSummaryAsync = new GetReportSummaryAsync(getActivity());
            getReportSummaryAsync.setListener(this).execute(data.getString("report_id"));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    @Override
    public void start() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading, Please wait...");
        progressDialog.show();
    }

    @Override
    public void finish(JSONObject results) {
        //set text material
        try {
            reportId = results.getJSONObject("report_details").getString("report_id");
            approvalStatus = results.getJSONObject("report_details").getString("report_status");
            uploadedDate = results.getJSONObject("report_details").getString("uploaded_by");
            matricId = results.getJSONObject("report_details").getString("matric_id");
            name = results.getJSONObject("report_details").getString("name");
            misconductDescription = results.getJSONObject("report_details").getString("misconduct_description");
            arrayMisconduct = results.getJSONArray("misconductList");
            arrayAttachment = results.getJSONArray("attachmentList");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder listOfOffenses = new StringBuilder();

        for (int i = 0; i < arrayMisconduct.length(); i++){
            try {
                listOfOffenses.append((i + 1) + ". ");
                listOfOffenses.append(arrayMisconduct.getString(i));
                listOfOffenses.append(System.getProperty("line.separator"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typeOfMisconduct = listOfOffenses.toString();

        evidenceCount = "(" + arrayAttachment.length() + ")";

        switch (approvalStatus){
            case "Pending":
                textApprovalStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.darker_grey));
                break;
            case "Approved":
                textApprovalStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                break;
            case "Denied":
                textApprovalStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                break;
        }

        textTitle.setText("Report #" + reportId);
        textApprovalStatus.setText(approvalStatus);
        textUploadedDate.setText(uploadedDate);
        textEvidenceCount.setText(evidenceCount);
        textMatricId.setText(matricId);
        textName.setText(name);
        textTypeOfMisconduct.setText(typeOfMisconduct);
        textMisconductDescription.setText(misconductDescription);

        //set view pager
        ViewGroup.LayoutParams params = sliderLayout.getLayoutParams();
        params.height = sliderLayout.getWidth();
        params.width = sliderLayout.getWidth();
        sliderLayout.setLayoutParams(params);
        sliderLayout.setVisibility(View.GONE);
        adapter = new ImageSliderAdapter(getActivity(), null, arrayAttachment);
        if (adapter != null){
            imageSlider.setAdapter(adapter);
        }
        imageCounter.setText((imageSlider.getCurrentItem() + 1) + " of " + imageSlider.getAdapter().getCount());
        imageSlider.setOnScrollChangeListener(slideListener);

        expandImages.setOnClickListener(expand);

        showOrHideIcon.setImageResource(R.drawable.icon_drop_down);
        showOrHideIcon.setTag(R.drawable.icon_drop_down);


        progressDialog.dismiss();
    }
}
