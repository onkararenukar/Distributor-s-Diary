package com.example.suyog.newapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class addCustomer extends AppCompatActivity
{
    EditText etname,etaddr,etpincode,etemail,etcontact;
    DatabaseReference dbRefCust,dbRefBuffCust,dbRefDist,dbRefProduct;
    String distId,duplicate=" ";
    int proceed; float cost,total;

    int procount=0,deleted=0;
    ProgressDialog dialog;
    LinearLayout llProducts;
    TextView lblqty;
    String proId[]=new String[10],custId;
    DataSnapshot snap1,snap2,snap3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        distId=SaveSharedPreference.getUserName(getApplicationContext());
        dbRefCust= FirebaseDatabase.getInstance().getReference("Customer").child(distId);
        dbRefProduct = FirebaseDatabase.getInstance().getReference("product").child(distId);
        etname=  findViewById(R.id.name);
        etaddr=  findViewById(R.id.address);
        etpincode= findViewById(R.id.pincode);
        etemail= findViewById(R.id.email);
        etcontact= findViewById(R.id.mobile);

        llProducts = (LinearLayout) findViewById(R.id.productsll);
        dialog = new ProgressDialog(addCustomer.this);

        if (llProducts.getChildCount() > 0)
            llProducts.removeAllViews();
        dbRefProduct.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                procount = 0;
                for (DataSnapshot productSnap : dataSnapshot.getChildren()) {
                    if (productSnap.hasChildren()) {
                        procount++;

                        Product obj = productSnap.getValue(Product.class);

                        proId[procount] = productSnap.getKey();

                        LinearLayout prolayout = new LinearLayout(llProducts.getContext());
                        LinearLayout linLayout1 = new LinearLayout(llProducts.getContext());
                        LinearLayout linLayout2 = new LinearLayout(llProducts.getContext());
                        LinearLayout linLayout3 = new LinearLayout(llProducts.getContext());

                        llProducts.addView(prolayout, 550, LinearLayout.LayoutParams.WRAP_CONTENT);
                        prolayout.addView(linLayout1, 550, 50);
                        prolayout.addView(linLayout2, 550, 50);
                        prolayout.addView(linLayout3, 550, 50);

                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) prolayout.getLayoutParams();
                        layoutParams.setMargins(0, 0, 0, 60);

                        prolayout.setOrientation(LinearLayout.VERTICAL);

                        linLayout1.setGravity(Gravity.CENTER);
                        linLayout2.setGravity(Gravity.CENTER);
                        linLayout3.setGravity(Gravity.CENTER);

                        TextView lblnm = new TextView(llProducts.getContext());
                        TextView name = new TextView(llProducts.getContext());

                        TextView lblcname = new TextView(llProducts.getContext());
                        TextView cname = new TextView(llProducts.getContext());

                        TextView lblprice = new TextView(llProducts.getContext());
                        TextView price = new TextView(llProducts.getContext());

                        lblcname.setGravity(Gravity.CENTER_VERTICAL);
                        lblcname.setTextColor(Color.BLACK);

                        lblnm.setGravity(Gravity.CENTER_VERTICAL);
                        lblnm.setTextColor(Color.BLACK);

                        lblprice.setGravity(Gravity.CENTER_VERTICAL);
                        lblprice.setTextColor(Color.BLACK);

                        cname.setGravity(Gravity.CENTER_VERTICAL);
                        cname.setTextColor(Color.BLACK);

                        name.setGravity(Gravity.CENTER_VERTICAL);
                        name.setTextColor(Color.BLACK);

                        price.setGravity(Gravity.CENTER_VERTICAL);
                        price.setTextColor(Color.BLACK);

                        lblnm.setText(" Product Name:");
                        lblcname.setText(" Company Name: ");
                        lblprice.setText(" Price: ");

                        //Toast.makeText(getApplicationContext(),"Product Name:"+productSnap.child("proName").getValue().toString(),Toast.LENGTH_LONG).show();
                        name.setText(obj.getProName());
                        cname.setText(obj.getCompName());
                        price.setText("" + obj.getPrice());

                        linLayout1.addView(lblnm, 260, 50);
                        linLayout1.addView(name, 300, 50);

                        linLayout2.addView(lblcname, 260, 50);
                        linLayout2.addView(cname, 300, 50);

                        linLayout3.addView(lblprice, 260, 50);
                        linLayout3.addView(price, 300, 50);

                        LinearLayout linLayout5=new LinearLayout(prolayout.getContext());
                        linLayout5.setGravity(Gravity.CENTER_VERTICAL);
                        prolayout.addView(linLayout5,550,50);

                        lblqty= new TextView(llProducts.getContext());
                        lblqty.setId(procount*17);
                        lblqty.setGravity(Gravity.CENTER_VERTICAL);
                        lblqty.setTextColor(Color.BLACK);
                        lblqty.setText(" Quantity : 0ml");

                        linLayout5.addView(lblqty,230,50);

                        final SeekBar sbqty=new SeekBar(prolayout.getContext());
                        sbqty.setId(13*procount);
                        sbqty.setMax(10);

                        sbqty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

                        linLayout5.addView(sbqty,300,70);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void validate(View view)
    {
        proceed=0;
        String name=etname.getText().toString().trim();
        final String email=etemail.getText().toString().trim();
        String addr=etaddr.getText().toString().trim();
        final String contact=etcontact.getText().toString().trim();
        final String pincode=etpincode.getText().toString().trim();


        if(name.isEmpty())
        {
            etname.setError("Please enter a Name");
            etname.requestFocus();
            return ;
        }

        if( ! name.matches("^[A-Za-z \\\\s]{1,}[\\\\.]{0,1}[A-Za-z \\\\s]{0,}$") )
        {
            etname.setError("Only Alphabets and space allowed");
            etname.requestFocus();
            return ;
        }

        if(addr.isEmpty()){
            etaddr.setError("Please enter address");
            etaddr.requestFocus();
            return;
        }

        if(pincode.isEmpty())
        {
            etpincode.setError("Please enter pincode");
            etpincode.requestFocus();
            return;
        }

        if(pincode.length()!=6)
        {
            etpincode.setError("Pincode must be 6 digit long");
            etpincode.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            etemail.setError("Please enter a Email Id");
            etemail.requestFocus();
            return ;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            etemail.setError("Invalid Email Id");
            etemail.requestFocus();
            return;
        }

        if(contact.isEmpty())
        {
            etcontact.setError("Please enter a mobile No.");
            etcontact.requestFocus();
            return ;
        }

        if(!Patterns.PHONE.matcher(contact).matches() || contact.length()<10)
        {
            etcontact.setError(" Invalid mobile no. ");
            etcontact.requestFocus();
            return;
        }


        int flag=0; // if flag==1 indicates Atleast 1 Seekbar for qty is moved
        // if zero make toast

        for(int tlv=1;tlv<=procount;tlv++)
        {
            SeekBar skb=findViewById(13*tlv);
            if(skb.getProgress()>0)
                flag=1;
        }

        if(flag==0)
        {
            Toast.makeText(getApplicationContext(), "Please set quantity for Atleast one product", Toast.LENGTH_LONG).show();
            return;
        }

        custId=dbRefCust.push().getKey();
        Random random=new Random();
        int rnd = random.nextInt(100000)+99999;
        final String passwd = String.valueOf(rnd);
        Customer cust= new Customer(custId,name,addr,pincode,email,contact,passwd);
        dbRefCust.child(custId).setValue(cust);

        for(int tlv=1;tlv<=procount;tlv++)
        {
            SeekBar skb=findViewById(13*tlv);
            if(skb.getProgress()>0)
            {

                final String proid=proId[tlv];
                DatabaseReference dbRefDefPro= FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId).child("defaults");

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

            }
        }


        FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId).child("fkDistId").setValue(distId);
        //check(email,contact);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    GMailSender sender = new GMailSender("milkdistributor1@gmail.com", "milk123!");
                    sender.sendMail("Password for your Login",
                            "Hello, Your registration to app Distributor's diary is" +
                                    " completed by your distributor, You can now log In to application using " +
                                    " this email id and password : "+passwd+"\n Don't know about this activity, please write back to milkdistributor1@gmail.com",
                            "milkdistributor1@gmail.com", etemail.getText().toString());
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(getApplicationContext(),addCustomer.class));

                }
                catch (Exception e)
                {
                    Log.e("mylog1", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    /*private void check(final String email,final String contact)
    {
        dbRefDist= FirebaseDatabase.getInstance().getReference("Distributor");
        DatabaseReference dbTempRefCust= FirebaseDatabase.getInstance().getReference("Customer");
        dbRefBuffCust= FirebaseDatabase.getInstance().getReference("Buffcust");

        dbTempRefCust.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    for(DataSnapshot cust : snap.getChildren())
                    {
                        if(cust.getKey().equals(custId))
                            continue;

                        if (cust.child("custEmail").getValue().toString().equals(email))
                            if(deleted!=1)
                                deleteCust("Email Id");


                        if (cust.child("custContact").getValue().toString().equals(contact))
                            if(deleted!=1)
                                deleteCust("Mobile No.");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        dbRefDist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot dist : dataSnapshot.getChildren())
                {
                    if(dist.child("distEmail").getValue().toString().equals(email))
                        if(deleted!=1)
                            deleteCust("Email Id");

                    if(dist.child("distContact").getValue().toString().equals(contact))
                        if(deleted!=1)
                            deleteCust("Mobile No.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        dbRefBuffCust.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot cust : dataSnapshot.getChildren())
                {
                    if(cust.child("custEmail").getValue().toString().equals(email))
                        if(deleted!=1)
                            deleteCust("Email Id");

                    if(cust.child("custContact").getValue().toString().equals(contact))
                        if(deleted!=1)
                          deleteCust("Mobile No.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }*/

    private void addValue(float c)
    {
        total+=c;
        FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId).child("dailyCost").setValue(total);
    }

    /*public void deleteCust(String reason)
    {
        if(deleted==0)
        {
            Toast.makeText(getApplicationContext(), reason + " already registered", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId).setValue(null);
            deleted=1;
            finishAffinity();
            dialog.dismiss();
            startActivity(new Intent(getApplicationContext(), addCustomer.class));
        }
    }*/
}

