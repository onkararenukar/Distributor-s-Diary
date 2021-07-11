package com.example.suyog.newapp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class addTodaysSales extends AppCompatActivity
{
    DatabaseReference dbRefCust;
    Customer[] custarr=new Customer[200];
    ValueEventListener custListner;
    String distId,autocustlist;
    LinearLayout mainLL;
    int custCount=0;
    TextView tv;
    View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todays_sales);

        autocustlist="";
        clickListener=new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int index=view.getId();

                if(!autocustlist.contains(""+index))
                    autocustlist+=""+index;

                String custId=custarr[index].getCustId();

                Intent intent= new Intent(getApplicationContext(),addSales.class);
                intent.putExtra("custId",custId);
                intent.putExtra("cname",custarr[index].getCustName());
                startActivity(intent);

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

    public void to_dist_home(View view)
    {
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
        finish();

    }

    public void def_for_rest(View view)
    {
        for(int tlv=1;tlv<=custCount;tlv++) //tlv : Temporary Loop Variable
        {
            if(!autocustlist.contains(""+tlv))
            {

                Date dt=new Date();

                dt= Calendar.getInstance().getTime();

                SimpleDateFormat dtFormat=new SimpleDateFormat("dd-MMMM-yyyy");

                String date=dtFormat.format(dt);

                final String year,month,day;

                StringTokenizer tok= new StringTokenizer(date,"-");

                day=tok.nextToken();
                month=tok.nextToken();
                year=tok.nextToken();

                final String custId=custarr[tlv].getCustId();

                DatabaseReference dbRefCustDefaults = FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId);

                dbRefCustDefaults.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild("defaults"))
                        {
                            FirebaseDatabase.getInstance().getReference("Records").child(year).child(month).child(day).child(distId).child(custId).setValue(dataSnapshot.child("defaults").getValue());
                            FirebaseDatabase.getInstance().getReference("Records").child(year).child(month).child(day).child(distId).child(custId).child("total").setValue(dataSnapshot.child("dailyCost").getValue());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

            }
        }
        Toast.makeText(getApplicationContext(),"Defaluts set for rest of the customers",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
    }
}
