package com.example.tallyandreport;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ListReportAdapter extends ArrayAdapter {
    private Activity mContext;
    List<UploadEntry> clientList;
    DatabaseReference referenceLeads, referenceCred;
    public ListReportAdapter(Activity mContext, List<UploadEntry> clientList){

        super(mContext, R.layout.list_report,clientList);
        this.mContext = mContext;
        this.clientList = clientList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_report, null, false);

        TextView tvMode = listItemView.findViewById(R.id.rowMode);
        //TextView tvClientType = listItemView.findViewById(R.id.rowClientType);
        TextView tvDate = listItemView.findViewById(R.id.rowDate);
        TextView tvSourceName = listItemView.findViewById(R.id.rowSourceName);
        TextView tvSubSource = listItemView.findViewById(R.id.rowSubSourceName);
        TextView tvDescription = listItemView.findViewById(R.id.rowDescription);
        TextView tvEntryBy = listItemView.findViewById(R.id.rowEntryBy);
        TextView tvCredit = listItemView.findViewById(R.id.rowCredit);
        TextView tvDebit = listItemView.findViewById(R.id.rowDebit);

        UploadEntry employee = clientList.get(position);

        tvMode.setText(employee.getMode());
        tvDate.setText(employee.getDate());
        tvSourceName.setText(employee.getSource_name());
        tvSubSource.setText(employee.getSub_source_name());
        tvDescription.setText(employee.getDescription());
        tvEntryBy.setText(employee.getAddedBy());
        tvCredit.setText(employee.getCredit());
        tvDebit.setText(employee.getDebit());

        return listItemView;
    }


}
