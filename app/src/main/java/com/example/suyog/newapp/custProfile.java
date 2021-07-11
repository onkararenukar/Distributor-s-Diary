package com.example.suyog.newapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class custProfile extends AppCompatActivity{

        public TextView tvName, tvAddress, tvPincode, tvMobile, tvEmail;
        DatabaseReference dbRefDist;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cust_profile);

            tvName = (TextView) findViewById(R.id.txtName);
            tvAddress = (TextView) findViewById(R.id.txtAddress);
            tvPincode = (TextView) findViewById(R.id.txtPincode);
            tvMobile = (TextView) findViewById(R.id.txtMobile);
            tvEmail = (TextView) findViewById(R.id.txtEmail);
            String custId= getIntent().getStringExtra("custId");
            final String distId= SaveSharedPreference.getUserName(getApplicationContext());
            DatabaseReference   dbRefDist= FirebaseDatabase.getInstance().getReference();
            dbRefDist.child("Customer").child("-Mc4w1IpDP8WGMikzvBD").child("-Mc4wgexm9Va40QnrvmC").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    System.out.println("$$$$$$$$$$$$$$$$$$"+snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    String address,name,email,contact,pincode;

                    name=snapshot.child("custName").getValue().toString();
                    address=snapshot.child("custAddr").getValue().toString();
                    email=snapshot.child("custEmail").getValue().toString();
                    contact=snapshot.child("custContact").getValue().toString();
                    pincode=snapshot.child("custPincode").getValue().toString();

                    tvName.setText(name);
                    tvAddress.setText(address);
                    tvMobile.setText(contact);
                    tvEmail.setText(email);
                    tvPincode.setText(pincode);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



        }

        @Override
        public void onBackPressed()
        {
            finish();
            startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        }

        public void to_cust_home(View view)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),cust_homepage.class));
        }

}
