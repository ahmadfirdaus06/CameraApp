package com.example.cameraapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.cameraapp.R;

import java.util.Calendar;

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText inputExamDate;
    Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        inputExamDate = getActivity().findViewById(R.id.input_date_of_exam);
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        return  dpd;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        String realDay = "";
        String realMonth = "";
        if (day < 10){
            realDay = "0" + day;
        }
        else{
            realDay = String.valueOf(day);
        }
        if ((month + 1) < 10){
            realMonth = "0" + month;
        }
        else{
            realMonth = String.valueOf(month + 1);
        }
        inputExamDate.setText(realDay + "-" + realMonth + "-" + year);
        inputExamDate.setBackgroundResource(R.drawable.green_input_field);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (inputExamDate.getText().toString().isEmpty()){
            inputExamDate.setBackgroundResource(R.drawable.red_input_field);
        }
    }
}
