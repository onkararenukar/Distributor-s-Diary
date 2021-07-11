package com.example.suyog.newapp;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class search_dist extends AppCompatActivity {

    DatabaseReference distref,reqRef,dbRefbuffCust;
    int distCount=0,i=0,procount=0;
    float cost,total;
    Distributor[] dist=new Distributor[100];
    TextView etCounter,etName,etAddr,etContact,etDelCharges,etOtherPros,lblqty,lblSeekbar;
    LinearLayout llProducts,mainll,noresultll;

    String proId[]=new String[10];
    String custId=null,pincode=null;

    ValueEventListener buffCustListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dist);

        pincode = getIntent().getStringExtra("pincode");
        custId = getIntent().getStringExtra("id");

        distref= FirebaseDatabase.getInstance().getReference("Distributor");

        etCounter=(TextView) findViewById(R.id.counter);

        etName=(TextView) findViewById(R.id.name);
        etAddr=(TextView) findViewById(R.id.address);
        etContact=(TextView) findViewById(R.id.contact);
        etDelCharges=(TextView) findViewById(R.id.delCharges);
        etOtherPros=(TextView) findViewById(R.id.extraProducts);

        llProducts=(LinearLayout)findViewById(R.id.productsll);
        mainll=(LinearLayout) findViewById(R.id.mainLayout);
        noresultll=(LinearLayout) findViewById(R.id.noresultll);

        distref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.d("loop2","Here");
                for (DataSnapshot distSnap : dataSnapshot.getChildren())
                {
                    String str = distSnap.child("distPincode").getValue().toString();
                    if (pincode.equals(str))
                    {
                        dist[distCount] = distSnap.getValue(Distributor.class);
                        distCount++;
                    }

                }
                if (distCount > 0)
                {       i++;
                    etCounter.setText("" + i + "/" + distCount);
                    loadDist();
                }

                if(distCount==0)
                {
                    mainll.setVisibility(View.GONE);
                    noresultll.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("Buffcust").child(custId).setValue(null);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeBuffListner()
    {
        dbRefbuffCust.removeEventListener(buffCustListener);
    }

    private void loadDist() {
        if(distCount>0 && i>0 && i<=distCount)
        {
            etName.setText(dist[i-1].getDistName());
            etAddr.setText(dist[i-1].getDistAddr());
            etContact.setText(dist[i-1].getDistContact());

            String distId=dist[i-1].getDistId();
            DatabaseReference dbRefProducts=FirebaseDatabase.getInstance().getReference("product").child(distId);

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
                        if(productSnap.getKey().equals("delCharges"))
                            etDelCharges.setText(productSnap.getValue().toString()+" ₹");

                        if(productSnap.getKey().equals("others"))
                            etOtherPros.setText(productSnap.getValue().toString());

                        if (productSnap.hasChildren())
                        {
                            procount++;
                            Product obj = productSnap.getValue(Product.class);

                            proId[procount]=productSnap.getKey();

                            LinearLayout prolayout = new LinearLayout(llProducts.getContext());

                            LinearLayout linLayout1 = new LinearLayout(llProducts.getContext());
                            LinearLayout linLayout2 = new LinearLayout(llProducts.getContext());
                            LinearLayout linLayout3 = new LinearLayout(llProducts.getContext());
                            LinearLayout linLayout4 = new LinearLayout(llProducts.getContext());

                            String description = obj.getDescription();

                            llProducts.addView(prolayout, 580, LinearLayout.LayoutParams.WRAP_CONTENT);
                            prolayout.addView(linLayout1,580,50);
                            prolayout.addView(linLayout2,580,50);
                            prolayout.addView(linLayout3,580,50);
                            if (!description.isEmpty())
                                prolayout.addView(linLayout4,580,50);

                            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) prolayout.getLayoutParams();
                            layoutParams.setMargins(0,0,0,60);

                            prolayout.setOrientation(LinearLayout.VERTICAL);

                            linLayout1.setGravity(Gravity.CENTER);
                            linLayout2.setGravity(Gravity.CENTER);
                            linLayout3.setGravity(Gravity.CENTER);


                            if(!description.isEmpty())
                                linLayout4.setGravity(Gravity.CENTER);

                            TextView lblnm = new TextView(llProducts.getContext());
                            TextView name = new TextView(llProducts.getContext());

                            TextView lblcname = new TextView(llProducts.getContext());
                            TextView cname = new TextView(llProducts.getContext());

                            TextView lblprice = new TextView(llProducts.getContext());
                            TextView price = new TextView(llProducts.getContext());

                            TextView lbldescript = new TextView(llProducts.getContext());
                            TextView descript = new TextView(llProducts.getContext());

                            lblcname.setGravity(Gravity.CENTER_VERTICAL);
                            lblcname.setTextColor(Color.BLACK);

                            lblnm.setGravity(Gravity.CENTER_VERTICAL);
                            lblnm.setTextColor(Color.BLACK);

                            lblprice.setGravity(Gravity.CENTER_VERTICAL);
                            lblprice.setTextColor(Color.BLACK);

                            if(!description.isEmpty())
                            {
                                lbldescript.setGravity(Gravity.CENTER_VERTICAL);
                                lbldescript.setTextColor(Color.BLACK);
                            }

                            cname.setGravity(Gravity.CENTER_VERTICAL);
                            cname.setTextColor(Color.BLACK);

                            name.setGravity(Gravity.CENTER_VERTICAL);
                            name.setTextColor(Color.BLACK);

                            price.setGravity(Gravity.CENTER_VERTICAL);
                            price.setTextColor(Color.BLACK);

                            if(!description.isEmpty())
                            {
                                descript.setGravity(Gravity.CENTER_VERTICAL);
                                descript.setTextColor(Color.BLACK);
                            }

                            lblnm.setText(" Product Name ");
                            lblcname.setText(" Company Name ");
                            lblprice.setText(" Price ");
                            if(!description.isEmpty())
                                lbldescript.setText(" Description ");

                            name.setText(obj.getProName());
                            cname.setText(obj.getCompName());
                            price.setText(obj.getPrice()+" ₹");

                            if (!description.isEmpty())
                                descript.setText(obj.getDescription());

                            linLayout1.addView(lblnm,250,50 );
                            linLayout1.addView(name,320,50);

                            linLayout2.addView(lblcname,250,50 );
                            linLayout2.addView(cname,320,50);

                            linLayout3.addView(lblprice,250,50);
                            linLayout3.addView(price,320,50);

                            if (!description.isEmpty())
                            {
                                linLayout4.addView(lbldescript,250,50);
                                linLayout4.addView(descript,320,50);
                            }

                            LinearLayout linLayout5=new LinearLayout(prolayout.getContext());
                            linLayout5.setGravity(Gravity.CENTER);
                            prolayout.addView(linLayout5,580,50);

                            lblqty= new TextView(llProducts.getContext());
                            lblqty.setId(procount*17);
                            lblqty.setGravity(Gravity.CENTER_VERTICAL);
                            lblqty.setTextColor(Color.BLACK);
                            lblqty.setText(" Quantity : 0ml");

                            linLayout5.addView(lblqty,240,50);
                            LinearLayout linLayout6=new LinearLayout(prolayout.getContext());
                            linLayout6.setGravity(Gravity.CENTER);
                            prolayout.addView(linLayout6,580,50);

                            final SeekBar sbqty=new SeekBar(prolayout.getContext());
                            sbqty.setId(13*procount);
                            sbqty.setMax(10);

                            sbqty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    int myid=seekBar.getId();
                                    myid/=13;
                                    linLayout6.removeAllViews();
                                    lblSeekbar=new TextView(llProducts.getContext());
                                    lblSeekbar.setId(myid*17);
                                    lblSeekbar.setGravity(Gravity.RIGHT);
                                    lblSeekbar.setTextColor(Color.RED);
                                    lblSeekbar.setText((500*seekBar.getProgress())+"ml");
                                    linLayout6.addView(lblSeekbar,240,50);
//                                    TextView lblqty= (TextView) findViewById(myid*17);
//                                    lblqty.setText(" Quantity : "+(500*seekBar.getProgress())+"ml");
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                            linLayout5.addView(sbqty,330,50);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Registration.class));
        finish();

    }



    public void prev(View view) {
        if(i>1)
        {
            i--;
            etCounter.setText("" + i + "/" + distCount);
            loadDist();
        }
    }

    public void next(View view)
    {
            if(i<distCount)
            {
                i++;
                etCounter.setText("" + i + "/" + distCount);
                loadDist();
            }
    }

    public void sendreq(View view)
    {
        String distId=dist[i-1].getDistId();
        int flag=0; // if flag==1 indicates Atleast 1 Seekbar for qty is moved
                    // if zero make toast

        for(int tlv=1;tlv<=procount;tlv++)
        {
            SeekBar skb=(SeekBar) findViewById(13*tlv);
            if(skb.getProgress()>0)
            {

                final String proid=proId[tlv];
                DatabaseReference dbRefDefPro= FirebaseDatabase.getInstance().getReference("Buffcust").child(custId).child("defaults");

                final Double qty=(float)(skb.getProgress()*500.000)/1000.00;
                dbRefDefPro.child(proid).setValue(qty);

                DatabaseReference dbRefProduct=FirebaseDatabase.getInstance().getReference("product").child(distId);

                dbRefProduct.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        cost=0;
                        cost+=(qty*Float.parseFloat(dataSnapshot.child("delCharges").getValue().toString()));

                        Toast.makeText(getApplicationContext(),"Cost 1"+cost,Toast.LENGTH_SHORT);

                        cost+=(qty*Float.parseFloat(dataSnapshot.child(proid).child("price").getValue().toString()));

                        addValue(cost);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });


                flag=1;
            }
        }

        if(flag==0)
        {
            Toast.makeText(getApplicationContext(), "Please set quantity for Atleast one product", Toast.LENGTH_LONG).show();
            return;
        }


        reqRef= FirebaseDatabase.getInstance().getReference("requests");

        reqRef.child(distId).child(custId).setValue("  ");

        FirebaseDatabase.getInstance().getReference("Buffcust").child(custId).child("fkDistId").setValue(distId);

        Toast.makeText(getApplicationContext(),"Registration Successfull\nYou Can login when distributor Accepts your request",Toast.LENGTH_LONG).show();

        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void to_new_cust_form(View view)
    {
            startActivity(new Intent(getApplicationContext(), cust_reg.class));
            finish();
    }

    private void addValue(float c)
    {
        //String distId= dist[i].getDistId();
        total+=c;
        FirebaseDatabase.getInstance().getReference("Buffcust").child(custId).child("dailyCost").setValue(total);
    }
}
