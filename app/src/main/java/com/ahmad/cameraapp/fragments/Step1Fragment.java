package com.ahmad.cameraapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.config.ConnectionCheck;
import com.ahmad.cameraapp.config.DataSource;
import com.ahmad.cameraapp.models.Student;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Step1Fragment extends Fragment {

    EditText inputSearchId;
    ScrollView scrollView;
    CardView layoutDetails;
    Button buttonNext;
    ImageButton buttonBack, buttonSearch;
    TextView textMatricId, textName, textIcOrPassport, textEmail, textPhoneNumber, textProgramme;
    ProgressDialog progressDialog;
    GridLayout layoutButtonNext;

    ConnectionCheck conn;
    DataSource dataSource;
    Cache cache;
    Student student = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step1_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){

        dataSource = new DataSource();
        cache = new Cache(getActivity());
        conn = new ConnectionCheck(getActivity());
        dataSource = new DataSource();
        cache = new Cache(getActivity());
        inputSearchId = view.findViewById(R.id.input_search_id);
        scrollView = view.findViewById(R.id.scrollView);
        layoutDetails = view.findViewById(R.id.layout_details);
        buttonNext = view.findViewById(R.id.button_next);
        buttonSearch = view.findViewById(R.id.button_search);
        textMatricId = view.findViewById(R.id.text_matric_id);
        textName = view.findViewById(R.id.text_name);
        textIcOrPassport = view.findViewById(R.id.text_ic_or_passport);
        textEmail = view.findViewById(R.id.text_email);
        textPhoneNumber = view.findViewById(R.id.text_phone_number);
        textProgramme = view.findViewById(R.id.text_programme);
        buttonBack = view.findViewById(R.id.button_back);
        layoutButtonNext = view.findViewById(R.id.layout_button_next);
        setup();
    }

    public void setup(){
        buttonBack.setOnClickListener(back);
        buttonSearch.setOnClickListener(search);
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            confirmCancelReport();
        }
    };

    private View.OnClickListener search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog = new ProgressDialog(getActivity());
            hideKeyboard(getActivity());
            progressDialog.setMessage("Searching...");
            progressDialog.setCancelable(false);
            String id = inputSearchId.getText().toString().trim();
            if (conn.isOnline()){
                if (!id.isEmpty()){
                    progressDialog.show();
                    HashMap<String, String> params = new HashMap();
                    params.put("matric_id", id);

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, dataSource.getGetStudentUrl(), new JSONObject(params), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.has("data")){
                                            JSONObject data = response.getJSONArray("data").getJSONObject(0);
                                            if (data != null){
                                                student = new Student();
                                                student.setStudentId(data.getString("student_id"));
                                                student.setMatricId(data.getString("matric_id"));
                                                student.setProgramme(data.getString("programme"));
                                                student.setEmail(data.getString("email"));
                                                student.setContactNo(data.getString("contact_no"));
                                                student.setIcOrPassport(data.getString("ic_or_passport"));
                                                student.setName(data.getString("name"));
                                            }
                                            if (student != null){
                                                textMatricId.setText(student.getMatricId());
                                                textName.setText(student.getName());
                                                textIcOrPassport.setText(student.getIcOrPassport());
                                                textPhoneNumber.setText(student.getContactNo());
                                                textEmail.setText(student.getEmail());
                                                textProgramme.setText(student.getProgramme());
                                                inputSearchId.setBackgroundResource(R.drawable.green_input_search_field);
                                                progressDialog.dismiss();
                                                layoutDetails.setVisibility(View.VISIBLE);
                                                scrollView.scrollTo(0,0);
                                                validateAllFields();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "Student not found!", Toast.LENGTH_SHORT).show();
                                            student = null;
                                            inputSearchId.setBackgroundResource(R.drawable.red_input_search_field);
                                            layoutDetails.setVisibility(View.INVISIBLE);
                                            progressDialog.dismiss();
                                            validateAllFields();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        student = null;
                                        validateAllFields();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Connection error!", Toast.LENGTH_SHORT).show();
                                    System.out.println(error.toString());
                                    student = null;
                                    validateAllFields();
                                }
                            });

                    requestQueue.add(jsonObjectRequest);
                }
                else{
                    layoutDetails.setVisibility(View.INVISIBLE);
                    inputSearchId.setBackgroundResource(R.drawable.red_input_search_field);
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Enter student matric id!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getActivity(), "No internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener validNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cache.setStudentInfoCache(student)){
                Step2Fragment step2Fragment = new Step2Fragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .add(R.id.container, step2Fragment, "step2Fragment")
                        .addToBackStack(null).commit();
            }
        }
    };

    private View.OnClickListener invalidNext = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (inputSearchId.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Enter student matric id!", Toast.LENGTH_SHORT).show();
                inputSearchId.setBackgroundResource(R.drawable.red_input_search_field);
            }
            else{
                Toast.makeText(getActivity(), "Student infomation cannot be left empty!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void confirmCancelReport(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Cancel report?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (cache.removeAllReportCache()){
                            MainFragment mainFragment = new MainFragment();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                                    .replace(R.id.container, mainFragment);
                            ft.commit();
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

    public void getSaved(){
        if (cache.getStudentInfoCache() != null){
            student = cache.getStudentInfoCache();
            textMatricId.setText(student.getMatricId());
            textName.setText(student.getName());
            textIcOrPassport.setText(student.getIcOrPassport());
            textPhoneNumber.setText(student.getContactNo());
            textEmail.setText(student.getEmail());
            textProgramme.setText(student.getProgramme());
            inputSearchId.setText(student.getMatricId());
            inputSearchId.setBackgroundResource(R.drawable.green_input_search_field);
            layoutDetails.setVisibility(View.VISIBLE);
        }
    }

    public void validateAllFields(){
        if (student != null){
            layoutButtonNext.setBackgroundResource(R.color.blue);
            buttonNext.setOnClickListener(validNext);
        }
        else{
            layoutButtonNext.setBackgroundResource(R.color.darker_grey);
            buttonNext.setOnClickListener(invalidNext);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getSaved();
        validateAllFields();
    }

}
