package com.example.tallyandreport;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchEntry extends AppCompatActivity {
    Button mShowReport;
    TextView mStartDate, mEndDate;
    String startDate, endDate, mode;
    SearchableSpinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_search_entry);
        mShowReport = findViewById(R.id.download);
        mStartDate = findViewById(R.id.tv_startDate);
        mEndDate = findViewById(R.id.tv_endDate);
        spinner = findViewById(R.id.spinner_payment_mode);

        final DialogFragment dialogFragment = new DatePickerDialogTheme4();

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogTheme4.id = "start";
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogTheme4.id = "end";
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "BOTH");
        spinnerDataList.add("CASH");
        spinnerDataList.add("ONLINE");
        adapter = new ArrayAdapter<String>(SearchEntry.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = spinner.getSelectedItem().toString();
                Toast.makeText(SearchEntry.this, "" + mode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        mShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = mStartDate.getText().toString();
                endDate = mEndDate.getText().toString();

                if (startDate.equals("") || endDate.equals("") ) {
                    Toast.makeText(SearchEntry.this, "Select Date First", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SearchEntry.this, SearchEntry2.class);
                    intent.putExtra("start", startDate);
                    intent.putExtra("end", endDate);
                    intent.putExtra("mode",mode);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        public static String id = null;
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;

            if (month2 < 10) {
                formattedMonth = "0" + month2;
            }
            if (day < 10) {
                formattedDayOfMonth = "0" + day;
            }
            if (id.equals("start")) {
                TextView textView = getActivity().findViewById(R.id.tv_startDate);
                textView.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                date = textView.getText().toString().trim();
            } else if (id.equals("end")) {
                TextView textView2 = getActivity().findViewById(R.id.tv_endDate);
                textView2.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
                date = textView2.getText().toString().trim();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchEntry.this, MainActivity.class));
        finish();
    }
}
