package com.example.cameraapp.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cameraapp.R;
import com.example.cameraapp.config.Cache;
import com.example.cameraapp.config.ConnectionCheck;
import com.example.cameraapp.config.DataSource;
import com.example.cameraapp.miscellanous.UploadService;
import com.example.cameraapp.models.Approval;
import com.example.cameraapp.models.ChiefInvigilator;
import com.example.cameraapp.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Step5Fragment extends Fragment {

    TextView showOrHideReportedBy, showOrHideApprovedBy, showOrHideWitness1, showOrHideWitness2,
            textReporterName, textReporterId, textReporterContactNo, textReporterEmail,
            textApproverName, textApproverId, textApproverContactNo, textApproverEmail;
    EditText inputSearchId, inputWitness1Name, inputWitness1ContactNo, inputWitness1Email, inputWitness2Name,
            inputWitness2ContactNo, inputWitness2Email;
    LinearLayout reportByLayout, approvedByLayout1, approvedByLayout2, approvedByLayout3, witness1Layout, witness2Layout;
    Button buttonNext, buttonSearch;
    ImageView buttonBack;
    ProgressDialog progressDialog;
    GridLayout layoutButtonNext;
    AlertDialog alertDialog;

    Cache cache;
    DataSource dataSource;
    ConnectionCheck conn;
    ChiefInvigilator invigilator = null;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step5_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        cache = new Cache(getActivity());
        dataSource = new DataSource();
        conn = new ConnectionCheck(getActivity());
        showOrHideReportedBy = view.findViewById(R.id.show_or_hide_reportedby);
        showOrHideApprovedBy = view.findViewById(R.id.show_or_hide_approved);
        showOrHideWitness1 = view.findViewById(R.id.show_or_hide_witness_1);
        showOrHideWitness2 = view.findViewById(R.id.show_or_hide_witness_2);
        textReporterName = view.findViewById(R.id.text_reporter_name);
        textReporterId = view.findViewById(R.id.text_reporter_id);
        textReporterContactNo = view.findViewById(R.id.text_reporter_contact_no);
        textReporterEmail = view.findViewById(R.id.text_reporter_email);
        textApproverName = view.findViewById(R.id.text_approver_name);
        textApproverId = view.findViewById(R.id.text_approver_id);
        textApproverContactNo = view.findViewById(R.id.text_approver_contact_no);
        textApproverEmail = view.findViewById(R.id.text_approver_email);
        inputSearchId = view.findViewById(R.id.input_search_id);
        inputWitness1Name = view.findViewById(R.id.input_witness_1_name);
        inputWitness1ContactNo = view.findViewById(R.id.input_witness_1_contact_no);
        inputWitness1Email = view.findViewById(R.id.input_witness_1_email);
        inputWitness2Name = view.findViewById(R.id.input_witness_2_name);
        inputWitness2ContactNo = view.findViewById(R.id.input_witness_2_contact_no);
        inputWitness2Email = view.findViewById(R.id.input_witness_2_email);
        reportByLayout = view.findViewById(R.id.reportedby_layout);
        approvedByLayout1 = view.findViewById(R.id.approvedby_layout1);
        approvedByLayout2 = view.findViewById(R.id.approvedby_layout2);
        approvedByLayout3 = view.findViewById(R.id.approvedby_layout3);
        witness1Layout = view.findViewById(R.id.witness_1_layout);
        witness2Layout = view.findViewById(R.id.witness_2_layout);
        buttonNext = view.findViewById(R.id.button_next);
        buttonSearch = view.findViewById(R.id.button_search);
        buttonBack = view.findViewById(R.id.button_back);
        layoutButtonNext = view.findViewById(R.id.layout_button_next);
        setup();
    }

    public void setup(){
        showOrHideReportedBy.setOnClickListener(showOrHideReportedByListener);
        showOrHideApprovedBy.setOnClickListener(showOrHideApprovedByListener);
        showOrHideWitness1.setOnClickListener(showOrHideWitness1Listener);
        showOrHideWitness2.setOnClickListener(showOrHideWitness2Listener);
        inputSearchId.addTextChangedListener(validator);
        inputWitness1Name.addTextChangedListener(validator);
        inputWitness1Email.addTextChangedListener(validator);
        inputWitness1ContactNo.addTextChangedListener(validator);
        inputWitness2Name.addTextChangedListener(validator);
        inputWitness2Email.addTextChangedListener(validator);
        inputWitness2ContactNo.addTextChangedListener(validator);
        reportByLayout.setVisibility(View.GONE);
        showOrHideReportedBy.setText("More");
        if (cache.getApprovalDetailsCache() != null){
            getSaved();
        }
        else{
            newFill();
        }
        buttonBack.setOnClickListener(back);
        buttonSearch.setOnClickListener(search);
    }

    private View.OnClickListener showOrHideReportedByListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((showOrHideReportedBy.getText().toString()).equals("Less")){
                reportByLayout.setVisibility(View.GONE);
                showOrHideReportedBy.setText("More");
            }
            else if ((showOrHideReportedBy.getText().toString()).equals("More")){
                reportByLayout.setVisibility(View.VISIBLE);
                showOrHideReportedBy.setText("Less");
            }
        }
    };

    private View.OnClickListener showOrHideApprovedByListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((showOrHideApprovedBy.getText().toString()).equals("Less")){
                if(cache.getApprovalDetailsCache() != null || invigilator != null){
                    approvedByLayout2.setVisibility(View.VISIBLE);
                }
                else{
                    approvedByLayout2.setVisibility(View.GONE);
                }
                approvedByLayout1.setVisibility(View.GONE);
                approvedByLayout3.setVisibility(View.GONE);
                showOrHideApprovedBy.setText("More");
            }
            else if ((showOrHideApprovedBy.getText().toString()).equals("More")){
                if(cache.getApprovalDetailsCache() != null || invigilator != null){
                    approvedByLayout2.setVisibility(View.VISIBLE);
                    approvedByLayout3.setVisibility(View.VISIBLE);
                }
                else{
                    approvedByLayout2.setVisibility(View.GONE);
                    approvedByLayout3.setVisibility(View.GONE);
                }
                approvedByLayout1.setVisibility(View.VISIBLE);
                showOrHideApprovedBy.setText("Less");
            }
        }
    };

    private View.OnClickListener showOrHideWitness1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ((showOrHideWitness1.getText().toString()).equals("Less")){
                String name = inputWitness1Name.getText().toString().trim();
                String email = inputWitness1Email.getText().toString().trim();
                String contactNo = inputWitness1ContactNo.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || contactNo.isEmpty()){
                    if (name.isEmpty()){
                        inputWitness1Name.setBackgroundResource(R.drawable.red_input_field);
                    }
                    if (email.isEmpty()){
                        inputWitness1Email.setBackgroundResource(R.drawable.red_input_field);
                    }
                    if (contactNo.isEmpty()){
                        inputWitness1ContactNo.setBackgroundResource(R.drawable.red_input_field);
                    }
                    Toast.makeText(getActivity(), "Witness 1 details cannot be left empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    witness1Layout.setVisibility(View.GONE);
                    showOrHideWitness1.setText("More");
                }
            }
            else if ((showOrHideWitness1.getText().toString()).equals("More")){
                witness1Layout.setVisibility(View.VISIBLE);
                showOrHideWitness1.setText("Less");
            }
        }
    };

    private View.OnClickListener showOrHideWitness2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = inputWitness2Name.getText().toString().trim();
            String email = inputWitness2Email.getText().toString().trim();
            String contactNo = inputWitness2ContactNo.getText().toString().trim();
            if ((showOrHideWitness2.getText().toString()).equals("Less")){
                if (name.isEmpty() || email.isEmpty() || contactNo.isEmpty()){
                    if (name.isEmpty()){
                        inputWitness2Name.setBackgroundResource(R.drawable.red_input_field);
                    }
                    if (email.isEmpty()){
                        inputWitness2Email.setBackgroundResource(R.drawable.red_input_field);
                    }
                    if (contactNo.isEmpty()){
                        inputWitness2ContactNo.setBackgroundResource(R.drawable.red_input_field);
                    }
                    Toast.makeText(getActivity(), "Witness 2 details cannot be left empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    witness2Layout.setVisibility(View.GONE);
                    showOrHideWitness2.setText("More");
                }
            }
            else if ((showOrHideWitness2.getText().toString()).equals("More")){
                witness2Layout.setVisibility(View.VISIBLE);
                showOrHideWitness2.setText("Less");
            }
        }
    };

    private View.OnClickListener search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Searching...");
            progressDialog.setCancelable(false);
            String id = inputSearchId.getText().toString().trim();
            if (id.isEmpty()){
                Toast.makeText(getActivity(), "ID cannot be left empty!", Toast.LENGTH_SHORT).show();
                inputSearchId.setBackgroundResource(R.drawable.red_input_field);
            }
            else {
                if (conn.isOnline()){
                    progressDialog.show();
                    HashMap<String, String> params = new HashMap();
                    params.put("staff_id", id);

                    requestQueue = Volley.newRequestQueue(getActivity());
                    jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, dataSource.getGetInvigilatorUrl(), new JSONObject(params), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    progressDialog.dismiss();
                                    try {
                                        if (response.has("data")){
                                            JSONObject data = response.getJSONArray("data").getJSONObject(0);
                                            if (data != null){
                                                invigilator = new ChiefInvigilator();
                                                invigilator.setUserID(data.getString("user_id"));
                                                invigilator.setName(data.getString("name"));
                                                invigilator.setStaffId(data.getString("staff_id"));
                                                invigilator.setContactNo(data.getString("contact_no"));
                                                invigilator.setEmail(data.getString("email"));
                                                invigilator.setUserType(data.getString("user_type"));
                                            }
                                            if (cache.setChiefInvDetailsCache(invigilator)){
                                                textApproverName.setText(invigilator.getName());
                                                textApproverId.setText(invigilator.getStaffId());
                                                textApproverContactNo.setText(invigilator.getContactNo());
                                                textApproverEmail.setText(invigilator.getEmail());
                                                approvedByLayout2.setVisibility(View.VISIBLE);
                                                approvedByLayout3.setVisibility(View.VISIBLE);
                                                inputSearchId.setBackgroundResource(R.drawable.green_input_field);
                                                validateAllFields();
                                            }
                                        }
                                        else{
                                            inputSearchId.setBackgroundResource(R.drawable.red_input_field);
                                            approvedByLayout2.setVisibility(View.GONE);
                                            approvedByLayout3.setVisibility(View.GONE);
                                            invigilator = null;
                                            Toast.makeText(getActivity(), "Invigilator id not found!", Toast.LENGTH_SHORT).show();
                                            validateAllFields();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        invigilator = null;
                                        validateAllFields();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    inputSearchId.setBackgroundResource(R.drawable.red_input_field);
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Connection error!", Toast.LENGTH_SHORT).show();
                                    System.out.println(error.toString());
                                    invigilator = null;
                                    validateAllFields();
                                }
                            });

                    requestQueue.add(jsonObjectRequest);
                }
                else{
                    Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("step5Fragment");
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).remove(currentFragment).commit();
            if (currentFragment == null){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    };

    private View.OnClickListener validNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Approval approval = new Approval();
            approval.setReporterName(cache.getUserPrefCache().getName());
            approval.setReporterUserId(cache.getUserPrefCache().getUserID());
            approval.setReporterId(cache.getUserPrefCache().getStaffId());
            approval.setReporterEmail(cache.getUserPrefCache().getEmail());
            approval.setReporterContactNo(cache.getUserPrefCache().getContactNo());
            approval.setSuperiorName(cache.getChiefInvDetailsCache().getName());
            approval.setSuperiorId(cache.getChiefInvDetailsCache().getStaffId());
            approval.setSuperiorUserId(cache.getChiefInvDetailsCache().getUserID());
            approval.setSuperiorEmail(cache.getChiefInvDetailsCache().getEmail());
            approval.setSuperiorContactNo(cache.getChiefInvDetailsCache().getContactNo());
            approval.setWitness1Name(inputWitness1Name.getText().toString().trim());
            approval.setWitness1Email(inputWitness1Email.getText().toString().trim());
            approval.setWitness1ContactNo(inputWitness1ContactNo.getText().toString().trim());
            approval.setWitness2Name(inputWitness2Name.getText().toString().trim());
            approval.setWitness2Email(inputWitness2Email.getText().toString().trim());
            approval.setWitness2ContactNo(inputWitness2ContactNo.getText().toString().trim());

            if (cache.setApprovalDetailsCache(approval)){
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.SEND_SMS) != PackageManager
                        .PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.SEND_SMS},
                            2000);
                }
                else{
                    if (conn.isOnline()){
                        alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Confirm");
                        alertDialog.setMessage("Are you sure to upload report?\n(You may cancel this message to review the report details.)");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        if (conn.isMobileData()){
                                            alertDialog = new AlertDialog.Builder(getActivity()).create();
                                            alertDialog.setTitle("Confirm");
                                            alertDialog.setMessage("Are you sure to upload using mobile data?\n(Data charges may apply.)");
                                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                                            upload();
                                                        }
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int i) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                        else{
                                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                            upload();
                                        }
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else{
                        Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else{
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener invalidNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String witness1Name = inputWitness1Name.getText().toString().trim();
            String witness1Email = inputWitness1Email.getText().toString().trim();
            String witness1ContactNo = inputWitness1ContactNo.getText().toString().trim();
            String witness2Name = inputWitness2Name.getText().toString().trim();
            String witness2Email = inputWitness2Email.getText().toString().trim();
            String witness2ContactNo = inputWitness2ContactNo.getText().toString().trim();

            if (invigilator == null){
                inputSearchId.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness1Name.isEmpty()){
                inputWitness1Name.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness1Email.isEmpty()){
                inputWitness1Email.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness1ContactNo.isEmpty()){
                inputWitness1ContactNo.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness2Name.isEmpty()){
                inputWitness2Name.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness2Email.isEmpty()){
                inputWitness2Email.setBackgroundResource(R.drawable.red_input_field);
            }
            if (witness2ContactNo.isEmpty()){
                inputWitness2ContactNo.setBackgroundResource(R.drawable.red_input_field);
            }

            Toast.makeText(getActivity(), "Required field(s) cannot be left empty!", Toast.LENGTH_SHORT).show();
        }
    };

    private TextWatcher validator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0){
                if (inputSearchId.isFocused()){
                    inputSearchId.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness1Name.isFocused()){
                    inputWitness1Name.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness1Email.isFocused()){
                    inputWitness1Email.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness1ContactNo.isFocused()){
                    inputWitness1ContactNo.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness2Name.isFocused()){
                    inputWitness2Name.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness2Email.isFocused()){
                    inputWitness2Email.setBackgroundResource(R.drawable.red_input_field);
                }
                else if (inputWitness2ContactNo.isFocused()){
                    inputWitness2ContactNo.setBackgroundResource(R.drawable.red_input_field);
                }
            }
            else{
                if (inputSearchId.isFocused()){
                    inputSearchId.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness1Name.isFocused()){
                    inputWitness1Name.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness1Email.isFocused()){
                    inputWitness1Email.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness1ContactNo.isFocused()){
                    inputWitness1ContactNo.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness2Name.isFocused()){
                    inputWitness2Name.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness2Email.isFocused()){
                    inputWitness2Email.setBackgroundResource(R.drawable.green_input_field);
                }
                else if (inputWitness2ContactNo.isFocused()){
                    inputWitness2ContactNo.setBackgroundResource(R.drawable.green_input_field);
                }
            }
            validateAllFields();
        }
    };

    public void getSaved(){
        Approval approval = cache.getApprovalDetailsCache();
        invigilator = new ChiefInvigilator();
        if (approval != null){
            invigilator.setStaffId(approval.getSuperiorId());
            invigilator.setName(approval.getSuperiorName());
            invigilator.setEmail(approval.getSuperiorEmail());
            invigilator.setContactNo(approval.getSuperiorContactNo());
            textReporterId.setText(approval.getReporterId());
            textReporterName.setText(approval.getReporterName());
            textReporterEmail.setText(approval.getReporterEmail());
            textReporterContactNo.setText(approval.getReporterContactNo());
            inputSearchId.setText(approval.getSuperiorId());
            inputSearchId.setBackgroundResource(R.drawable.green_input_field);
            textApproverId.setText(invigilator.getStaffId());
            textApproverName.setText(invigilator.getName());
            textApproverEmail.setText(invigilator.getEmail());
            textApproverContactNo.setText(invigilator.getContactNo());
            approvedByLayout1.setVisibility(View.GONE);
            approvedByLayout2.setVisibility(View.VISIBLE);
            approvedByLayout3.setVisibility(View.GONE);
            showOrHideApprovedBy.setText("More");
            inputWitness1Name.setText(approval.getWitness1Name());
            inputWitness1Email.setText(approval.getWitness1Email());
            inputWitness1ContactNo.setText(approval.getWitness1ContactNo());
            inputWitness2Name.setText(approval.getWitness2Name());
            inputWitness2Email.setText(approval.getWitness2Email());
            inputWitness2ContactNo.setText(approval.getWitness2ContactNo());
            inputWitness1Name.setBackgroundResource(R.drawable.green_input_field);
            inputWitness1Email.setBackgroundResource(R.drawable.green_input_field);
            inputWitness1ContactNo.setBackgroundResource(R.drawable.green_input_field);
            inputWitness2Name.setBackgroundResource(R.drawable.green_input_field);
            inputWitness2Email.setBackgroundResource(R.drawable.green_input_field);
            inputWitness2ContactNo.setBackgroundResource(R.drawable.green_input_field);
            witness1Layout.setVisibility(View.GONE);
            witness2Layout.setVisibility(View.GONE);
            showOrHideWitness1.setText("More");
            showOrHideWitness2.setText("More");
        }
    }

    public void newFill(){
        if (cache.getUserPrefCache() != null){
            User user = cache.getUserPrefCache();
            textReporterName.setText(user.getName());
            textReporterId.setText(user.getStaffId());
            textReporterContactNo.setText(user.getContactNo());
            textReporterEmail.setText(user.getEmail());
        }
    }

    public void validateAllFields(){
        int validCount = 0;
        String witness1Name = inputWitness1Name.getText().toString();
        String witness1Email = inputWitness1Email.getText().toString();
        String witness1ContactNo = inputWitness1ContactNo.getText().toString();
        String witness2Name = inputWitness2Name.getText().toString();
        String witness2Email = inputWitness2Email.getText().toString();
        String witness2ContactNo = inputWitness2ContactNo.getText().toString();

        if (invigilator != null && !witness1Name.isEmpty() && !witness1Email.isEmpty() &&
                !witness1ContactNo.isEmpty() && !witness2Name.isEmpty() &&
                !witness2Email.isEmpty() && !witness2ContactNo.isEmpty()){
            validCount = 1;
        }
        if (validCount == 1){
            layoutButtonNext.setBackgroundResource(R.color.blue);
            buttonNext.setOnClickListener(validNextButton);
        }
        else if (validCount == 0){
            layoutButtonNext.setBackgroundResource(R.color.darker_grey);
            buttonNext.setOnClickListener(invalidNextButton);
        }
    }

    public void upload(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest = new StringRequest(Request.Method.GET, "http://" + dataSource.getHost() + ":8080",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Intent serviceIntent = new Intent(getActivity(), UploadService.class);
                        ContextCompat.startForegroundService(getActivity(), serviceIntent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Server offline. Try again later.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        validateAllFields();
    }
}
