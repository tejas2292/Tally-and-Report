package com.example.tallyandreport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AddEntry extends AppCompatActivity {
    FirebaseAuth fAuth;
    TextView mStartDate;
    EditText mDescription, mAmount;
    SearchableSpinner spinner, spinner2, spinner3, spinner4;
    ArrayAdapter<String> adapter, adapter2, adapter3, adapter4;
    ArrayList<String> spinnerDataList, spinnerDataList2, spinnerDataList3, spinnerDataList4;
    String mode, date, source, subSource, description, amount, payment, credit, debit, parentSource, addedBy;
    DatabaseReference referenceSource, reference, referenceModel;
    int totalEntryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        final DialogFragment dialogFragment = new DatePickerDialogTheme4();
        fAuth = FirebaseAuth.getInstance();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String tempAddedBy = fAuth.getCurrentUser().getEmail();
        if(tempAddedBy.equals("test@gmail.com")){
            addedBy = "Testing";
        }
        else if(tempAddedBy.equals("aaqib@gmail.com")){

            addedBy = "Aaqib Nazir";
        }
        else if(tempAddedBy.equals("vibhanshu@gmail.com")){
            addedBy = "Vibhanshu Sonicha";
        }
        spinner = findViewById(R.id.spinner_designation_name);
        spinner2 = findViewById(R.id.spinner_source_name);
        spinner3 = findViewById(R.id.spinner_credit_debit);
        spinner4 = findViewById(R.id.spinner_subsource_name);
        mStartDate = findViewById(R.id.tv_startDate);
        mDescription = findViewById(R.id.description);
        mAmount = findViewById(R.id.amount);

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });
        referenceSource = FirebaseDatabase.getInstance().getReference("source");
        reference = FirebaseDatabase.getInstance().getReference("data");

        ////////////////////////////////////////////////////////////////////////////////////////////
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalEntryCount = (int) dataSnapshot.getChildrenCount();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        spinnerDataList.add(0, "CASH");
        spinnerDataList.add("ONLINE");

        adapter = new ArrayAdapter<String>(AddEntry.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = spinner.getSelectedItem().toString();
                Toast.makeText(AddEntry.this, "" + mode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList2 = new ArrayList<>();
        adapter2 = new ArrayAdapter<String>(AddEntry.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList2);

        spinnerDataList4 = new ArrayList<>();
        adapter4 = new ArrayAdapter<String>(AddEntry.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList4);

        spinner4.setAdapter(adapter4);

        spinner2.setAdapter(adapter2);
        retriveSpinnerData();

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentSource = spinner2.getSelectedItem().toString();
                source = spinner2.getSelectedItem().toString();
                Toast.makeText(AddEntry.this, ""+source, Toast.LENGTH_SHORT).show();
                referenceModel = FirebaseDatabase.getInstance().getReference("source").child(parentSource);
                referenceModel.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Set<String> set = new HashSet<String>();
                        Iterator i = dataSnapshot.getChildren().iterator();
                        while (i.hasNext()) {
                            set.add(((DataSnapshot) i.next()).getKey());
                        }
                        adapter4.clear();
                        adapter4.addAll(set);
                        adapter4.notifyDataSetChanged();
                        spinner4.setAdapter(adapter4);

                        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                subSource = spinner4.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList3 = new ArrayList<>();
        spinnerDataList3.add(0, "CREDIT");
        spinnerDataList3.add("DEBIT");

        adapter3 = new ArrayAdapter<String>(AddEntry.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList3);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment = spinner3.getSelectedItem().toString();
                Toast.makeText(AddEntry.this, "" + payment, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    public void onAdd(View view) {
       date =  mStartDate.getText().toString();
       description = mDescription.getText().toString();
       amount = mAmount.getText().toString();

       if(date.equals("") || description.equals("") || amount.equals("") || source == null){
           Toast.makeText(this, "Fill all data", Toast.LENGTH_SHORT).show();
       }
       else {
           if(payment.equals("CREDIT")){
               credit = amount;
               debit = "0";
               UploadData();

           }
           else {
               debit = amount;
               credit = "0";
               UploadData();
           }
       }

    }

    private void UploadData() {
        UploadEntry u1 = new UploadEntry(date, mode, source, subSource, description, credit, debit , addedBy);
        reference.child(String.valueOf(totalEntryCount + 1)).setValue(u1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddEntry.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddEntry.this, MainActivity.class));
                finish();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            //these three lines are used to for cancle set previous dates
//            calendar.add(Calendar.DATE, 0);
//            Date newDate = calendar.getTime();
//            datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
//            here it ends
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;

            if(month2 < 10){

                formattedMonth = "0" + month2;
            }
            if(day < 10){

                formattedDayOfMonth = "0" + day;
            }
            TextView textView = getActivity().findViewById(R.id.tv_startDate);
            textView.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
            date = textView.getText().toString().trim();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void retriveSpinnerData() {
        referenceSource.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        set.add(((DataSnapshot) i.next()).getKey());
                    }
                    adapter2.clear();
                    adapter2.addAll(set);
                    adapter2.notifyDataSetChanged();
                    spinner2.setAdapter(adapter2);


                }
               else {
                    Toast.makeText(AddEntry.this, "Data not present", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddEntry.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddEntry.this, MainActivity.class));
        finish();
    }
}