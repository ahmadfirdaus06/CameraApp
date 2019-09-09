package com.ahmad.cameraapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ahmad.cameraapp.R;
import com.ahmad.cameraapp.config.Cache;
import com.ahmad.cameraapp.models.Exam;

public class Step2Fragment extends Fragment {

    ImageButton buttonBack;
    Button buttonNext;
    EditText inputCourseCode, inputCourseName, inputExamVenue, inputExamDate, inputExamTime;
    GridLayout layoutButtonNext;

    Cache cache;
    Exam exam = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step2_fragment,
                container, false);
    }

    public void onViewCreated (View view, Bundle savedInstanceState){
        cache = new Cache(getActivity());
        buttonBack = view.findViewById(R.id.button_back);
        buttonNext = view.findViewById(R.id.button_next);
        inputCourseCode = view.findViewById(R.id.input_course_code);
        inputCourseName = view.findViewById(R.id.input_course_name);
        inputExamVenue = view.findViewById(R.id.input_venue_of_exam);
        inputExamDate = view.findViewById(R.id.input_date_of_exam);
        inputExamTime = view.findViewById(R.id.input_time_of_exam);
        layoutButtonNext = view.findViewById(R.id.layout_button_next);
        setup();
    }

    private View.OnClickListener validNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String courseCode = inputCourseCode.getText().toString().trim();
            String courseName = inputCourseName.getText().toString().trim();
            String examVenue = inputExamVenue.getText().toString().trim();
            String examDate = inputExamDate.getText().toString().trim();
            String examTime = inputExamTime.getText().toString().trim();
            exam = new Exam();
            exam.setCourseCode(courseCode);
            exam.setCourseName(courseName);
            exam.setExamVenue(examVenue);
            exam.setExamDate(examDate);
            exam.setExamTime(examTime);
            if (cache.setExamInfoCache(exam)){
                Step3Fragment step3Fragment = new Step3Fragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, step3Fragment, "step3Fragment")
                        .addToBackStack(null).commit();
            }
        }
    };

    private View.OnClickListener invalidNext = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String courseCode = inputCourseCode.getText().toString().trim();
            String courseName = inputCourseName.getText().toString().trim();
            String examVenue = inputExamVenue.getText().toString().trim();
            String examDate = inputExamDate.getText().toString().trim();
            String examTime = inputExamTime.getText().toString().trim();

            if (courseCode.isEmpty()){
                inputCourseCode.setBackgroundResource(R.drawable.red_input_field);
            }
            if (courseName.isEmpty()){
                inputCourseName.setBackgroundResource(R.drawable.red_input_field);
            }
            if (examVenue.isEmpty()){
                inputExamVenue.setBackgroundResource(R.drawable.red_input_field);
            }
            if (examDate.isEmpty()){
                inputExamDate.setBackgroundResource(R.drawable.red_input_field);
            }
            if (examTime.isEmpty()){
                inputExamTime.setBackgroundResource(R.drawable.red_input_field);
            }
            Toast.makeText(getActivity(), "Exam infomation cannot be left empty!",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().getSupportFragmentManager().popBackStack();

        }
    };

    private View.OnClickListener date = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DateFragment dateFragment = new DateFragment();
            dateFragment.show(getFragmentManager(), "DatePicker");
            dateFragment.setCancelable(false);
        }
    };

    private View.OnClickListener time = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TimeFragment timeFragment = new TimeFragment();
            timeFragment.show(getFragmentManager(), "TimePicker");
            timeFragment.setCancelable(false);
        }
    };

    private TextWatcher validator = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0){
                if (inputCourseCode.isFocused()){
                    inputCourseCode.setBackgroundResource(R.drawable.red_input_field);
                }
                if (inputCourseName.isFocused()){
                    inputCourseName.setBackgroundResource(R.drawable.red_input_field);
                }
                if (inputExamVenue.isFocused()){
                    inputExamVenue.setBackgroundResource(R.drawable.red_input_field);
                }
            }
            else if (editable.length() > 0){
                if (inputCourseCode.isFocused()){
                    inputCourseCode.setBackgroundResource(R.drawable.green_input_field);
                }
                if (inputCourseName.isFocused()){
                    inputCourseName.setBackgroundResource(R.drawable.green_input_field);
                }
                if (inputExamVenue.isFocused()){
                    inputExamVenue.setBackgroundResource(R.drawable.green_input_field);
                }
            }
            validateAllFields();
        }
    };

    public void setup(){;
        buttonBack.setOnClickListener(back);
        inputCourseCode.addTextChangedListener(validator);
        inputCourseName.addTextChangedListener(validator);
        inputExamVenue.addTextChangedListener(validator);
        inputExamDate.addTextChangedListener(validator);
        inputExamTime.addTextChangedListener(validator);
        inputExamDate.setOnClickListener(date);
        inputExamTime.setOnClickListener(time);
    }

    public void validateAllFields(){
        String courseCode = inputCourseCode.getText().toString().trim();
        String courseName = inputCourseName.getText().toString().trim();
        String examVenue = inputExamVenue.getText().toString().trim();
        String examDate = inputExamDate.getText().toString().trim();
        String examTime = inputExamTime.getText().toString().trim();
        if (!courseCode.isEmpty() && !courseName.isEmpty() && !examVenue.isEmpty() &&
                !examDate.isEmpty() && !examTime.isEmpty()){
            layoutButtonNext.setBackgroundResource(R.color.blue);
            buttonNext.setOnClickListener(validNext);
        }
        else{
            layoutButtonNext.setBackgroundResource(R.color.darker_grey);
            buttonNext.setOnClickListener(invalidNext);
        }
    }

    public void getSaved(){
        if(cache.getExamInfoCache() != null){
            exam = cache.getExamInfoCache();
            inputCourseCode.setText(exam.getCourseCode());
            inputCourseName.setText(exam.getCourseName());
            inputExamVenue.setText(exam.getExamVenue());
            inputExamDate.setText(exam.getExamDate());
            inputExamTime.setText(exam.getExamTime());
            inputCourseCode.setBackgroundResource(R.drawable.green_input_field);
            inputCourseName.setBackgroundResource(R.drawable.green_input_field);
            inputExamVenue.setBackgroundResource(R.drawable.green_input_field);
            inputExamDate.setBackgroundResource(R.drawable.green_input_field);
            inputExamTime.setBackgroundResource(R.drawable.green_input_field);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getSaved();
        validateAllFields();
    }

}
