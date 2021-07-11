package com.example.suyog.newapp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class viewBillsDist extends AppCompatActivity {

    Spinner spMonths,spYears;
    DatabaseReference dbRefCust,dbRefRecords;
    Customer[] custarr=new Customer[200];
    ValueEventListener custListner,recordListner;
    String distId;
    LinearLayout mainLL;
    int custCount=0;
    TextView tv;
    View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bills_dist);

        spMonths=findViewById(R.id.spinnerMonths);
        spYears=findViewById(R.id.spinnerYears);

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

        for(int y=Calendar.getInstance().get(Calendar.YEAR);y>=2000;y--)
        {
            list2.add(""+y);
        }

        ArrayAdapter<String> dataAdapter2=new ArrayAdapter<String>(spMonths.getContext(),android.R.layout.simple_spinner_item,list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYears.setAdapter(dataAdapter2);

        clickListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int index=view.getId();

                final String custId=custarr[index].getCustId();
                final String distId=SaveSharedPreference.getUserName(getApplicationContext());
                final String month=""+spMonths.getSelectedItem(),year=""+spYears.getSelectedItem();
                final String cName=custarr[index].getCustName();


                dbRefRecords=FirebaseDatabase.getInstance().getReference("Records");

                recordListner=dbRefRecords.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        ifRecordExists(dataSnapshot,year,month,distId,custId,cName);
                        removeRecordListner();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mainLL= findViewById(R.id.mainLayout);

        distId=SaveSharedPreference.getUserName(getApplicationContext());

        dbRefCust= FirebaseDatabase.getInstance().getReference("Customer").child(distId);

        custListner= dbRefCust.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot cust : dataSnapshot.getChildren())
                {
                    String dbName, dbContact, dbEmail, dbPincode, dbAddress;

                    dbName=cust.child("custName").getValue().toString();
                    dbContact=cust.child("custContact").getValue().toString();
                    dbEmail=cust.child("custEmail").getValue().toString();
                    dbPincode=cust.child("custPincode").getValue().toString();
                    dbAddress=cust.child("custAddr").getValue().toString();

                    dbName=dbName.toLowerCase();
                    dbEmail=dbEmail.toLowerCase();

                    custCount++;

                    custarr[custCount]= new Customer();

                    custarr[custCount].setCustName(dbName);
                    custarr[custCount].setCustAddr(dbAddress);
                    custarr[custCount].setCustContact(dbContact);
                    custarr[custCount].setCustEmail(dbEmail);
                    custarr[custCount].setCustPincode(dbPincode);
                    custarr[custCount].setCustId(cust.getKey());
                }

                if(custCount>0)
                    load();
                else
                {
                    tv=findViewById(R.id.textview);
                    tv.setText("No Customers Found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void ifRecordExists(DataSnapshot snap,String y,String m,String did,String cid, String cnm)
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
        intent.putExtra("custId",cid);
        intent.putExtra("cName",cnm);
        intent.putExtra("distId",did);
        intent.putExtra("month",m);
        intent.putExtra("year",y);
        startActivity(intent);

    }

    private void removeRecordListner()
    {
        dbRefRecords.removeEventListener(recordListner);
    }


    public void load()
    {
        dbRefCust.removeEventListener(custListner);

        for(int x=1;x<=custCount;x++)
        {
            TextView tvCust= new TextView(mainLL.getContext());

            tvCust.setId(x);

            tvCust.setOnClickListener(clickListener);

            mainLL.addView(tvCust,DpTOPx(400),LinearLayout.LayoutParams.WRAP_CONTENT);

            tvCust.setPadding(DpTOPx(20),0,0,0);
            tvCust.setTextColor(Color.BLACK);

            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(DpTOPx(400),LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,15);

            tvCust.setLayoutParams(params);

            tvCust.setText(""+custarr[x].getCustName()+"\n"+custarr[x].getCustAddr()+"\n");

            tvCust.setBackgroundColor(Color.WHITE);

        }
    }


    public int DpTOPx(int dp)
    {
        DisplayMetrics dm= getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp*(dm.xdpi/dm.DENSITY_DEFAULT));
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
    }

}
