package com.example.cameraapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cameraapp.R;
import com.example.cameraapp.config.Cache;
import com.example.cameraapp.models.SubReport;

import java.util.ArrayList;

public class Step4Fragment extends Fragment {

    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8;
    TextView showOrhide, checkboxCounter;
    EditText inputOtherMisconduct, inputMisconductDescription, inputActionTaken, inputTimeOfMisconduct;
    Button buttonNext;
    ImageButton buttonBack;
    LinearLayout checkboxesLayout;

    private ArrayList<String> checkboxList = new ArrayList<>();
    private int count = 0;
    Cache cache = new Cache();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step4_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        checkBox1 = view.findViewById(R.id.cb1);
        checkBox2 = view.findViewById(R.id.cb2);
        checkBox3 = view.findViewById(R.id.cb3);
        checkBox4 = view.findViewById(R.id.cb4);
        checkBox5 = view.findViewById(R.id.cb5);
        checkBox6 = view.findViewById(R.id.cb6);
        checkBox7 = view.findViewById(R.id.cb7);
        checkBox8 = view.findViewById(R.id.cb8);
        showOrhide = view.findViewById(R.id.show_or_hide);
        checkboxCounter = view.findViewById(R.id.checkbox_counter);
        inputOtherMisconduct = view.findViewById(R.id.input_other_misconduct);
        inputTimeOfMisconduct = view.findViewById(R.id.input_time_of_misconduct);
        inputMisconductDescription = view.findViewById(R.id.input_misconduct_description);
        inputActionTaken = view.findViewById(R.id.input_action_taken);
        buttonNext = view.findViewById(R.id.button_next);
        buttonBack = view.findViewById(R.id.button_back);
        checkboxesLayout = view.findViewById(R.id.checkboxes_layout);
        setup();
    }

    public void setup(){
        checkBox1.setOnCheckedChangeListener(checkbox1Listener);
        checkBox2.setOnCheckedChangeListener(checkbox2Listener);
        checkBox3.setOnCheckedChangeListener(checkbox3Listener);
        checkBox4.setOnCheckedChangeListener(checkbox4Listener);
        checkBox5.setOnCheckedChangeListener(checkbox5Listener);
        checkBox6.setOnCheckedChangeListener(checkbox6Listener);
        checkBox7.setOnCheckedChangeListener(checkbox7Listener);
        checkBox8.setOnCheckedChangeListener(checkbox8Listener);
        showOrhide.setOnClickListener(showOrHideCheckboxes);
        inputOtherMisconduct.addTextChangedListener(validator);
        inputMisconductDescription.addTextChangedListener(validator);
        inputActionTaken.addTextChangedListener(validator);
        inputTimeOfMisconduct.setOnClickListener(time);
        buttonBack.setOnClickListener(back);
    }

    private CompoundButton.OnCheckedChangeListener checkbox1Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox2Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox3Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox4Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox5Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox6Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox7Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                count += 1;
            }
            else{
                count -= 1;
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private CompoundButton.OnCheckedChangeListener checkbox8Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                inputOtherMisconduct.setVisibility(View.VISIBLE);
                count += 1;
            }
            else{
                count -= 1;
                inputOtherMisconduct.setVisibility(View.GONE);
                inputOtherMisconduct.setText("");
                inputOtherMisconduct.setBackgroundResource(R.drawable.red_input_field);
            }
            checkboxCounter.setText(String.valueOf(count));
            validateAllFields();
        }
    };

    private View.OnClickListener showOrHideCheckboxes = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((showOrhide.getText().toString()).equals("Hide")){
                if (count != 0){
                    checkboxesLayout.setVisibility(View.GONE);
                    showOrhide.setText("Show");
                }
                else{
                    Toast.makeText(getActivity(), "Please choose at least 1 type of misconduct!"
                            , Toast.LENGTH_SHORT).show();
                }
            }
            else if ((showOrhide.getText().toString()).equals("Show")){
                checkboxesLayout.setVisibility(View.VISIBLE);
                showOrhide.setText("Hide");
            }
        }
    };

    private View.OnClickListener time = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimeFragment timeFragment = new TimeFragment();
            timeFragment.show(getFragmentManager(), "TimePicker");
        }
    };

    private View.OnClickListener validNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setSaved();
        }
    };

    private View.OnClickListener invalidNextButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Required field(s) cannot be left empty!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().popBackStack();
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
        public void afterTextChanged(final Editable s) {
            if (!s.toString().trim().isEmpty()){
                if (inputOtherMisconduct.isFocused()){
                    inputOtherMisconduct.setBackgroundResource(R.drawable.green_input_field);
                }
                if (inputMisconductDescription.isFocused()){
                    inputMisconductDescription.setBackgroundResource(R.drawable.green_input_field);
                }
                if (inputActionTaken.isFocused()){
                    inputActionTaken.setBackgroundResource(R.drawable.green_input_field);
                }
            }
            else {
                if (inputOtherMisconduct.isFocused()){
                    inputOtherMisconduct.setBackgroundResource(R.drawable.red_input_field);
                }
                if (inputMisconductDescription.isFocused()){
                    inputMisconductDescription.setBackgroundResource(R.drawable.red_input_field);
                }
                if (inputActionTaken.isFocused()){
                    inputActionTaken.setBackgroundResource(R.drawable.red_input_field);
                }
            }
            validateAllFields();
        }
    };

    public void validateAllFields(){
        boolean valid = false;

        String otherMisconduct = inputOtherMisconduct.getText().toString().trim();
        String misconductDesc = inputMisconductDescription.getText().toString().trim();
        String actionTaken = inputActionTaken.getText().toString().trim();
        String misconductTime = inputTimeOfMisconduct.getText().toString().trim();

        if (count != 0 && !misconductTime.isEmpty() && !misconductDesc.isEmpty() &&
                !actionTaken.isEmpty() && checkBox8.isChecked() && !otherMisconduct.trim().isEmpty()){
            valid = true;
        }
        else if (count != 0 && !misconductTime.isEmpty() && !misconductDesc.isEmpty() &&
                !actionTaken.isEmpty() && !checkBox8.isChecked()){
            valid = true;
        }
        else if (count != 0 && !misconductTime.isEmpty() && !misconductDesc.isEmpty() &&
                !actionTaken.isEmpty() && checkBox8.isChecked() && otherMisconduct.trim().isEmpty()){
            valid = false;
        }

        if (valid){
            buttonNext.setBackgroundResource(R.drawable.blue_button);
            buttonNext.setOnClickListener(validNextButton);
        }
        else{
            buttonNext.setBackgroundResource(R.drawable.grey_button);
            buttonNext.setOnClickListener(invalidNextButton);
        }
    }

    public void setSaved(){
        String cb1 = checkBox1.getText().toString();
        String cb2 = checkBox1.getText().toString();
        String cb3 = checkBox1.getText().toString();
        String cb4 = checkBox1.getText().toString();
        String cb5 = checkBox1.getText().toString();
        String cb6 = checkBox1.getText().toString();
        String cb7 = checkBox1.getText().toString();
        String cb8 = inputOtherMisconduct.getText().toString().trim();

        if (checkBox1.isChecked()){
            checkboxList.add(cb1);
        }
        if (checkBox2.isChecked()){
            checkboxList.add(cb2);
        }
        if (checkBox3.isChecked()){
            checkboxList.add(cb3);
        }
        if (checkBox4.isChecked()){
            checkboxList.add(cb4);
        }
        if (checkBox5.isChecked()){
            checkboxList.add(cb5);
        }
        if (checkBox6.isChecked()){
            checkboxList.add(cb6);
        }
        if (checkBox7.isChecked()){
            checkboxList.add(cb7);
        }
        if (checkBox8.isChecked()){
            checkboxList.add(cb8);
        }

        count = 0;

        SubReport subReport = new SubReport();
        subReport.setTypeOfMisconduct(checkboxList);
        subReport.setTimeOfMisconduct(inputTimeOfMisconduct.getText().toString().trim());
        subReport.setMisconductDesc(inputMisconductDescription.getText().toString().trim());
        subReport.setActionTaken(inputActionTaken.getText().toString().trim());
        if (cache.setSubReportDetailsCache(getActivity(), subReport)){
            Step5Fragment step5Fragment = new Step5Fragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.container, step5Fragment, "step5Fragment")
                    .addToBackStack(null).commit();
        }
    }

    public void getSaved(){
        if (cache.getSubReportDetailsCache(getActivity()) != null){
            SubReport subReport = cache.getSubReportDetailsCache(getActivity());
            checkboxList = subReport.getTypeOfMisconduct();
            for (int i = 0; i < checkboxList.size(); i++){
                String misconduct = checkboxList.get(i);
                if (misconduct.equals(checkBox1.getText().toString())){
                    checkBox1.setChecked(true);
                }
                else if (misconduct.equals(checkBox2.getText().toString())){
                    checkBox2.setChecked(true);
                }
                else if (misconduct.equals(checkBox3.getText().toString())){
                    checkBox3.setChecked(true);
                }
                else if (misconduct.equals(checkBox4.getText().toString())){
                    checkBox4.setChecked(true);
                }
                else if (misconduct.equals(checkBox5.getText().toString())){
                    checkBox5.setChecked(true);
                }
                else if (misconduct.equals(checkBox6.getText().toString())){
                    checkBox6.setChecked(true);
                }
                else if (misconduct.equals(checkBox7.getText().toString())){
                    checkBox7.setChecked(true);
                }
                else {
                    checkBox8.setChecked(true);
                    inputOtherMisconduct.setText(misconduct);
                    inputOtherMisconduct.setBackgroundResource(R.drawable.green_input_field);
                }
            }
            inputTimeOfMisconduct.setText(subReport.getTimeOfMisconduct());
            inputTimeOfMisconduct.setBackgroundResource(R.drawable.green_input_field);
            inputMisconductDescription.setText(subReport.getMisconductDesc());
            inputMisconductDescription.setBackgroundResource(R.drawable.green_input_field);
            inputActionTaken.setText(subReport.getActionTaken());
            inputActionTaken.setBackgroundResource(R.drawable.green_input_field);
            checkboxCounter.setText(String.valueOf(checkboxList.size()));
            if (checkboxList.size() != 0){
                checkboxesLayout.setVisibility(View.GONE);
                showOrhide.setText("Show");
            }
            checkboxList.clear();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getSaved();
        validateAllFields();
    }
}
