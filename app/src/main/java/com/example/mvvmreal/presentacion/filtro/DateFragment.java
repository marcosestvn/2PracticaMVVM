package com.example.mvvmreal.presentacion.filtro;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mvvmreal.util.Constantes;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

public class DateFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private final Calendar calendario = Calendar.getInstance();

    private DateTime maxDate;
    private DateTime minDate;
    private DateTime currentDate;
    int year = calendario.get(Calendar.YEAR);
    int month = calendario.get(Calendar.MONTH);
    int day = calendario.get(Calendar.DAY_OF_MONTH);
    DateTimeFormatter format = DateTimeFormat.forPattern(Constantes.DefaultPatternFecha);

    DatePickerDialog picker;

    public static DateFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DateFragment fragment = new DateFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.picker = new DatePickerDialog(getActivity(), listener, year, month, day);

        System.out.println(maxDate);
        if (maxDate != null) {
            picker.getDatePicker().setMaxDate(maxDate.getMillis());

        } else {
            picker.getDatePicker().setMaxDate(new Date().getTime());

        }

        if (minDate != null) {
            picker.getDatePicker().setMinDate(minDate.getMillis());
        }


        if (currentDate != null) {
            picker.getDatePicker().updateDate(currentDate.year().get(), currentDate.monthOfYear().get(), currentDate.dayOfMonth().get());
        }

        return picker;
    }

    public void setMaxDate2(String maxDateNueva) {
        this.maxDate = format.parseDateTime(maxDateNueva);
    }

    public void setMinDate2(String minDateNueva) {
        this.minDate = format.parseDateTime(minDateNueva);
    }

    public void setCurrentDate(String dateNueva) {
        this.currentDate = format.parseDateTime(dateNueva);

    }
}