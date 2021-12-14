package com.example.tallyandreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchEntry2 extends AppCompatActivity {
    DatabaseReference reference;
    List<UploadEntry> reportList;
    ListReportAdapter adapterReport;
    ListView myListView;
    String startDate, endDate, mode;
    int count = 0;
    TextView mCount;
    long credit = 0;
    long debit = 0;
    String creditS, debitS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_entry2);

        mCount = findViewById(R.id.listCount);

        reference = FirebaseDatabase.getInstance().getReference().child("data");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");
        mode = getIntent().getStringExtra("mode");

        Query query1 = reference.orderByChild("date").startAt(startDate).endAt(endDate);

        myListView = findViewById(R.id.myListView);

        reportList = new ArrayList<>();

        query1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reportList.clear();

                    for (DataSnapshot employeeDatasnap : snapshot.getChildren()) {

                        UploadEntry employee = employeeDatasnap.getValue(UploadEntry.class);
                        if(mode.equals("BOTH")){
                            reportList.add(employee);
                            count = count + 1;

                            creditS = employee.getCredit();
                            long tempcredit = Long.parseLong(creditS);
                            credit = credit + tempcredit;

                            debitS = employee.getDebit();
                            long tempdebit = Long.parseLong(debitS);
                            debit = debit + tempdebit;

                        }
                        else if(mode.equals(employee.getMode())) {
                            reportList.add(employee);
                            count = count + 1;

                            creditS = employee.getCredit();
                            long tempcredit = Long.parseLong(creditS);
                            credit = credit + tempcredit;

                            debitS = employee.getDebit();
                            long tempdebit = Long.parseLong(debitS);
                            debit = debit + tempdebit;
                        }
                    }
                    adapterReport = new ListReportAdapter(SearchEntry2.this, reportList);
                    myListView.setAdapter(adapterReport);
                    mCount.setText("Count: "+count+"\nCredit - Debit (Total : " + (credit - debit) + ")");
                }
                else {
                    mCount.setText("Count: "+count);
                    Toast.makeText(SearchEntry2.this, "No data saved between these days.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchEntry2.this, SearchEntry.class));
        finish();
    }

}