package com.example.cameraapp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.cameraapp.R;

import java.util.Calendar;

public class TimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    EditText inputExamTime, inputTimeOfMisconduct;
    Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        inputExamTime = getActivity().findViewById(R.id.input_time_of_exam);
        inputTimeOfMisconduct = getActivity().findViewById(R.id.input_time_of_misconduct);
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), R.style.DialogTheme,this, hour, min, false);
        return  tpd;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int min) {

        String realHour = "";
        String realMin = "";

        if (hour < 10){
            realHour = "0" + hour;
        }
        else{
            realHour = String.valueOf(hour);
        }

        if (min < 10){
            realMin = "0" + min;
        }
        else{
            realMin = String.valueOf(min);
        }
        if(inputExamTime != null){
            inputExamTime.setText(realHour + ":" + realMin);
            inputExamTime.setBackgroundResource(R.drawable.green_input_field);
        }
        if(inputTimeOfMisconduct != null){
            inputTimeOfMisconduct.setText(realHour + ":" + realMin);
            inputTimeOfMisconduct.setBackgroundResource(R.drawable.green_input_field);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if(inputExamTime != null){
            if (inputExamTime.getText().toString().isEmpty()){
                inputExamTime.setBackgroundResource(R.drawable.red_input_field);
            }
        }
        if(inputTimeOfMisconduct != null){
            if (inputTimeOfMisconduct.getText().toString().isEmpty()){
                inputTimeOfMisconduct.setBackgroundResource(R.drawable.red_input_field);
            }
        }
    }
}
