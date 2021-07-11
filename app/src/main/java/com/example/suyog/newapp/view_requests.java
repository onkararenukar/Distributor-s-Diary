package com.example.suyog.newapp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class view_requests extends AppCompatActivity {

    DatabaseReference dbRefReq,dbRefBuffCust,dbRefCustDefaults;
    LinearLayout mainLL,noResultLL,reqLL;
    String distId,name,addr,contact,custmail;
    int reqCount=0,i;
    String[] custId=new String[50];
    TextView tvCounter,tvName,tvAddr,tvContact;
    ValueEventListener bufflistener,prolistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        distId=SaveSharedPreference.getUserName(view_requests.this);

        mainLL=(LinearLayout) findViewById(R.id.mainLayout);
        noResultLL=(LinearLayout) findViewById(R.id.noresultll);
        reqLL= (LinearLayout) findViewById(R.id.reqLL);

        tvCounter=(TextView) findViewById(R.id.counter);
        tvName=(TextView) findViewById(R.id.name);
        tvAddr=(TextView) findViewById(R.id.address);
        tvContact=(TextView) findViewById(R.id.contact);


        if(distId.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Login Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        dbRefReq= FirebaseDatabase.getInstance().getReference("requests").child(distId);

        prolistner = dbRefReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt : dataSnapshot.getChildren())
                {
                    reqCount++;
                    custId[reqCount]=""+dt.getKey();
                }

                if(reqCount==0)
                    noResultLL.setVisibility(View.VISIBLE);
                else
                {
                    mainLL.setVisibility(View.VISIBLE);
                    tvCounter.setText("1/"+reqCount);
                    i=1;
                    load();
                }

                removeProListner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void load()
    {
        tvCounter.setText(i+"/"+reqCount);
        String id=""+ custId[i];

        dbRefBuffCust=FirebaseDatabase.getInstance().getReference("Buffcust").child(id);

        bufflistener = dbRefBuffCust.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                name=dataSnapshot.child("custName").getValue().toString();
                addr=dataSnapshot.child("custAddr").getValue().toString();
                contact=dataSnapshot.child("custContact").getValue().toString();
                tvName.setText(name);
                tvAddr.setText(addr);
                tvContact.setText(contact);
                removeBuffListner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbRefCustDefaults= dbRefBuffCust.child("defaults");

        if(reqLL.getChildCount()>0)
            reqLL.removeAllViews();

        dbRefCustDefaults.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dt : dataSnapshot.getChildren())
                {
                    LinearLayout linLayout=new LinearLayout(getApplicationContext());
                    reqLL.addView(linLayout,580, LinearLayout.LayoutParams.WRAP_CONTENT);

                    linLayout.setGravity(Gravity.CENTER);
                    linLayout.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) linLayout.getLayoutParams();
                    layoutParams.setMargins(0,0,0,35);

                    LinearLayout ll1,ll2,ll3;
                    ll1 = new LinearLayout(getApplicationContext());
                    ll2 = new LinearLayout(getApplicationContext());
                    ll3 = new LinearLayout(getApplicationContext());

                    linLayout.addView(ll1,580,LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll1.setGravity(Gravity.CENTER);

                    linLayout.addView(ll2,580, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll2.setGravity(Gravity.CENTER);

                    linLayout.addView(ll3,580,LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll3.setGravity(Gravity.CENTER);

                    TextView lblPname,lblCname,lblQty;
                    final TextView Pname,Cname,Qty;

                    lblPname= new TextView(reqLL.getContext());
                    lblCname= new TextView(reqLL.getContext());
                    lblQty= new TextView(reqLL.getContext());

                    Pname = new TextView(reqLL.getContext());
                    Cname = new TextView(reqLL.getContext());
                    Qty = new TextView(reqLL.getContext());

                    lblPname.setPadding(10,0,0,0);
                    lblPname.setText("Product Name");
                    lblPname.setTextColor(Color.BLACK);

                    lblCname.setPadding(10,0,0,0);
                    lblCname.setText("Company Name");
                    lblCname.setTextColor(Color.BLACK);

                    lblQty.setPadding(10,0,0,0);
                    lblQty.setText("Daily Required ");
                    lblQty.setTextColor(Color.BLACK);

                    Pname.setPadding(10,0,0,0);
                    Pname.setTextColor(Color.BLACK);

                    Cname.setPadding(10,0,0,0);
                    Cname.setTextColor(Color.BLACK);

                    Qty.setPadding(10,0,0,0);
                    Qty.setTextColor(Color.BLACK);

                    String temp=dt.getValue().toString();
                    if(!temp.isEmpty())
                        Qty.setText(""+(Float.parseFloat(temp)*1000)+" ml ("+temp+" Ltrs)");

                    ll1.addView(lblPname,200,35);
                    ll1.addView(Pname,380,35);

                    ll2.addView(lblCname,200,35);
                    ll2.addView(Cname,380,35);

                    ll3.addView(lblQty,200,35);
                    ll3.addView(Qty,380,35);

                    String pid;

                    pid= dt.getKey();

                    DatabaseReference dbRefProduct= FirebaseDatabase.getInstance().getReference("product").child(distId).child(pid);

                            dbRefProduct.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String nm,cnm;

                                    nm=dataSnapshot.child("proName").getValue().toString();

                                    String desc;

                                    desc=dataSnapshot.child("description").getValue().toString();

                                    if(!desc.isEmpty())
                                    nm+="  ("+desc+")";

                                    cnm=dataSnapshot.child("compName").getValue().toString();

                                    Pname.setText(""+nm);
                                    Cname.setText(""+cnm);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void prev(View view)
    {
        if(i>1)
        {
            i--;
            load();
        }
    }

    public void next(View view)
    {
        if(i<reqCount)
        {
            i++;
            load();
        }
    }

    public void removeBuffListner()
    {
        dbRefBuffCust.removeEventListener(bufflistener);
    }

    public  void  removeProListner()
    {
        dbRefReq.removeEventListener(prolistner);
    }

    public void accept_req(View view) {
        final String cid=custId[i];

        if(cid.isEmpty())
            return;

        final DatabaseReference dbRefBuffCust,dbRefCust,dbRefReq;

        dbRefBuffCust=FirebaseDatabase.getInstance().getReference("Buffcust").child(cid);

        dbRefCust=FirebaseDatabase.getInstance().getReference("Customer").child(distId);

        dbRefReq=FirebaseDatabase.getInstance().getReference("requests").child(distId).child(cid);

        bufflistener=dbRefBuffCust.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                custmail=dataSnapshot.child("custEmail").getValue().toString();
                dbRefCust.child(cid).setValue(dataSnapshot.getValue());
                dbRefReq.setValue(null);
                dbRefBuffCust.setValue(null);
                Toast.makeText(getApplicationContext(),"Request Accepted",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),view_requests.class));
                finish();
                removeBuffListner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void to_dist_home(View view) {
        startActivity(new Intent(getApplicationContext(),dist_homepage.class));
        finish();
    }

    public void reject_req(View view)
    {
        final String cid=custId[i];

        FirebaseDatabase.getInstance().getReference("requests").child(distId).child(cid).setValue(null);
        FirebaseDatabase.getInstance().getReference("Buffcust").child(cid).child("fkDistId").setValue(null);
        FirebaseDatabase.getInstance().getReference("Buffcust").child(cid).child("defaults").setValue(null);

        startActivity(new Intent(getApplicationContext(),view_requests.class));
        finish();

    }
}
