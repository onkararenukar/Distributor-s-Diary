package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class viewBillsCust extends AppCompatActivity
{

    Spinner spMonths,spYears;
    DatabaseReference dbRefCust,dbRefRecords;
    ValueEventListener custListner,recordListner;
    String cName;
    String distId,custId,custName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bills_cust);

        custId=SaveSharedPreference.getUserName(getApplicationContext());
        distId=SaveSharedPreference.getFkOfDist(getApplicationContext());
        spMonths=findViewById(R.id.spnMonths);
        spYears=findViewById(R.id.spnYears);
        getCustName();

        final List<String> list=new ArrayList<String>();

        list.add("January");
        list.add("February");
        list.add("March");
        list.add("April");
        list.add("May");
        list.add("June");
        list.add("July");
        list.add("August");
        list.add("September");
        list.add("October");
        list.add("November");
        list.add("December");


        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(spMonths.getContext(),android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonths.setAdapter(dataAdapter);


        final List<String> list2=new ArrayList<String>();

        for(int y = Calendar.getInstance().get(Calendar.YEAR); y>=2000; y--)
        {
            list2.add(""+y);
        }

        ArrayAdapter<String> dataAdapter2=new ArrayAdapter<String>(spMonths.getContext(),android.R.layout.simple_spinner_item,list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(dataAdapter2);


    }

    public void to_view_bills(View view)
    {
        final String month=""+spMonths.getSelectedItem(),year=""+spYears.getSelectedItem();

        dbRefRecords= FirebaseDatabase.getInstance().getReference("Records");

        recordListner=dbRefRecords.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                ifRecordExists(dataSnapshot,year,month);
                removeRecordListner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void ifRecordExists(DataSnapshot snap,String y,String m)
    {
        int flag=1;

        if(!snap.hasChild(y))
            flag=0;
        else
        if(!snap.child(y).hasChild(m))
            flag=0;

        if(flag==0)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder( this);

            alertDialog.setMessage("Sorry, No records found").setPositiveButton("ok",null);
            AlertDialog dialog=alertDialog.create();
            dialog.show();

            return;
        }

        Intent intent= new Intent(getApplicationContext(),displayBill.class);
        intent.putExtra("custId",custId);
        intent.putExtra("cName",custName);
        intent.putExtra("distId",distId);
        intent.putExtra("month",m);
        intent.putExtra("year",y);
        startActivity(intent);

    }

    private void getCustName()
    {

        dbRefCust=FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId);

        custListner=dbRefCust.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                custName=dataSnapshot.child("custName").getValue().toString();
                removeCustListner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void removeRecordListner()
    {
        dbRefRecords.removeEventListener(recordListner);
    }

    private void removeCustListner()
    {
        dbRefCust.removeEventListener(custListner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        finish();
    }
}

