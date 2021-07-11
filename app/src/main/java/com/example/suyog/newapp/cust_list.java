package com.example.suyog.newapp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cust_list extends AppCompatActivity {

    DatabaseReference dbRefCust;
    Customer[] custarr=new Customer[200];
    ValueEventListener custListner;
    String distId,name,contact,email,pincode;
    LinearLayout mainLL;
    int custCount=0;
    TextView tv;
    View.OnClickListener clickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_list);

        name=getIntent().getStringExtra("name");
        contact=getIntent().getStringExtra("contact");
        email=getIntent().getStringExtra("email");
        pincode=getIntent().getStringExtra("pincode");

        clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index=view.getId();

                String custId=custarr[index].getCustId();

                Intent intent= new Intent(getApplicationContext(),floatingCustInfo.class);
                intent.putExtra("custId",custId);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(),"id="+id,Toast.LENGTH_SHORT).show();
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
                        int flag=1;
                        String dbName, dbContact, dbEmail, dbPincode, dbAddress;

                        dbName=cust.child("custName").getValue().toString();
                        dbContact=cust.child("custContact").getValue().toString();
                        dbEmail=cust.child("custEmail").getValue().toString();
                        dbPincode=cust.child("custPincode").getValue().toString();
                        dbAddress=cust.child("custAddr").getValue().toString();

                        dbName=dbName.toLowerCase();
                        dbEmail=dbEmail.toLowerCase();

                        if(name!=null)
                            {
                                name=name.toLowerCase();
                                if(!dbName.contains(name))
                                    flag=0;
                            }

                        if(pincode!=null)
                            if(!dbPincode.contains(pincode))
                                flag=0;

                        if(email!=null)
                            {
                                email=email.toLowerCase();
                                if (!dbEmail.contains(email))
                                    flag = 0;
                            }

                        if(contact!=null)
                            if(!dbContact.contains(contact))
                                flag=0;


                        if(flag==1)
                        {
                            custCount++;

                            custarr[custCount]= new Customer();

                            custarr[custCount].setCustName(dbName);
                            custarr[custCount].setCustAddr(dbAddress);
                            custarr[custCount].setCustContact(dbContact);
                            custarr[custCount].setCustEmail(dbEmail);
                            custarr[custCount].setCustPincode(dbPincode);
                            custarr[custCount].setCustId(cust.getKey());

                        }
                    }

                    //Toast.makeText(getApplicationContext(),"Match Count "+custCount,Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
    }

    public void to_search_cust(View view)
    {
        startActivity(new Intent(getApplicationContext(),search_cust.class));
        finish();

    }

    public void to_dist_home(View view)
    {
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
        finish();

    }
}
