package com.example.studentattendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class CalendarActivity extends DialogFragment {
    // get the instance of calendar
    Calendar calendar = Calendar.getInstance();

    public interface OnCalendarOkClickListener{
        void onClick(int year, int month, int day);
    }

    public OnCalendarOkClickListener onCalendarOkClickListener;

    public void setOnCalendarOkClickListener(OnCalendarOkClickListener onCalendarOkClickListener) {
        this.onCalendarOkClickListener = onCalendarOkClickListener;
    }

    //get the date oncalendar click
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), (new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onCalendarOkClickListener.onClick(year,month,dayOfMonth);
            }
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    void setDate(int year, int month, int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }

    //set the date format
    String getDate(){
        return DateFormat.format("dd.MM.yyyy",calendar).toString();
    }
}
