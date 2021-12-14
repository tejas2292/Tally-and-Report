package com.example.tallyandreport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AddSite extends AppCompatActivity {
    EditText mBrandName, mModelName, mModelName2;
    String brandName, parentBrandName, modelName, modelName2;
    Button mBtnAddBrand, mBtnAddBrand2;
    DatabaseReference reference, referenceBrand;
    ProgressDialog progressDialog;
    SearchableSpinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mModelName = findViewById(R.id.model_name_add);
        mBrandName = findViewById(R.id.brand_name_add);
        mBtnAddBrand = findViewById(R.id.btnAddBrand);
        spinner = findViewById(R.id.spinner_brand);

        mModelName2 = findViewById(R.id.model_name_add2);
        mBtnAddBrand2 = findViewById(R.id.btnAddBrand2);

        reference = FirebaseDatabase.getInstance().getReference("source");
        referenceBrand = FirebaseDatabase.getInstance().getReference().child("source");


        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(AddSite.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);
        spinner.setAdapter(adapter);
        retriveSpinnerData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentBrandName = spinner.getSelectedItem().toString();
                Toast.makeText(AddSite.this, ""+parentBrandName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        mBtnAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(AddSite.this);
                progressDialog.setMessage("Adding your data..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                brandName = mBrandName.getText().toString().trim();
                modelName = mModelName.getText().toString().trim();
                if (TextUtils.isEmpty(brandName) || TextUtils.isEmpty(modelName)) {
                    progressDialog.dismiss();
                    mBrandName.setError("Source Name is required.");
                    mModelName.setError("Sub Source Name is required.");
                    return;
                }
                else {
                    BrandUpload b = new BrandUpload(brandName);
                    reference.child(brandName).child(modelName).setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSite.this, "Source entered successfully.. Now you can go back and try...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSite.this, "Source not entered..", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mBtnAddBrand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(AddSite.this);
                progressDialog.setMessage("Adding your data..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                modelName2 = mModelName2.getText().toString().trim();
                if (TextUtils.isEmpty(modelName2)) {
                    progressDialog.dismiss();
                    mModelName2.setError("Source Name is required.");
                    return;
                }
                else {
                    BrandUpload b = new BrandUpload(parentBrandName);
                    try{
                        reference.child(parentBrandName).child(modelName2).setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(AddSite.this, "Source entered successfully.. Now you can go back and try...", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddSite.this, "Source not entered..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e){
                        Toast.makeText(AddSite.this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void retriveSpinnerData() {
        referenceBrand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                adapter.clear();
                adapter.addAll(set);
                adapter.notifyDataSetChanged();
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddSite.this, MainActivity.class));
        finish();
    }
}
