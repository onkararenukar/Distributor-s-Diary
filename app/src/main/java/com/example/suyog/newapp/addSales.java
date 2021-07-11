package com.example.suyog.newapp;

import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

public class addSales extends AppCompatActivity {

    String[] proId=new String[10];
    LinearLayout llProducts;
    int procount;
    float cost,total=0;
    String custId,custName,distId,date;
    TextView lblqty,tvDate,tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {

        custId= getIntent().getStringExtra("custId") ;
        custName= getIntent().getStringExtra("cname");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales);
        
        llProducts=findViewById(R.id.productsll);

        tvName=findViewById(R.id.cname);
        tvDate=findViewById(R.id.date);

        Date dt= Calendar.getInstance().getTime();

        SimpleDateFormat dtformat= new SimpleDateFormat("dd-MMMM-yyyy");

        date=dtformat.format(dt);

        tvName.append(custName);
        tvDate.append(date);

        distId=SaveSharedPreference.getUserName(getApplicationContext());
        
        DatabaseReference dbRefProducts= FirebaseDatabase.getInstance().getReference("product").child(distId);

        CheckBox cbx=findViewById(R.id.noMilk);

        cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
            {
                if(!checked)
                    return;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        setNoMilk();
                    }
                },1000);
            }
        });


        if(llProducts.getChildCount()>0)
            llProducts.removeAllViews();

        dbRefProducts.addValueEventListener(new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                procount=0;
                for (DataSnapshot productSnap : dataSnapshot.getChildren())
                {
                    if (productSnap.hasChildren())
                    {
                        procount++;
                        Product obj = productSnap.getValue(Product.class);

                        proId[procount]=productSnap.getKey();

                        LinearLayout prolayout = new LinearLayout(llProducts.getContext());

                        LinearLayout linLayout1 = new LinearLayout(llProducts.getContext());
                        LinearLayout linLayout2 = new LinearLayout(llProducts.getContext());

                        String description = obj.getDescription();

                        llProducts.addView(prolayout, 580, LinearLayout.LayoutParams.WRAP_CONTENT);
                        prolayout.addView(linLayout1,580,35);
                        if (!description.isEmpty())
                            prolayout.addView(linLayout2,580,35);

                        LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) prolayout.getLayoutParams();
                        layoutParams.setMargins(0,0,0,60);

                        prolayout.setOrientation(LinearLayout.VERTICAL);

                        linLayout1.setGravity(Gravity.CENTER);
                        
                        if(!description.isEmpty())
                            linLayout2.setGravity(Gravity.CENTER);

                        TextView lblnm = new TextView(llProducts.getContext());
                        TextView name = new TextView(llProducts.getContext());
                        
                        TextView lbldescript = new TextView(llProducts.getContext());
                        TextView descript = new TextView(llProducts.getContext());
                        
                        lblnm.setGravity(Gravity.CENTER_VERTICAL);
                        lblnm.setTextColor(Color.BLACK);
                        
                        if(!description.isEmpty())
                        {
                            lbldescript.setGravity(Gravity.CENTER_VERTICAL);
                            lbldescript.setTextColor(Color.BLACK);
                        }
                        
                        name.setGravity(Gravity.CENTER_VERTICAL);
                        name.setTextColor(Color.BLACK);
                        
                        if(!description.isEmpty())
                        {
                            descript.setGravity(Gravity.CENTER_VERTICAL);
                            descript.setTextColor(Color.BLACK);
                        }

                        lblnm.setText(" Product  ");
                        if(!description.isEmpty())
                            lbldescript.setText(" Description ");

                        name.setText(obj.getProName()+" "+obj.getCompName());

                        if (!description.isEmpty())
                            descript.setText(obj.getDescription());

                        linLayout1.addView(lblnm,250,35 );
                        linLayout1.addView(name,320,35);
                        

                        if (!description.isEmpty())
                        {
                            linLayout2.addView(lbldescript,250,35);
                            linLayout2.addView(descript,320,35);
                        }

                        LinearLayout linLayout3=new LinearLayout(prolayout.getContext());
                        linLayout3.setGravity(Gravity.CENTER);
                        prolayout.addView(linLayout3,580,35);

                        lblqty= new TextView(llProducts.getContext());
                        lblqty.setId(procount*17);
                        lblqty.setGravity(Gravity.CENTER_VERTICAL);
                        lblqty.setTextColor(Color.BLACK);
                        lblqty.setText(" Quantity : 0ml");

                        linLayout3.addView(lblqty,240,35);

                        final SeekBar sbqty=new SeekBar(prolayout.getContext());
                        sbqty.setId(13*procount);

                        sbqty.setMax(10);

                        sbqty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                        {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                int myid=seekBar.getId();
                                myid/=13;
                                TextView lblqty= (TextView) findViewById(myid*17);
                                lblqty.setText(" Quantity : "+(500*seekBar.getProgress())+"ml");
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        linLayout3.addView(sbqty,330,35);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void setNoMilk()
    {
        String year,month,day;

        StringTokenizer tok= new StringTokenizer(date,"-");

        day=tok.nextToken();
        month=tok.nextToken();
        year=tok.nextToken();

        FirebaseDatabase.getInstance().getReference("Records").child(year).child(month).child(day).child(distId).child(custId).setValue("0");

        finish();
    }

    public void set_qty(View view)
    {
        String year,month,day;

        StringTokenizer tok= new StringTokenizer(date,"-");

        day=tok.nextToken();
        month=tok.nextToken();
        year=tok.nextToken();

        final DatabaseReference dbRefRecords= FirebaseDatabase.getInstance().getReference("Records").child(year).child(month).child(day).child(distId).child(custId);
        dbRefRecords.setValue(null);
        dbRefRecords.child("total").setValue("0");

        int flag=0; // if flag==1 indicates Atleast 1 Seekbar for qty is moved
                    // if zero make toast

        for(int tlv=1;tlv<=procount;tlv++)
        {
            SeekBar skb=findViewById(13*tlv);
            if(skb.getProgress()>0)
            {
                final String proid=proId[tlv];

                final Double qty=(float)(skb.getProgress()*500.000)/1000.00;

                DatabaseReference dbRefProduct=FirebaseDatabase.getInstance().getReference("product").child(distId);

                dbRefProduct.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        cost=0;
                        cost+=(qty*Float.parseFloat(dataSnapshot.child("delCharges").getValue().toString()));

                        cost+=(qty*Float.parseFloat(dataSnapshot.child(proid).child("price").getValue().toString()));

                        addValue(cost);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

                dbRefRecords.child(proid).setValue(qty);

                flag=1;

            }
        }

        if(flag==0)
        {
            Toast.makeText(getApplicationContext(), "Please set quantity for Atleast one product", Toast.LENGTH_LONG).show();
            return;
        }


        Toast.makeText(getApplicationContext(),"Details stored Successfully",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addValue(float c)
    {
        String year,month,day;

        StringTokenizer tok= new StringTokenizer(date,"-");

        day=tok.nextToken();
        month=tok.nextToken();
        year=tok.nextToken();

        total+=c;
        FirebaseDatabase.getInstance().getReference("Records").child(year).child(month).child(day).child(distId).child(custId).child("total").setValue(total);
    }
}
