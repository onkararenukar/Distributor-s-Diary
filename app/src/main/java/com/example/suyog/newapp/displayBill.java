package com.example.suyog.newapp;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class displayBill extends AppCompatActivity
{

    TextView tvDate,tvcName;
    LinearLayout billLayout;
    DatabaseReference dbRefRecords;
    ValueEventListener recordListner;
    String distId,custId,cName,year,month;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bill);

        month=getIntent().getStringExtra("month");
        year=getIntent().getStringExtra("year");
        distId=getIntent().getStringExtra("distId");
        custId=getIntent().getStringExtra("custId");
        cName=getIntent().getStringExtra("cName");

        tvDate=findViewById(R.id.month);
        tvcName=findViewById(R.id.custName);

        tvDate.setText(month+" "+year);
        tvcName.append(cName);

        billLayout=findViewById(R.id.llbill);

        dbRefRecords= FirebaseDatabase.getInstance().getReference("Records").child(year).child(month);

        recordListner=dbRefRecords.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                load(dataSnapshot);
                removeRecordls();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void load(DataSnapshot snap)
    {
        String zeroPad="";
        float billAmount=0;
        for(int dt=1;dt<31;dt++)
        {

            if(dt<10)
                zeroPad="0";
            else
                zeroPad="";

            if(snap.hasChild(zeroPad+dt))
                if(snap.child(zeroPad+dt).hasChild(distId))
                    if(snap.child(zeroPad+dt).child(distId).hasChild(custId))
                    {
                        DataSnapshot rec=snap.child(zeroPad+dt).child(distId).child(custId);

                        LinearLayout ll=new LinearLayout(billLayout.getContext());
                        ll.setGravity(Gravity.CENTER);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        billLayout.addView(ll,DpToPx(330),LinearLayout.LayoutParams.WRAP_CONTENT);

                        TextView tv1,tv2,tv3;

                        tv1= new TextView(getApplicationContext());
                        tv2= new TextView(getApplicationContext());
                        tv3= new TextView(getApplicationContext());

                        tv1.setTextColor(Color.BLACK);
                        tv2.setTextColor(Color.BLACK);
                        tv3.setTextColor(Color.BLACK);

                        tv1.setGravity(Gravity.CENTER);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setPadding(0,5,0,5);
                        tv3.setGravity(Gravity.CENTER);

                        tv1.setBackgroundResource(R.drawable.button_border_thin);
                        tv2.setBackgroundResource(R.drawable.button_border_thin);
                        tv3.setBackgroundResource(R.drawable.button_border_thin);

                        ll.addView(tv1,DpToPx(67),LinearLayout.LayoutParams.MATCH_PARENT);
                        ll.addView(tv2,DpToPx(172),LinearLayout.LayoutParams.WRAP_CONTENT);
                        ll.addView(tv3,DpToPx(91),LinearLayout.LayoutParams.MATCH_PARENT);

                        if(rec.getValue().equals("0"))
                        {
                            tv1.setText(""+dt);
                            tv2.setText("No Milk Taken");
                            tv3.setText("-");
                        }

                        if(rec.getChildrenCount()>0)
                        {
                            tv1.setText(""+dt);
                            for(DataSnapshot pro : rec.getChildren())
                            {
                                    getProductName(pro.getKey(),tv2);
                                    if(!pro.getKey().equalsIgnoreCase("total"))
                                    tv2.append(pro.getValue().toString()+" ltrs ");
                            }
                            tv3.setText(rec.child("total").getValue()+"₹");

                            billAmount+=Float.parseFloat(rec.child("total").getValue().toString());
                        }
                    }
        }

        TextView tt=findViewById(R.id.total);
        tt.append(""+billAmount+"₹");
    }

    private void removeRecordls()
    {
        dbRefRecords.removeEventListener(recordListner);
    }

    public int DpToPx(int dp)
    {
        DisplayMetrics dm= getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp*(dm.xdpi/dm.DENSITY_DEFAULT));
    }

    private void getProductName(String proId,final TextView tv)
    {
        if(proId.equalsIgnoreCase("total"))
            return;
        DatabaseReference dbRefProducts=FirebaseDatabase.getInstance().getReference("product").child(distId).child(proId);

        dbRefProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String str="";
                str+=" "+dataSnapshot.child("proName").getValue().toString();
                str+=" "+dataSnapshot.child("compName").getValue().toString();

                tv.append(str);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
