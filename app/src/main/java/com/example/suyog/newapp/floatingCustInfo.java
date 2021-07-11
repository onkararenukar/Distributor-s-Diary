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

public class floatingCustInfo extends AppCompatActivity {

    TextView tvName,tvAddr,tvEmail,tvContact,tvPincode,tvqty;
    DatabaseReference dbRefProducts;
    ValueEventListener proListner;
    LinearLayout proLL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_cust_info);

        String custId= getIntent().getStringExtra("custId");
        final String distId= SaveSharedPreference.getUserName(getApplicationContext());

        tvName= findViewById(R.id.name);
        tvAddr=findViewById(R.id.address);
        tvContact=findViewById(R.id.contact);
        tvEmail=findViewById(R.id.email);
        tvPincode=findViewById(R.id.pincode);
        proLL=findViewById(R.id.prolayout);

        DatabaseReference dbRefCust= FirebaseDatabase.getInstance().getReference("Customer").child(distId).child(custId);

        dbRefCust.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                    String address,name,email,contact,pincode;

                    name=dataSnapshot.child("custName").getValue().toString();
                    address=dataSnapshot.child("custAddr").getValue().toString();
                    email=dataSnapshot.child("custEmail").getValue().toString();
                    contact=dataSnapshot.child("custContact").getValue().toString();
                    pincode=dataSnapshot.child("custPincode").getValue().toString();

                    tvName.setText(name);
                    tvAddr.setText(address);
                    tvContact.setText(contact);
                    tvEmail.setText(email);
                    tvPincode.setText(pincode);

                DataSnapshot snap=dataSnapshot.child("defaults");

                for(DataSnapshot def : snap.getChildren())
                {
                    final LinearLayout linLayout=new LinearLayout(proLL.getContext());

                    proLL.addView(linLayout,DpTOPx(320),LinearLayout.LayoutParams.WRAP_CONTENT);
                    linLayout.setGravity(Gravity.CENTER);
                    linLayout.setOrientation(LinearLayout.HORIZONTAL);

                    String proId=def.getKey();
                    final String qty=def.getValue().toString().trim();

                    dbRefProducts= FirebaseDatabase.getInstance().getReference("product").child(distId).child(proId);

                    proListner=dbRefProducts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            TextView tvpro=new TextView(proLL.getContext());
                            tvpro.setTextColor(Color.BLACK);
                            tvpro.setPadding(DpTOPx(22),0,0,0);
                            linLayout.addView(tvpro,DpTOPx(220),LinearLayout.LayoutParams.WRAP_CONTENT);

                            String str;
                            str=dataSnapshot.child("compName").getValue().toString();
                            str+=" "+dataSnapshot.child("description").getValue().toString();
                            tvpro.setText(str);

                            tvqty=new TextView(proLL.getContext());
                            tvqty.setTextColor(Color.BLACK);
                            tvqty.setPadding(DpTOPx(20),0,0,0);

                            linLayout.addView(tvqty,DpTOPx(110),LinearLayout.LayoutParams.WRAP_CONTENT);

                            tvqty.setText(qty+" ltrs.");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                        {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public int DpTOPx(int dp)
    {
        DisplayMetrics dm= getApplicationContext().getResources().getDisplayMetrics();
        return Math.round(dp*(dm.xdpi/dm.DENSITY_DEFAULT));
    }

}
